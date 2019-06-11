
(defonce ^:dynamic  *service-name-list* (atom {}))
(defonce ^:dynamic  *service-kill-list* (atom {}))

(defn- func-name
  [fn-object]
;;  (let [long-name (clojure.repl/demunge (str fn-object))



  (let [long-name (clj-main/demunge (str fn-object))
        short-name (second (re-find #"(.*\/[^@]*)" long-name))]
    (if short-name short-name long-name)))

(defn add-service [service-func kill-service new-service]
  (let [ name-list @*service-name-list*
         kill-list @*service-kill-list*
         the-name (func-name service-func)       
         service-key (keyword the-name)         
         new-names (assoc-in name-list [service-key] new-service)    
         new-kills (assoc-in kill-list [service-key] kill-service) ]
       (reset! *service-name-list* new-names)   
							(reset! *service-kill-list* new-kills)))

(defn remove-service [service-func kill-service]
  (let [ name-list @*service-name-list*
         the-name (func-name service-func)
         service-key (keyword the-name)
         service-running? (contains? name-list service-key)]
     (if service-running?
         (let [ old-service (service-key name-list)]
              (kill-service old-service )))
     (let [new-names (dissoc name-list service-key)]
										(reset! *service-name-list* new-names))))

(comment  "to stop all services" (kill-services)         )
(defn kill-services []
   ( let [ name-list @*service-name-list*
           kill-list @*service-kill-list*  ]
         (println "Killing all services:")
						   (for [[service-key service-ref] name-list
												    :let [kill-service (service-key kill-list)] ]
				        (kill-service service-ref))))




															


