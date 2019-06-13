


(load "amazonica-db")  
(load "monger-db") 

(defn get-db-conn [table-name pages-to-check db-type the-config]

( let [ db-keyword (keyword db-type)]
    (try 
 					(case db-keyword :amazonica (dynamo-build the-config table-name pages-to-check) 
                       :monger (mongolabs-build the-config  table-name pages-to-check))
(catch Exception e (println " get-db-conn - " db-keyword " -caught exception: " (.getMessage e)))
      )

    )
)

(defn build-db [table-name pages-to-check db-type config-file environment-utilize]
    (let [
           the-config (make-config db-type config-file environment-utilize)
          my-db-funcs (get-db-conn table-name pages-to-check db-type the-config     )

          ]
       { :delete-table (get my-db-funcs :delete-table) 
              :get-all (get my-db-funcs :get-all)     
              :get-url (get my-db-funcs :get-url)
             :put-item (get my-db-funcs :put-item)          
            :put-items (get my-db-funcs :put-items)}

           ))
