




(ns sffaudio.web-stat
  (:gen-class)
	 (:require [amazonica.aws.dynamodbv2 :as aws-dyn])  
  (:require [clojure.string :as clj-str])    

(:require [clojure.main :as clj-main])    



  (:require [edn-config.core :as edn-read])  


  (:require [overtone.at-at :as at-at])       
  ;(:require [compojure.core :refer [defroutes GET]] )
  (:require [ring.adapter.jetty :as ring])            ;; ring-jetty
  (:require [net.cgrand.enlive-html :as html])        ;; enlive-html
  (:require [monger.core :as mong-core])           
  (:require [monger.collection :as mong-coll])   
  (:require [monger.operators :refer :all])
  (:require [clj-http.client :as http-client])     

  (:require  [environ.core :refer [env]])     


   ;(:use [net.cgrand.moustache :only [app]] )
)  

(def ^:const SERVER-PORT-1 8080)
(def ^:const SERVER-PORT-2 3001)

(def ^:const TABLE-NAME "ddd2")

(def ^:const THE-CHECK-PAGES   [
    {:check-page "www.sffaudio.com"               :enlive-keys[:article :div.blog-item-wrap]  :at-least 5}    
  	 {:check-page "sffaudio.herokuapp.com_rsd_rss" :enlive-keys[:item]                         :at-least 5}	
  ])

(comment  "to start" (-main "monger" "localhost")         )
(comment  "to start" (-main "amazonica" "localhost")         )

(load "config-args")  
(load "check-db")       
(load "home-page")    


(load "cron-service")  
(load "web-service")  





; (defn -tm []
;  ( let [ start-args '("monger" "localhost")
;         [my-db-obj pages-to-check]  ( db-handle "./scrape-constants.edn" start-args)
;     (scrape-pages my-db-obj pages-to-check true false test-time-fn)  ;!!! WORKS
;   )
; )


; (main "../heroku-config.edn" "monger" "heroku")

; (-main "./local-config.edn" "amazonica")
; (-main "./local-config.edn" "monger")

;(defn -main [& start-args]
(defn -main [config-file & start-args]
  ( let [ ;config-file "../prod-config.edn"
          my-db-obj (db-handle TABLE-NAME THE-CHECK-PAGES start-args config-file)

                    my-test-objs-a [ {:the-url "www.sffaudio.com"    
                        ;  :the-date "2019-06-19-01:54:03.800Z"
                          :the-html "blah 1111"
                          :the-status true
																		        :the-time 1234 }
																								{:the-url "sffaudio.herokuapp.com_rsd_rss" 		
                         ; :the-date "2019-06-18T01:54:03.801Z"       ;; ((:put-many my-db-obj) my-test-objs-a)
                          :the-html "bluh 2222"
                          :the-status true
					   												        :the-time 12346 }
																						 {:the-url "www.sffaudio.com" 
                         ; :the-date "2019-06-17-01:54:03.802Z"
                          :the-html "blah 3333"
                          :the-status true
																		        :the-time 1234 }
																								{:the-url "sffaudio.herokuapp.com_rsd_rss" 		
                          ;:the-date "2019-06-18T01:54:03.803Z"
                          :the-html "bluhss 4444"
                          :the-status false
					   												        :the-time 12346 } ]
											   one-iteme {:the-url "www.sffaudio.com"    
                          :the-html "blah 5555"
                          :the-status true
																		        :the-time 1234 }
]

((:put-items my-db-obj) my-test-objs-a)        
((:put-item my-db-obj)one-iteme)        
 

   (println "***" ((:get-all my-db-obj) "2019-06") )
 
  (web-init SERVER-PORT-1 request-handler)
  ;(web-init SERVER-PORT-2 request-handler-extra)
  ;(cron-init scrape-pages TABLE-NAME THE-CHECK-PAGES start-args config-file) 
  ;(cron-init scrape-pages-extra TABLE-NAME THE-CHECK-PAGES start-args config-file) 
  "I am outta web-stat !!!"
)

)
    



;    (-main "./local-config.edn" "monger" )


;    (-main "../heroku-config.edn" "monger"   "heroku")
;    (-main "../heroku-config.edn" "autodyne" "heroku")


