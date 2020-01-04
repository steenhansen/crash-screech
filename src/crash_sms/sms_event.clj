
(ns crash-sms.sms-event
  (:require [global-consts-vars  :refer :all])
  (:require [clj-http.client :as http-client])
  (:require [crash-sms.config-args :refer [compact-hash]])
  (:require [crash-sms.years-months :refer [instant-time-fn]]))


;https://tillmobile.com/

;   https://elements.heroku.com/addons/temporize
; UTC time is +7 Vancouver time
; every day    23:10
;    https://fathomless-woodland-85635.herokuapp.com/do-the-temporize-call
; GET
; 5 retries


(defn make-api-call
  ""
  [sms-data sms-message]
  (let [{:keys [till-username till-url till-api-key phone-numbers heroku-app-name]} sms-data
        till-url (str till-url "?username=" till-username "&api_key=" till-api-key)
        sms-message (str sms-message " - " heroku-app-name)
        sms-params {:form-params {:phone phone-numbers, :text sms-message},
                    :content-type :json}]
    (compact-hash till-url sms-params)))

(defn build-sms-send
  ""
  [sms-data testing-sms?]
  (fn sms-send-fn
    [sms-message]
    (let [{:keys [till-url sms-params]} (make-api-call sms-data sms-message)
          test-sms (compact-hash till-url sms-params)]
      (if-not testing-sms?
        (http-client/post till-url sms-params))
      test-sms)))

(defn sms-to-phones
  [sms-data testing-sms?]
  (let [sms-send-fn (build-sms-send sms-data testing-sms?)]
    (sms-send-fn SMS-TEST-CALL)))

(defn build-web-scrape_old   ;;; q*bert
  [scrape-pages-fn my-db-obj pages-to-check sms-data testing-sms? date-time-fn]
  (let [sms-send-fn (build-sms-send sms-data testing-sms?)
        read-from-web true]
    (fn web-scraper
      []
      (scrape-pages-fn my-db-obj
                       pages-to-check
                       date-time-fn
                       sms-send-fn
                       read-from-web))))

(defn build-web-scrape   ;;; q*bert
  [scrape-pages-fn my-db-obj pages-to-check sms-data under-test? date-time-fn]
  (let [sms-send-fn (build-sms-send sms-data under-test?)
        read-from-web (not under-test?)]
;(println "reed-from web" read-from-web)
    (fn web-scraper
      []
      (scrape-pages-fn my-db-obj
                       pages-to-check
                       date-time-fn
                       sms-send-fn
                       read-from-web))))
