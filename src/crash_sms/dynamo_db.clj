; https://lesterhan.wordpress.com/2015/11/19/dynamodb-(-with-amazonica/
; https://github.com/mcohen01/amazonica#dynamodbv2
; https://docs.aws.amazon.com/amazondynamodb/latest/APIReference/API_BatchWriteItem.html

(ns crash-sms.dynamo-db
  (:require [clojure.spec.alpha :as s])
  (:require [amazonica.aws.dynamodbv2 :as aws-dyn])
  (:require [clojure.string :as clj-str])
  (:require [crash-sms.config-args :refer  [compact-hash]])
  (:require [crash-sms.check-data  :refer [prepare-data]])
  (:require [global-consts-vars  :refer :all]))

(defn dynamo-build
  [amazonica-config my-table-name pages-to-check]
  (let [{:keys [access-key secret-key endpoint]} amazonica-config
        connection-opts (compact-hash access-key secret-key endpoint)

        my-list-tables  (fn list-tables []
                          (->> connection-opts
                               (aws-dyn/list-tables)
                               :table-names
                               (map keyword)))

        my-table-exist? (fn table-exist?  [table-name]
                          (let [keyed-table-name (keyword table-name)]
                            (-> (my-list-tables)
                                set
                                (contains? keyed-table-name))))

        my-db-alive? (fn db-alive? []
                       (try
                         (my-table-exist? "_any_dynamo_name")
                         true
                         (catch Exception e
                           (print-line "DynamoDb is not alive " (.getMessage e))        ;; Connection pool shut down
                           (print-line "On command line run: ")
                           (print-line "                      java \"-Djava.library.path=./DynamoDBLocal_lib\" -jar DynamoDBLocal.jar -sharedDb ")
                           false)))

        my-make-table  (fn make-table []
                         (let [my-key-schema [{:attribute-name "check-url", :key-type "HASH"}
                                              {:attribute-name "_id", :key-type "RANGE"}]
                               my-attribute-defn  [{:attribute-name "check-url", :attribute-type "S"}
                                                   {:attribute-name "_id", :attribute-type "S"}]
                               my-provision-through  {:read-capacity-units MAX-R-W-RECORDS,
                                                      :write-capacity-units
                                                      MAX-R-W-RECORDS}]
                           (aws-dyn/create-table
                            connection-opts
                            :table-name my-table-name
                            :key-schema my-key-schema
                            :attribute-definitions my-attribute-defn
                            :provisioned-throughput my-provision-through)))

        my-get-url    (fn get-url [begins-with page-to-check]
                        (s/assert string? begins-with)
                        (s/assert not-empty begins-with)
                        (s/assert string? page-to-check)
                        (s/assert not-empty page-to-check)
                        (when-not (my-table-exist? my-table-name) (my-make-table))
                        (let [my-key-conditions {:check-url {:attribute-value-list [page-to-check],
                                                             :comparison-operator "EQ"},
                                                 :_id {:attribute-value-list [begins-with],
                                                       :comparison-operator "BEGINS_WITH"}}
                              page-matches (aws-dyn/query
                                            connection-opts
                                            :table-name my-table-name
                                            :key-conditions my-key-conditions)
                              plain-items (:items page-matches)]
                          plain-items))

        my-delete-table   (fn delete-table []
                            (aws-dyn/delete-table connection-opts :table-name my-table-name))

        my-purge-table    (fn purge-table []
                            (if (my-table-exist? my-table-name) (my-delete-table))
                            (my-make-table))

        my-get-all   (fn get-all [begins-with]
                       (s/assert string? begins-with)
                       (s/assert not-empty begins-with)
                       (let [my-get-the-pages (fn get-the-pages
                                                [items-vector check-page-obj]
                                                (let [{check-page :check-page} check-page-obj
                                                      plain-items (my-get-url begins-with check-page)]
                                                  (concat items-vector plain-items)))]
                         (let [all-matches (reduce my-get-the-pages [] pages-to-check)
                               sorted-matches (sort-by :check-url all-matches)]
                           sorted-matches)))

        my-put-item  (fn put-item [check-record]
                       (s/assert map? check-record)

                       (when-not (my-table-exist? my-table-name) (my-make-table))
                       (let [fixed-dates (prepare-data [check-record] my-table-name)
                             fixed-item (first fixed-dates)]
                         (aws-dyn/put-item connection-opts
                                           :table-name my-table-name
                                           :item fixed-item)))

        my-put-items  (fn put-items [check-records]
                        (s/assert vector? check-records)
                        (s/assert not-empty check-records)
                        (when-not (my-table-exist? my-table-name) (my-make-table))
                        (let [fixed-dates (prepare-data check-records my-table-name)
                              has-puts (for [fixed-date fixed-dates]
                                         {:put-request {:item fixed-date}})

                              my-request-items {my-table-name has-puts}]
                          (aws-dyn/batch-write-item connection-opts
                                                    :return-consumed-capacity "TOTAL"
                                                    :return-item-collection-metrics "SIZE"
                                                    :request-items my-request-items)))]

    (compact-hash my-delete-table my-db-alive? my-purge-table my-get-all my-get-url my-put-item my-put-items)))
