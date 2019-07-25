
(ns sff-audio.sms-event

  (:require [clj-http.client :as http-client])
(:require [  sff-audio.config-args :refer [compact-hash]])
(:require [  sff-audio.years-months :refer [ instant-time-fn] ])


)


;   https://elements.heroku.com/addons/temporize
; UTC time is +7 Vancouver time
; every day    23:10
;    https://fathomless-woodland-85635.herokuapp.com/temporize-call
; GET
; 5 retries


(defn make-api-call
  [sms-data sms-message]
  (let [{:keys [till-username till-url till-api-key phone-numbers heroku-app-name testing-sms?]} sms-data
        till-url (str till-url "?username=" till-username "&api_key=" till-api-key)
        sms-message (str sms-message " - " heroku-app-name)
        sms-params {:form-params {:phone phone-numbers, :text sms-message},
                    :content-type :json}]
    (compact-hash till-url sms-params testing-sms?)))

(defn build-sms-send
  [sms-data]    
  
  
  (fn sms-send-fn
    [sms-message]
    (let [{:keys [till-url sms-params testing-sms?]} (make-api-call sms-data sms-message)
          test-sms (compact-hash till-url sms-params testing-sms?)]
      (if (not testing-sms?)
          (http-client/post till-url sms-params)
         (println "7892789324234  my test-sms == " test-sms) 
        )
      test-sms))
  
  )


;https://tillmobile.com/
(defn sms-to-phones
  [sms-data]
  (let [sms-send-fn (build-sms-send sms-data)]
   (sms-send-fn "test sms call" )))
  

(defn single-cron-fn
  [scrape-pages-fn my-db-obj pages-to-check sms-data]
  (let [ sms-send-fn (build-sms-send sms-data)
        read-from-web true]
    (fn temporize-func
      []
      (scrape-pages-fn my-db-obj
                       pages-to-check
                       instant-time-fn
                       sms-send-fn
                       read-from-web))
    ))
