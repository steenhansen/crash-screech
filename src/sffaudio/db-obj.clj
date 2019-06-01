

(defn easy-date [the-date]
      (clj-str/replace the-date #"T|:" "-") )

(defn trunc-string
  [s n]
  (subs s 0 (min (count s) n)))

(load "amazonica-db")  
(load "mongolabs-db") 


(defn get-db-conn [db-conn-params]
 ( let [ {db-type :db-type db-location :db-location the-config :the-config 
 	        table-name :table-name pages-to-check :pages-to-check error-length :error-length} db-conn-params] 
       (try 
 					(case db-type :amazonica (amazonica-create the-config  table-name pages-to-check error-length) 
                    :mongolabs (mongolabs-create the-config  table-name pages-to-check error-length) )
      (catch Exception e (str db-location " -- caught exception: " (.getMessage e))))))


(defn build-db [build-db-params]
    (let [ { start-args :start-args table-name :table-name config-suffix :config-suffix 
 	                           pages-to-check :pages-to-check error-length :error-length} build-db-params
           the-config (load-config start-args  config-suffix)
           db-type (db-1-arg-config start-args)
           db-location (db-2-args-config start-args)
           db-conn-params { :db-type db-type :db-location db-location :the-config the-config 
 	                      :table-name table-name :pages-to-check pages-to-check :error-length error-length}
           my-db-funcs (get-db-conn db-conn-params)

           put-item (get  my-db-funcs :put-item) ]


																			(comment  "" ((:put-many my-db-obj) my-test-objs-a)         )
																		 (defn put-many [url-checks] 
																		 							( doseq [ url-check url-checks ] 
																		         ( put-item url-check ) ) )

						 { :delete-table (get  my-db-funcs :delete-table) 
         :put-item (get  my-db-funcs :put-item)
         :begins-all (get  my-db-funcs :begins-all)
         :begins-one (get  my-db-funcs :begins-one)
         :put-many put-many }))

(defn test-page-bytes-fn [_]
    (rand-int 30))

(defn db-handle [constant-name start-args]
 ( let [ const-valss (load-constants constant-name)
 		 	    table-name (get const-valss :db-table-name)
         pages-to-check (get const-valss :the-check-pages)
         config-suffix (get const-valss :config-suffix)
         error-length (get const-valss :error-length)
         build-db-params { :start-args start-args :table-name table-name :config-suffix config-suffix 
                           :pages-to-check pages-to-check :error-length error-length}
         my-db-obj (build-db build-db-params)


         cron-seconds (get const-valss :cron-seconds)
         cron-run-always (get const-valss :cron-run-always)
         cron-save-db (get const-valss :cron-save-db)
          cron-read-real (get const-valss :cron-read-real)

          cron-info {:cron-seconds cron-seconds :cron-run-always cron-run-always 
                                     :cron-save-db cron-save-db :cron-read-real cron-read-real}
       ]
   [my-db-obj pages-to-check cron-info]))

; (defn all-stop [at-at-stop-ref an-web-server]
;   (defn all-stops []
;     (at-at/stop at-at-stop-ref)
;     (.stop an-web-server)
;     (println "all-stop")
;   )
;   all-stops)


