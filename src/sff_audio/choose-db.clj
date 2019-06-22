
(load "dynamo-db")  
(load "mongo-db") 

(defn get-db-conn [table-name pages-to-check db-type the-config]

  ( let [ db-keyword (keyword db-type)]
   (try 
     (case db-keyword :amazonica-db (dynamo-build the-config table-name pages-to-check) 
           :monger-db (mongolabs-build the-config  table-name pages-to-check))
     (catch Exception e (println " get-db-conn - " db-keyword " -caught exception: " (.getMessage e))))))

(defn build-db [table-name pages-to-check db-type config-file environment-utilize]
  (let [ the-config (make-config db-type config-file environment-utilize)
        my-db-funcs (get-db-conn table-name pages-to-check db-type the-config     )
        web-port (:PORT the-config)
        my-db-obj { :delete-table (:delete-table my-db-funcs) 
                   :get-all (:get-all my-db-funcs)     
                   :get-url (:get-url my-db-funcs)
                   :put-item (:put-item my-db-funcs)          
                   :put-items (:put-items my-db-funcs)}]
    [my-db-obj web-port]))
