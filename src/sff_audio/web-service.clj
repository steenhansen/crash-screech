


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

