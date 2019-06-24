
(load "dynamo-db")  
(load "mongo-db") 
(load "check-data") 

(defn get-db-conn [table-name pages-to-check db-type the-config]

  ( let [ db-keyword (keyword db-type)]
   (try 
     (case db-keyword :amazonica-db (dynamo-build the-config table-name pages-to-check) 
           :monger-db (mongolabs-build the-config  table-name pages-to-check))
     (catch Exception e (println " get-db-conn - " db-keyword " -caught exception: " (.getMessage e))))))

(defn build-today-error? [get-all]
    
         (defn failed-check [found-error? url-check]
           (if-not (:check-ok url-check)
              (reduced true)                        ; return early once a failed check is found
              false))
    
        (defn today-error? []
          ( let [ yyyy-mm (this-y-m)
                  url-checks (get-all yyyy-mm) 
                  error-found (reduce failed-check false url-checks)]
           error-found))
        
  today-error?)


(defn build-db [table-name pages-to-check db-type config-file environment-utilize]
  (let [ the-config (make-config db-type config-file environment-utilize)
        my-db-funcs (get-db-conn table-name pages-to-check db-type the-config     )
        web-port (:PORT the-config)
        my-db-obj { :delete-table (:delete-table my-db-funcs) 
                   :get-all (:get-all my-db-funcs)     
                   :get-url (:get-url my-db-funcs)
                   :put-item (:put-item my-db-funcs)          
                   :put-items (:put-items my-db-funcs)
                   :today-error? (build-today-error? (:get-all my-db-funcs)   )
                   }]
    [my-db-obj web-port]))
