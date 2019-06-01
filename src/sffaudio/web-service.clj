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
 ; (:require [compojure.core :refer [defroutes GET]] )
  

   (:require [compojure.core :refer [routes GET POST PUT DELETE ANY]])
    (:require         [compojure.route :as route])
    (:require         [compojure.handler :as handler])

  (:require [ring.adapter.jetty :as ring])
  (:require [net.cgrand.enlive-html :as html])


 (:require
   [ring.adapter.jetty :refer [run-jetty]]
   [ring.middleware.reload :refer [wrap-reload]])

 (:use [net.cgrand.moustache :only [app]] )

 
(:require  [ring.middleware.reload :as ring-reload] )

)


(load "singular-service")



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(html/deftemplate friends-list "t2.html"
        [username friends friend-class]
          [:.username] (html/content username)
          [:ul.friends :li] (html/clone-for [a-friend friends]
                             (html/do-> (html/content a-friend)
                              (html/add-class friend-class))))


(defn request-handler [request]

 ( let [the-str (friends-list "Chas" ["Christophe" "Brian"] "programmer") ] 
  (println (:uri request))
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body the-str })
 )



;; https://stackoverflow.com/questions/54056579/how-to-avoid-global-state-in-clojure-when-using-wrap-reload
;     https://github.com/panta82/clojure-webdev/blob/master/src/webdev/core.clj
(def jetty-reloader #'ring-reload/reloader)

(defn web-reload []
  (let [reload-jetty! (jetty-reloader ["src"] true)]
     (reload-jetty!)))



(defn web-init-2 [ constant-file ]

    (defn kill-web-fn [web-ref] 
         (.stop web-ref) )




  (off-service "THE-WEB-SERVICE" kill-web-fn)
  (web-reload)
  ( let [ const-valss (load-constants constant-file)
 		 	     server-port (get const-valss :server-port)
          an-web-server (ring/run-jetty request-handler {:port server-port :join? false})]


 ; (println "server-port" server-port)
       (add-cron-job-to-queue2 an-web-server "THE-WEB-SERVICE" kill-web-fn)

					(defn stop-web []
								(try (kill-web-fn an-web-server)
             (catch Exception e (str " -- caught exception: " )))    
								 "web-server job stopped "		)


       )




  "web-service"
)