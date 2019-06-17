


;   https://elements.heroku.com/addons/temporize



(load "check-data")
(load "choose-db")


; UTC time is +7 Vancouver time


; 



; every day    23:10 

;    https://fathomless-woodland-85635.herokuapp.com/temporize-call

; GET

; 5 retries







(def ^:const 	TEMPORIZE-CALL "/temporize-call")   

(defn cron-via-url [cron-job-fn pages-to-check] 
  	   (defn url-func-call [my-db-obj] 
	       (cron-job-fn my-db-obj pages-to-check instant-time-fn))											 					
  url-func-call)

(defn single-cron-fn[cron-job-fn pages-to-check ]
  (let [ callable-cron (cron-via-url cron-job-fn pages-to-check)]
    callable-cron))


