
(ns sffaudio.web-stat
	 (:require [amazonica.aws.dynamodbv2 :as aws-dyn])  
  (:require [clojure.string :as clj-str])         
  (:require [edn-config.core :as edn-read])        
  (:require [monger.core :as mong-core])           
  (:require [monger.collection :as mong-coll])   
  (:require [monger.operators :refer :all])
  (:require [clj-http.client :as httpsff-client])     
  (:require [java-time :as jav-time])             
  (:require [overtone.at-at :as at-at])       
  (:require [compojure.core :refer [defroutes GET]] )
  (:require [ring.adapter.jetty :as ring])
  (:require [net.cgrand.enlive-html :as html])
)


(comment  "to start" (-main "mongolabs" "localhost")         )

;; (-main "mongolabs" "localhost")
;; (-main "amazonica" "localhost")
(load "config-name")  
(load "db-obj")       
(load "schedule-jobs")    
(load "home-page")    

; (defn -tm []
;  ( let [ start-args '("mongolabs" "localhost")
;         [my-db-obj pages-to-check cron-info]  ( db-handle "./scrape-constants.edn" start-args)
;     (scrape-pages my-db-obj pages-to-check true false test-time-fn)  ;!!! WORKS
;   )
; )

(defn -main [& start-args]
  ( let [ [my-db-obj pages-to-check cron-info] ( db-handle "./scrape-constants.edn" start-args)
        new-cron-job  (start-cron my-db-obj pages-to-check cron-info)      ; (stop-cron)
          my-test-objs-a [ {:check-url "http://www.yahoo.com" 
                          :the-date "2019-05-18-01:54:03.818Z"
                          :the-html "blah"
																		        :time-took 1234 }
																								{:check-url "http://www.google.com" 		
                          :the-date "2019-05-18T01:54:03.819Z"
                          :the-html "bluhsss"
					   												        :time-took 12346 }] ]
         ;;    one-months-records ((:begins-all my-db-obj)  "2019-05") 
         (add-cron-job-to-queue new-cron-job)
          
          (println (data-2-months my-db-obj 2019 5))

	   )	 
      


; (defroutes web-routes
;   (GET "/" [] ( let[ astr "<h1>Hello Worl ddddddddd</h1>"] astr))
;   (GET "/bart" [] "asdsa")
;  )
; (defonce an-web-server(ring/run-jetty #'web-routes {:port 8080 :join? false}) )
; ;    (.stop an-web-server)      (.start an-web-server)


  
  "I am outta here2312 !!!"
)


    

