
;   https://elements.heroku.com/addons/temporize
; UTC time is +7 Vancouver time
; every day    23:10
;    https://fathomless-woodland-85635.herokuapp.com/temporize-call
; GET
; 5 retries


(defn make-api-call
  [sms-data sms-error-message send-hello-sms?]
  (let [{:keys [till-username till-url till-api-key phone-numbers heroku-app-name]} sms-data
        till-url (str till-url "?username=" till-username "&api_key=" till-api-key)
        sms-message (str sms-error-message " - " heroku-app-name)
        sms-params {:form-params {:phone phone-numbers, :text sms-message},
                    :content-type :json}]
    (compact-hash till-url sms-params send-hello-sms?)))

(defn build-sms-send
  [sms-data an-sms-test?]
  (defn sms-send-fn
    [sms-error-message send-hello-sms?]
    (let [{:keys [till-url sms-params]} (make-api-call sms-data sms-error-message send-hello-sms?)
          test-sms (compact-hash till-url sms-params send-hello-sms?)]
      (if an-sms-test?
         (println " tttttttttttttttttttttttttttttttttttt test" test-sms)
            (http-client/post till-url sms-params)
        )
      test-sms))
  sms-send-fn)


;https://tillmobile.com/
(defn sms-to-phones
  [sms-data]
  (let [an-sms-test? false
        sms-send-fn (build-sms-send sms-data an-sms-test?)]
(println "ddddddddddd" sms-data)
   (sms-send-fn "test sms call" an-sms-test? )))
  

(defn single-cron-fn
  [scrape-pages-fn my-db-obj pages-to-check sms-data]
  (let [an-sms-test? false
        sms-send-fn (build-sms-send sms-data an-sms-test?)
        read-from-web true]
    (defn temporize-func
      []
      (scrape-pages-fn my-db-obj
                       pages-to-check
                       instant-time-fn
                       sms-send-fn
                       read-from-web))
    temporize-func))
