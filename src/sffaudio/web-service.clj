(ns sffaudio.web-stat
	 (:require [amazonica.aws.dynamodbv2 :as aws-dyn])  
  (:require [clojure.string :as clj-str])         
  (:require [edn-config.core :as edn-read])        
  (:require [monger.core :as mong-core])           
  (:require [monger.collection :as mong-coll])   
  (:require [monger.operators :refer :all])
  (:require [java-time :as jav-time])             
  (:require [overtone.at-at :as at-at])       
  (:require [ring.adapter.jetty :as ring])
  (:require [net.cgrand.enlive-html :as html])
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

 ( let [the-str (friends-list "Chas" ["Christophe2" "Brian"] "programmer") ] 
  (println (:uri request))
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body the-str })
 )



(defn request-handler-extra [request]

 ( let [the-str (friends-list "Chas" ["Bobbereno2" "Vinnie"] "programmer") ] 
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

(defn kill-web [web-ref] 
  (println "Killing web-service:" web-ref)
  (.stop web-ref))

(defn web-init [server-port request-hander]
  (remove-service request-hander kill-web)
  (web-reload)
  (let [web-server (ring/run-jetty request-hander {:port server-port :join? false}) ]
     (add-service request-hander kill-web web-server)))

