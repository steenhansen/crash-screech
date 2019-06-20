

(load "singular-service")
(load "temporize-event")


(defn render-parts [html-pieces]
  (apply str html-pieces))

(defn make-request-fn [temporize-func my-db-obj]
   (let [ link-sel [[:.month_content (enlive-html/nth-of-type 1)] :> enlive-html/first-child]
          section-sel {[:.a_month] [[:.month_content (enlive-html/nth-of-type 1)]]} ]  
          
(enlive-html/defsnippet link-model2 "template2.html" link-sel
  [{:keys [check-url check-date check-bytes check-html check-ok check-time]}]
  		[:span.the_url] (enlive-html/do->
        (enlive-html/content check-url))
   		[:span.the_date] (enlive-html/do->
         (enlive-html/content check-date))
   		[:span.the_bytes] (enlive-html/do->
         (enlive-html/content (str check-bytes)))
   		[:span.the_html] (enlive-html/do->
         (enlive-html/content check-html))
   		[:span.the_ok] (enlive-html/do->
         (enlive-html/content (str check-ok)))
   		[:span.the_time] (enlive-html/do->
         (enlive-html/content (str check-time))))
      
(enlive-html/defsnippet section-model2 "template2.html" section-sel      
  [{:keys [month-type month-data]} ]                                    
  [:.a_month]   (enlive-html/content month-type)
  [:.month_content] (enlive-html/content (map link-model2 month-data)))

(enlive-html/deftemplate index-page2 "template2.html"
  [{:keys [page-title month-sections]}]
  [:#title_of_page] (enlive-html/content page-title)
  [:body]   (enlive-html/content (map #(section-model2 %) month-sections)))

						(defn request-handler [request]
									  ( let [ the-uri (:uri request)
									  						 	this-y-m (this-y-m)
									  							 last-y-m (last-y-m)
									  							 this-months ((:get-all my-db-obj) this-y-m)
									  							 last-months ((:get-all my-db-obj) last-y-m)
                   current-months (vec this-months)
                   previous-months (vec last-months)                 
                   prev-name (prev-month)
                   cur-name (current-month)
                   db-data  {:page-title "SFFaudio page checks"
                                 :month-sections [ {:month-type prev-name
                                                    :month-data previous-months }
                                                   {:month-type cur-name
                                                    :month-data current-months }]}
                   page-html (render-parts (index-page2 db-data))]

									  (if (= the-uri TEMPORIZE-CALL)
									      (temporize-func my-db-obj))
									  {:status 200
									   :headers {"Content-Type" "text/html"}
									   :body page-html }
           ))
     
      )
  request-handler)


;; https://stackoverflow.com/questions/54056579/how-to-avoid-global-state-in-clojure-when-using-wrap-reload
;     https://github.com/panta82/clojure-webdev/blob/master/src/webdev/core.clj
(def jetty-reloader #'ring-reload/reloader)

(defn web-reload []
  (let [reload-jetty! (jetty-reloader ["src"] true)]
     (reload-jetty!)))

(defn kill-web [web-ref] 
  (println "Killing web-service:" web-ref)
  (.stop web-ref))

(defn web-init [server-port request-handler]
  (remove-service request-handler kill-web)
  (web-reload)
  (let [web-server (ring-jetty/run-jetty request-handler {:port server-port :join? false}) ]
     (add-service request-handler kill-web web-server)))

