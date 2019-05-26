;;   https://lesterhan.wordpress.com/2015/11/19/dynamodb-(-with-amazonica/

;; (-main "amazonica" "localhost")


;; amazonica-build
(defn amazonica-create [amazonica-config my-table-name pages-to-check error-length]

  	 (let [ connection-opts {:access-key (get amazonica-config :access-key)
                            :secret-key (get amazonica-config :secret-key)
                            :endpoint  (get amazonica-config :endpoint)  }]

  (defn list-amazonica [] 
  (->> connection-opts
      (aws-dyn/list-tables)
      :table-names
      (map keyword)))


(defn table-exist? [table-name]
;(println "table-exist?")
	 (let [keyed-table-name (keyword table-name)]
 	   (-> (list-amazonica) set (contains? keyed-table-name)))) 

		(defn create-amazonica []
					(let [ create-return (aws-dyn/create-table connection-opts
					 :table-name my-table-name
					 :key-schema  [{:attribute-name "check-url"   :key-type "HASH"}
					                 {:attribute-name "the-date" :key-type "RANGE"}]
					 :attribute-definitions [{:attribute-name "check-url"      :attribute-type "S"}
					                 {:attribute-name "the-date"    :attribute-type "S"}] 
					 :provisioned-throughput 
					{:read-capacity-units 50 
					 :write-capacity-units 50}) ]	))

;; https://github.com/mcohen01/amazonica   put-item
		(defn put-item-amazonica [check-record]
    (when-not (table-exist? my-table-name) (create-amazonica) )
     ( let [     {  check-url :check-url 
                    the-date :the-date
                    the-html :the-html
                    page-ok :page-ok
																		  time-took :time-took } check-record 
					     						 trunc-html (trunc-string the-html error-length)           
					     						  adjusted-date (easy-date the-date)
                  total-bytes (count the-html)
					             new-record {  
					             	   :check-url check-url
					             	   :the-date adjusted-date
                      :total-bytes total-bytes
                      :first-html  trunc-html
                      :page-ok page-ok
																	 	   :time-took time-took}
       ]
    		(aws-dyn/put-item
												 connection-opts
												 :table-name my-table-name
												 :item new-record)	
						)  
     )			
			
(defn begins-one-amazonica 
  " ((:begins-one-amazonica my-db-obj) ``2019-05`` ``http://www.yahoo.ca``)  "
 [begins-with page-to-check]
; (println "in  begins-one-amazonica ")
; (println "my-table-name" my-table-name)
; (println "page-to-check" page-to-check)
 							     (let [ page-matches (aws-dyn/query 
                                       connection-opts
           	                          :table-name my-table-name
                                      :key-conditions 
                                           {:check-url {:attribute-value-list [page-to-check]      :comparison-operator "EQ"}
                                             :the-date {:attribute-value-list [begins-with] :comparison-operator "BEGINS_WITH"}})
								     					  plain-items (get page-matches :items) ]
								     					;  (println "plain items" page-to-check ":::" plain-items)
								       plain-items) )

(defn begins-all-amazonica 
  " ((:date-begins-amazonica my-db-obj) ``2019-05``) 
				((:begins-all) ``2019-05``)
  "
 [begins-with]
								(defn get-the-pages [items-vector check-page-obj]
								     (let [ {check-page :check-page } check-page-obj
								     plain-items (begins-one-amazonica begins-with check-page) ]
								        (concat items-vector plain-items)))
    ( let [ all-matches (reduce get-the-pages [] pages-to-check) 
             sorted-matches (sort-by :check-url    all-matches) ]
             sorted-matches ))

				(defn delete-amazonica []
       (aws-dyn/delete-table connection-opts
         :table-name my-table-name))

  {:delete-table delete-amazonica
   :put-item put-item-amazonica
   :begins-all begins-all-amazonica
   :begins-one begins-one-amazonica
   }))




