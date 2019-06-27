


                                        ;   https://elements.heroku.com/addons/temporize




                                        ; UTC time is +7 Vancouver time


                                        ; 



                                        ; every day    23:10 

                                        ;    https://fathomless-woodland-85635.herokuapp.com/temporize-call

                                        ; GET

                                        ; 5 retries




(load "check-data")
(load "choose-db")

(def ^:dynamic  *test-results* (atom {}))



  
  ; (if (caseless-match "Testing abc" "test"))
(defn caseless-match [mixed-string start-with]
  (let [lower-mixed (clj-str/lower-case mixed-string)
        lower-start (clj-str/lower-case start-with)
        is-match (clj-str/starts-with? lower-mixed lower-start)]
  is-match))  
  

(defn make-api-call [sms-data sms-error-message]
  (let [{:keys[till-username till-api-key phone-numbers heroku-app-name]} sms-data 
        till-url (str "https://platform.tillmobile.com/api/send?username=" till-username "&api_key=" till-api-key)
        phone-list (str phone-numbers)
        sms-message (str sms-error-message " - " heroku-app-name)
        sms-params  {:form-params {:phone [ phone-list] 
                                   :text sms-message} 
                     :content-type :json}]
    [till-url sms-params]))
  
  (defn build-sms-send [sms-data sms-error-message]
      
    (defn sms-send-fn [] 
      (let [ [till-url sms-params] (make-api-call sms-data sms-error-message)
             test-sms [till-url sms-params] ] 
         ( if (caseless-match sms-error-message "TesT")
             (swap! *test-results*  assoc :sms-send-fn test-sms)
             (println "sms-send res " test-sms)   
             )
        ))
    
    sms-send-fn
  )
  

(defn single-cron-fn [scrape-pages-fn my-db-obj pages-to-check sms-data sms-error-message] 
  (let [sms-send-fn (build-sms-send sms-data sms-error-message)]  
  
    (defn temporize-func [] 
      (scrape-pages-fn my-db-obj pages-to-check instant-time-fn sms-send-fn))
  
  temporize-func))


