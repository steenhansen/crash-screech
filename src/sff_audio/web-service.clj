

;   https://stackoverflow.com/questions/26239972/clojure-enlive-applying-snippet-on-a-list
; https://github.com/swannodette/enlive-tutorial/


(declare layout_YY)
(def lookup_ww {"layout_YY" #'layout_YY})

(defn parent-template [nodes]
  (when-let [id (-> nodes 
                  (enlive-html/select [[:meta (enlive-html/attr= :name "parent_uu")]])
                  first :attrs :content)]
    (lookup_ww id)))

(defmacro inheriting [source args & forms]
  `(let [source# (enlive-html/html-resource ~source)
         parent# (parent-template source#)]
     (comp (or parent# enlive-html/emit*) (enlive-html/snippet* source# ~args ~@forms))))


(def layout_YY (inheriting "layout.html" [child_XX]
  [:col1] (enlive-html/substitute (enlive-html/select child_XX [:col1 :> :*]))
  [:col2] (enlive-html/substitute (enlive-html/select child_XX [:col2 :> :*]))
  [:col3] (enlive-html/substitute (enlive-html/select child_XX [:col3 :> :*]))))

(def child_XX 
  (inheriting "child.html" [msg]
    [:col2 :div] (enlive-html/content msg))
)



(println "11111111111111111111111111111111111111111111111111")

(println (apply str (child_XX "helloSa")))

(println "22222222222222222222222222222222222222")



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(load "singular-service")
(load "temporize-event")


;;  https://github.com/clojure-cookbook/clojure-cookbook/blob/master/07_webapps/7-11_enlive.asciidoc



; https://github.com/cgrand/enlive/tree/master/examples/net/cgrand/enlive_html/examples/inheritance


; https://github.com/cgrand/enlive/wiki/Table-and-Layout-Tutorial,-Part-4:-Duplicating-Elements-and-Nested-Transformations


(def ^:dynamic *dummy-context*
     {:title "Enlive Template2 Tutorial"
      :sections [
                 {:type "May"
                   :data [{   :check-url "the-url 11111"        
					             	  :check-date "dashed-date 1111"   
                     :check-bytes "total-bytes 111"     
                     :check-html  "trunc-html 111"     
                     :check-ok "the-status 11"        
																	 	  :check-time "the-time 1"}
                          
                       {   :check-url "the-url 2222"        
					             	  :check-date "dashed-date 222"   
                     :check-bytes "total-bytes 2"     
                     :check-html  "trunc-html 2"     
                     :check-ok "the-status 2 "        
																	 	  :check-time "the-time 2"}
                          ]
                  }
                 
               {  :type "June"
                   :data [{   :check-url "the-url a"        
					             	  :check-date "dashed-date a"   
                     :check-bytes "total-bytes a"     
                     :check-html  "trunc-html a"     
                     :check-ok "the-status a"        
																	 	  :check-time "the-time a"}
                          
                       {   :check-url "the-url b"        
					             	  :check-date "dashed-date b"   
                     :check-bytes "total-bytes b"     
                     :check-html  "trunc-html b"     
                     :check-ok "the-status b "        
																	 	  :check-time "the-time b"}
                          ]
                  }
                 
                 
                 
                 
                 
                 ]})


;; =============================================================================
;; Templates
;; =============================================================================

; we only want to select a model link
(def ^:dynamic *link-sel* [[:.content (enlive-html/nth-of-type 1)] :> enlive-html/first-child])
;(def ^:dynamic *link-sel* [:.content  ])

(enlive-html/defsnippet link-model "template2.html" *link-sel*
  [{:keys [check-url check-date check-bytes check-html check-ok check-time]}
    ]
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
         (enlive-html/content (str check-time)))
  )

; we only want to select the model h2 ul range
(def ^:dynamic *section-sel* {[:.the_type] [[:.content (enlive-html/nth-of-type 1)]]})

(enlive-html/defsnippet section-model "template2.html" *section-sel*      
  [{:keys [type data]} ]                                    
  [:.the_type]   (enlive-html/content type)
  [:.content] (enlive-html/content (map link-model data)))

(enlive-html/deftemplate index-page "template2.html"
  [{:keys [title sections]}]
  [:#title] (enlive-html/content title)
 [:body]   (enlive-html/content (map #(section-model %) sections)))


(defn render [t]
  (apply str t))










(defn make-request-fn [temporize-func table-name pages-to-check db-type config-file environment-utilize]
  (let [ [my-db-obj _web-port] (build-db table-name pages-to-check db-type config-file environment-utilize)]
									
									(defn request-handler [request]
									  ( let [ the-uri (:uri request)
									  							this-y-m (this-y-m)
									  							last-y-m (last-y-m)

									  							  this-months ((:get-all my-db-obj) this-y-m)
									  							  last-months ((:get-all my-db-obj) last-y-m)
                    
                    vc-this (vec this-months)
                    vc-last (vec last-months)
                    
                    
                    all-data  {:title "Enlive Template2 Tutorial"
                           :sections [
                 {:type "May"
                   :data [{   :check-url "the-url 111111212easdsd"        
					             	  :check-date "dashed-date 1111"   
                     :check-bytes "total-bytes 111"     
                     :check-html  "trunc-html 111"     
                     :check-ok "the-status 11"        
																	 	  :check-time "the-time 1"}
                          
                       {   :check-url "the-url 2222"        
					             	  :check-date "dashed-date 222"   
                     :check-bytes "total-bytes 2"     
                     :check-html  "trunc-html 2"     
                     :check-ok "the-status 2 "        
																	 	  :check-time "the-time 2"}
                          ]
                  }
                 
               {  :type "June"
                   :data [{   :check-url "the-url a"        
					             	  :check-date "dashed-date a"   
                     :check-bytes "total-bytes a"     
                     :check-html  "trunc-html a"     
                     :check-ok "the-status a"        
																	 	  :check-time "the-time a"}
                          
                       {   :check-url "the-url b"        
					             	  :check-date "dashed-date b"   
                     :check-bytes "total-bytes b"     
                     :check-html  "trunc-html b"     
                     :check-ok "the-status b "        
																	 	  :check-time "the-time b"}
                          ]
                  } ] }
                 
               
                 
                    test-data  {:title "Enlive Template2 Tutorial"
                           :sections [                 {:type "May"
                   :data vc-last
                  }
                 
               {  :type "June"
                   :data vc-this
                  } ] }
               
               

                   tttt (render (index-page test-data))	
                 
                 
                 
                 
                    
                    
                    
                    	]
						

               
                 
(println "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")                 
(println all-data)                 
(println "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb")                 
(println test-data)
(println "cccccccccccccccccccccccccccccccccccccccccc")    

									  (if (= the-uri TEMPORIZE-CALL)
									      (temporize-func my-db-obj))
									  {:status 200
									   :headers {"Content-Type" "text/html"}
									   :body tttt }
           )
           
           )




   request-handler
   )
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

(defn web-init [server-port request-handler]
  (remove-service request-handler kill-web)
  (web-reload)
  (let [web-server (ring-jetty/run-jetty request-handler {:port server-port :join? false}) ]
     (add-service request-handler kill-web web-server)))

