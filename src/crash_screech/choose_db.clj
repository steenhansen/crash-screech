(ns crash-screech.choose-db
  (:require [clojure.string :as clj-str])
  (:require [clojure.pprint :as prt-prn])
  (:require [global-consts-vars  :refer :all])
  (:require [crash-screech.config-args :refer [make-config compact-hash]])
  (:require [crash-screech.dynamo-db  :refer [dynamo-build]])
  (:require [crash-screech.mongo-db :refer [mongolabs-build]])
  (:require [crash-screech.years-months :refer [current-yyyy-mm current-yyyy-mm-dd]]))

(comment
  (get-phone-nums "12345678901,01234567890")
  ; ["12345678901" "01234567890"]
  )
(defn get-phone-nums
  [phone-comma-string]
  (let [phone-spaces (clj-str/split phone-comma-string #",")
        phone-numbers (map clj-str/trim phone-spaces)
        phone-vector (vec phone-numbers)]
    phone-vector))

(comment
  (let  [the-config (make-config USE_MONGER_DB TEST-CONFIG-FILE IGNORE-ENV-VARS)
         my-db-funcs (get-db-conn T-TEST-COLLECTION [] USE_MONGER_DB the-config)]
    my-db-funcs)
  ; {:my-delete-table
  ;  #function[crash-screech.mongo-db/mongolabs-build/delete-table--126143],
  ;  :my-purge-table ...
  )
(defn get-db-conn
  [table-name pages-to-check db-type the-config]
  (let [db-keyword (keyword db-type)]
    (try (case db-keyword
           :amazonica-db (dynamo-build the-config table-name pages-to-check)
           :monger-db (mongolabs-build the-config table-name pages-to-check))
         (catch Exception e
           (println " get-db-conn - " db-keyword
                    " -caught exception: " (.getMessage e))))))

(defn build-empty-month?
  [get-all-fn]
  (fn empty-month?

    ([]
     (let [yyyy-mm (current-yyyy-mm)
           url-checks (get-all-fn yyyy-mm)
           months-checks (count url-checks)]
       (if (zero? months-checks) true false)))

    ([yyyy-mm]
     (let [url-checks (get-all-fn yyyy-mm)
           months-checks (count url-checks)]
       (if (zero? months-checks) true false)))))

(defn build-today-error?
  [get-all-fn]
  (let [FOUND-FAILED-CHECK true
        ALL-ACCURATE-CHECKS false
        my-failed-check (fn failed-check
                          [found-error? url-check]
                          (if (:check-accurate url-check)
                            ALL-ACCURATE-CHECKS            ; return true early once a
                            (reduced FOUND-FAILED-CHECK)))]   ; failed check is found
    (fn today-error?

      ([]
       (let [yyyy-mm-dd (current-yyyy-mm-dd)
             url-checks (get-all-fn yyyy-mm-dd)
             error-found (reduce my-failed-check false url-checks)]
         error-found))

      ([yyyy-mm-dd]
       (let  [url-checks (get-all-fn yyyy-mm-dd)
              error-found (reduce my-failed-check false url-checks)]
         error-found)))))

(comment
  (let [[my-db-obj web-port cron-url sms-data] (build-db T-TEST-COLLECTION
                                                         []
                                                         USE_MONGER_DB
                                                         TEST-CONFIG-FILE
                                                         IGNORE-ENV-VARS)]
    [my-db-obj web-port cron-url sms-data])
  ; [{:get-url  #function[crash-screech.mongo-db/mongolabs-build/get-url--126151],
  ;   :empty-month? #function[crash-screech.choose-db/build-empty-month?/empty-month?--126161],
  ;   :put-items #function[crash-screech.mongo-db/mongolabs-build/put-items--126141],
  ;   :today-error?  #function[crash-screech.choose-db/build-today-error?/today-error?--126166],
  ;   :put-item  #function[crash-screech.mongo-db/mongolabs-build/put-item--126147],
  ;   :delete-table  #function[crash-screech.mongo-db/mongolabs-build/delete-table--126143],
  ;   :get-all  #function[crash-screech.mongo-db/mongolabs-build/get-all--126149],
  ;   :table-name "_test_collection_",
  ;   :purge-table  #function[crash-screech.mongo-db/mongolabs-build/purge-table--126145]}
  ;      "8080"
  ;      "/url-for-cron-execution"
  ;  {:till-username "abcdefghijklmnopqrstuvwxyz1234",
  ;   :till-url "https://platform.tillmobile.com/api/send",
  ;   :till-api-key "1234567890abcdefghijklmnopqrstuvwxyz1234",
  ;   :phone-numbers ["12345678901" "12345678901" "12345678901"],
  ;   :heroku-app-name
  ;   "https://fathomless-woodland-85635.herokuapp.com/",
  ;   :testing-sms? true,
  ;   :send-test-sms-url "/zxc"}]
  )

(defn build-db
  [table-name pages-to-check db-type config-file environment-utilize]
  (let [the-config (make-config db-type config-file environment-utilize)
        my-db-funcs (get-db-conn table-name pages-to-check db-type the-config)
        web-port (:PORT the-config)
        cron-url (:CRON_URL_DIR the-config)
        till-username (:TILL_USERNAME the-config)
        till-url (:TILL_URL the-config)
        till-api-key (:TILL_API_KEY the-config)
        phone-comma-string (:PHONE_NUMBERS the-config)
        phone-numbers (get-phone-nums phone-comma-string)
        heroku-app-name (:HEROKU_APP_NAME the-config)
        testing-sms? (:TESTING_SMS the-config)
        send-test-sms-url (:SEND_TEST_SMS_URL the-config)
        sms-data (compact-hash till-username
                               till-url
                               till-api-key
                               phone-numbers
                               heroku-app-name
                               testing-sms?
                               send-test-sms-url)
        get-all (:my-get-all my-db-funcs)
        my-db-obj {:delete-table (:my-delete-table my-db-funcs),
                   :purge-table (:my-purge-table my-db-funcs),
                   :get-all get-all,
                   :get-url (:my-get-url my-db-funcs),
                   :put-item (:my-put-item my-db-funcs),
                   :put-items (:my-put-items my-db-funcs),
                   :empty-month? (build-empty-month? get-all),
                   :today-error? (build-today-error? get-all)
                   :table-name table-name}]
    (if  (= PRODUCTION-COLLECTION table-name)
      (prt-prn/pprint the-config))
    [my-db-obj web-port cron-url sms-data]))
