
(ns crash-screech.sms-event

  (:require [clj-http.client :as http-client])
  (:require [crash-screech.config-args :refer [compact-hash]])
  (:require [crash-screech.years-months :refer [instant-time-fn]]))


;https://tillmobile.com/

;   https://elements.heroku.com/addons/temporize
; UTC time is +7 Vancouver time
; every day    23:10
;    https://fathomless-woodland-85635.herokuapp.com/temporize-call
; GET
; 5 retries

;(defonce ^:dynamic *sms-was-executed* (atom {}))


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
;;; testing-sms? was not in expected value from make-api-call          test-sms (compact-hash till-url sms-params testing-sms?)
          test-sms (compact-hash till-url sms-params)
]
      (reset!  global-consts-vars/*sms-was-executed* true)
      (if-not testing-sms?
        (http-client/post till-url sms-params))
      test-sms)))

(defn sms-to-phones
  [sms-data testing-sms?]
  (let [sms-send-fn (build-sms-send sms-data testing-sms?)]
    (sms-send-fn "test sms call")))


;;;;  build-web-scrapE
(defn build-web-scrap
  [scrape-pages-fn my-db-obj pages-to-check sms-data testing-sms? date-time-fn]
  (let [sms-send-fn (build-sms-send sms-data testing-sms?)
        read-from-web true]
; q*bert
;(println "time in here  " (instant-time-fn))
    (fn temporize-func
      []
      (scrape-pages-fn my-db-obj
                       pages-to-check
                       date-time-fn
                      ; instant-time-fn
                       sms-send-fn
                       read-from-web))))

