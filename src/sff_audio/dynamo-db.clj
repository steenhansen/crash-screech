; https://lesterhan.wordpress.com/2015/11/19/dynamodb-(-with-amazonica/
; https://github.com/mcohen01/amazonica#dynamodbv2
; https://github.com/mcohen01/amazonica   put-item
; https://docs.aws.amazon.com/amazondynamodb/latest/APIReference/API_BatchWriteItem.html

; (load "check-data")

; (load "check-data")






(defn dynamo-build
  [amazonica-config my-table-name pages-to-check]
  (let [
        {:keys [access-key secret-key endpoint]} amazonica-config
        connection-opts (compact-hash access-key secret-key endpoint)]
    (defn list-tables
      []
      (->> connection-opts
           (aws-dyn/list-tables)
           :table-names
           (map keyword)))
    (defn table-exist?
      [table-name]
      (let [keyed-table-name (keyword table-name)]
        (-> (list-tables)
            set
            (contains? keyed-table-name))))
    (defn make-table
      []
      (let [create-return
              (aws-dyn/create-table
                connection-opts
                :table-name my-table-name
                :key-schema [{:attribute-name "check-url", :key-type "HASH"}
                             {:attribute-name "_id", :key-type "RANGE"}]
                :attribute-definitions
                  [{:attribute-name "check-url", :attribute-type "S"}
                   {:attribute-name "_id", :attribute-type "S"}]
                :provisioned-throughput {:read-capacity-units MAX-R-W-RECORDS,
                                         :write-capacity-units
                                           MAX-R-W-RECORDS})]))
    (defn put-item
      [check-record]
      (when-not (table-exist? my-table-name) (make-table))
      (let [fixed-dates (prepare-data [check-record])
            fixed-item (first fixed-dates)]
        (aws-dyn/put-item connection-opts
                          :table-name my-table-name
                          :item fixed-item)))
    (defn put-items
      [check-records]
      (when-not (table-exist? my-table-name) (make-table))
      (let [fixed-dates (prepare-data check-records)
            has-puts (for [fixed-date fixed-dates]
                       {:put-request {:item fixed-date}})]
        (aws-dyn/batch-write-item connection-opts
                                  :return-consumed-capacity "TOTAL"
                                  :return-item-collection-metrics "SIZE"
                                  :request-items {my-table-name has-puts})))
    (defn get-url
      [begins-with page-to-check]
      (let [page-matches (aws-dyn/query
                           connection-opts
                           :table-name my-table-name
                           :key-conditions
                             {:check-url {:attribute-value-list [page-to-check],
                                          :comparison-operator "EQ"},
                              :_id {:attribute-value-list [begins-with],
                                    :comparison-operator "BEGINS_WITH"}})
            plain-items (:items page-matches)]
        plain-items))
    (defn get-all
      [begins-with]
      (defn get-the-pages
        [items-vector check-page-obj]
        (let [{check-page :check-page} check-page-obj
              plain-items (get-url begins-with check-page)]
          (concat items-vector plain-items)))
      (let [all-matches (reduce get-the-pages [] pages-to-check)
            sorted-matches (sort-by :check-url all-matches)]
        sorted-matches))
    (defn delete-table
      []
      (aws-dyn/delete-table connection-opts :table-name my-table-name)
      )
    
     (defn purge-table [] 
       (if (table-exist? my-table-name)
            (delete-table)
         )
       (make-table)
       )
    
    
    (compact-hash delete-table purge-table get-all get-url put-item put-items)))
