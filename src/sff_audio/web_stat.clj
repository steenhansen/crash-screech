
(ns sff-audio.web-stat
  (:gen-class)
  (:require [amazonica.aws.dynamodbv2 :as aws-dyn])  
  (:require [clojure.string :as clj-str])    
  (:require [clojure.main :as clj-main])    
  (:require [edn-config.core :as edn-read])  
  (:require [overtone.at-at :as at-at])       
  (:require [ring.adapter.jetty :as ring-jetty])           
  (:require [net.cgrand.enlive-html :as enlive-html])       
  (:require [monger.core :as mong-core])      
  (:require [monger.collection :as mong-coll])   
  (:require [monger.operators :refer :all])
  (:require [clj-http.client :as http-client])     
  (:require [me.raynes.fs :as file-sys])
  (:require  [ring.middleware.reload :as ring-reload] )
  (:require [java-time])
)  




(def ^:const TABLE-NAME "ddd4")

(def ^:const THE-CHECK-PAGES   [
    {:check-page "www.sffaudio.com"               :enlive-keys[:article :div.blog-item-wrap]  :at-least 5}    
  	 {:check-page "sffaudio.herokuapp.com_rsd_rss" :enlive-keys[:item]                         :at-least 5}	
  ])

 (comment  "to start" (-main "monger-db" "./local-config.edn" "use-environment")         )
 (comment  "to start" (-main "amazonica-db" "./local-config.edn" "ignore-environment")         )

 (load "config-args")   
 (load "home-page")     
 (load "web-service")  
 (load "check-data")  

 
 (load "cron-service") 
 
 ;(load "test") 




(defn -main [db-type config-file environment-utilize]
  ( let [ [my-db-obj web-port] (build-db TABLE-NAME THE-CHECK-PAGES db-type config-file environment-utilize) 

										int-port (Integer/parseInt web-port)
                    my-test-objs-a [ {:the-url "www.sffaudio.com" 
                          :the-date "2019-06-19-01:54:03.800Z"   
                          :the-html "blah 1111"
                          :the-status true
																		        :the-time 1234 }
																								{:the-url "sffaudio.herokuapp.com_rsd_rss"
																								:the-date "2019-06-19-01:54:03.800Z" 		
                          :the-html "bluh 2222"
                          :the-status true
					   												        :the-time 12346 }
																						 {:the-url "www.sffaudio.com" 
																						 :the-date "2019-05-19-01:54:03.800Z"
                          :the-html "blah 3333"
                          :the-status true
																		        :the-time 1234 }
																								{:the-url "sffaudio.herokuapp.com_rsd_rss" 
																								:the-date "2019-05-19-01:54:03.800Z"		
                          :the-html "bluhss 4444"
                          :the-status false
			   												        :the-time 12346 } ]
											   one-iteme {:the-url "www.sffaudio.com"    
											              :the-date "2019-06-19-01:54:03.800Z"
                          :the-html "blah 5555"
                          :the-status true
																		        :the-time 1234 } 

 temporize-func (single-cron-fn scrape-pages-fn THE-CHECK-PAGES) 

request-handler (make-request-fn temporize-func 
                       TABLE-NAME THE-CHECK-PAGES db-type config-file environment-utilize)

																		        ]

;((:put-items my-db-obj) my-test-objs-a)        


;((:put-item my-db-obj)one-iteme)        


;(println "***" ((:get-all my-db-obj) "2019-06") )
 

 (web-init int-port request-handler)           



 ; (cron-init single-cron-fn TABLE-NAME THE-CHECK-PAGES db-type config-file environment-utilize) 

  "I am outta web-stat !!!"
  )

)
    
