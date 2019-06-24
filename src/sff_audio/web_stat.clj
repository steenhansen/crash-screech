

(ns sff-audio.web-stat
  (:gen-class)
  (:require [amazonica.aws.dynamodbv2 :as aws-dyn])  
  (:require [clojure.string :as clj-str])    
  (:require [clojure.main :as clj-main])    
  (:require [edn-config.core :as edn-read])  
  (:require [overtone.at-at :as at-at])       
  (:require [net.cgrand.enlive-html :as enlive-html])       
  (:require [monger.core :as mong-core])      
  (:require [monger.collection :as mong-coll])   
  (:require [monger.operators :refer :all])
  (:require [clj-http.client :as http-client])     
  (:require [me.raynes.fs :as file-sys])
  (:require [ring.adapter.jetty :as ring-jetty])      
  (:require  [ring.middleware.reload :as ring-reload] )
  (:require  [ring.util.response :as ring-response] )
  (:require [java-time])
  )  

(load "global-consts")   
(load "config-args")   
(load "web-service") 
(load "check-data")  
(load "cron-service") 

                                        ; main called by Heroku, has no cron-init() job, relies on temporize-func()
(comment "to start" (-main "monger-db" "./local-config.edn" "use-environment")         )
(defn -main [db-type config-file environment-utilize]
  ( let [ [my-db-obj web-port cron-url] (build-db DB-TABLE-NAME THE-CHECK-PAGES db-type config-file environment-utilize) 
         int-port (Integer/parseInt web-port)
         temporize-func (single-cron-fn scrape-pages-fn THE-CHECK-PAGES) 
         request-handler (make-request-fn temporize-func my-db-obj cron-url)
         
          ; test-one {:the-url "www.sffaudio.com"    
          ;          :the-date "2019-06-22-02:54:03.800Z"
          ;          :the-html "blah 5555"
          ;          :the-accurate true
          ;          :the-time 1234 } 
          ;  
          ]
   ;((:put-item my-db-obj) test-one)  
                      ;( println "****" ((:get-all my-db-obj) "2019-06-19-01-54-03")    )

  ;  ( println "****" ((:today-error? my-db-obj))    )
   
   (web-init int-port request-handler)))

                                        ; dev main, has scrape-pages-fn as an at-at scheduled job
                                        ; (kill-services) will delete web-server and at-at-scheduled job     
(comment  "local amazonica db" (-main "amazonica-db" "./local-config.edn" "ignore-environment")         )
(comment  "local monger db" (-main "monger-db" "./local-config.edn" "ignore-environment")         )
(comment  "real monger db, config file outside project" (-main "monger-db" "../heroku-config.edn" "ignore-environment")         )
(defn -main-dev [db-type config-file environment-utilize]
  ( let [ [my-db-obj web-port cron-url] (build-db DB-TABLE-NAME THE-CHECK-PAGES db-type config-file environment-utilize) 
         int-port (Integer/parseInt web-port)
         test-many [ {:the-url "www.sffaudio.com" 
                      :the-date "2019-06-19-01:54:03.800Z"   
                      :the-html "blah 1111"
                      :the-accurate true
                      :the-time 1234 }
                    {:the-url "sffaudio.herokuapp.com_rsd_rss"
                     :the-date "2019-06-19-01:54:03.800Z" 		
                     :the-html "bluh 2222"
                     :the-accurate true
                     :the-time 12346 }
                    {:the-url "www.sffaudio.com" 
                     :the-date "2019-05-19-01:54:03.800Z"
                     :the-html "blah 3333"
                     :the-accurate true
                     :the-time 1234 }
                    {:the-url "sffaudio.herokuapp.com_rsd_rss" 
                     :the-date "2019-05-19-01:54:03.800Z"		
                     :the-html "bluhss 4444"
                     :the-accurate false
                     :the-time 12346 } ]
         temporize-func (single-cron-fn scrape-pages-fn THE-CHECK-PAGES) 
         request-handler (make-request-fn temporize-func my-db-obj cron-url)
         test-one {:the-url "www.sffaudio.com"    
                   :the-date "2019-06-19-01:54:03.800Z"
                   :the-html "blah 5555"
                   :the-accurate true
                   :the-time 1234 } ]
                                        ;((:put-items my-db-obj) test-many)        
                                        ;((:put-item my-db-obj) test-one)        
   (web-init int-port request-handler)           
   (cron-init scrape-pages-fn my-db-obj THE-CHECK-PAGES)))
