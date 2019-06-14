


(load "singular-service")
(load "temporize-event")

(enlive-html/deftemplate friends-list "t2.html"
        [username friends friend-class]
          [:.username] (enlive-html/content username)
          [:ul.friends :li] (enlive-html/clone-for [a-friend friends]
                             (enlive-html/do-> (enlive-html/content a-friend)
                              (enlive-html/add-class friend-class))))



(defn make-request-fn [temporize-func]

									(defn request-handler [request]
									  ( let [the-str (friends-list "iChas" ["Christophe2" "Brian"] "programmer")

									  							the-uri (:uri request)


									   ] 
									  (println (:uri request) request)
									       
									        (if (= the-uri TEMPORIZE-CALL)
									             (temporize-func))

									  {:status 200
									   :headers {"Content-Type" "text/html"}
									   :body the-str })
									 )

   request-handler
)




; (defn request-handler [request]
;   ( let [the-str (friends-list "Chas" ["Christophe2" "Brian"] "programmer")

;   							the-uri (:uri request)


;    ] 
;   (println (:uri request))
       
;       ;  (if (= the-uri "temporize")
;           ;   (cron-init scrape-pages TABLE-NAME THE-CHECK-PAGES db-type config-file environment-utilize) 
;                   ;     (mongolabs-build the-config  table-name pages-to-check))
; ;

;   {:status 200
;    :headers {"Content-Type" "text/html"}
;    :body the-str })
;  )

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

(defn web-init [server-port request-handler cron-function ]
  (remove-service request-handler kill-web)
  (web-reload)
  (let [web-server (ring-jetty/run-jetty request-handler {:port server-port :join? false}) ]
     (add-service request-handler kill-web web-server)))

