(ns crash-sms.fake-db
  (:require [clojure.string :as clj-str])
  (:require [crash-sms.config-args :refer [compact-hash]])
  (:require [crash-sms.check-data :refer [prepare-data trunc-string]])
  (:require [global-consts-vars  :refer :all]))





(def ^:dynamic  *fake-db-records* (atom {}))


(defn fake-build
  []
  (let [my-delete-table (fn delete-table []
                          (reset! *fake-db-records* {}))

        my-purge-table  (fn purge-table []
                          (reset! *fake-db-records* {}))


        my-put-item      (fn put-item [check-record]
                           (let [fixed-dates (prepare-data [check-record] "fake-db")   ;; table-name
;_ (println "eeeeeeeeeeeeeeeeeeee")
;_ (println "fixed-dates" fixed-dates)
;_ (println "ffffffffffffffffffffff")
                                 fixed-rec (first fixed-dates)
                                 the-id (:_id fixed-rec)
                                 key-id (keyword the-id)
                                 db-records @*fake-db-records*
                                 new-db-recs (assoc db-records key-id fixed-rec)
                                 map-sorted (into (sorted-map) (sort-by :_id new-db-recs))
                             ;    _ (println "map-sorted" map-sorted)
                                 ]
                             (reset! *fake-db-records* map-sorted)))

        my-put-items  (fn put-items [check-records]
                        (doseq [a-record check-records]
                          (my-put-item a-record))
                        @*fake-db-records*)

        my-get-all (fn get-all [begins-with]
                     (let [begin-date (trunc-string begins-with DATE-MAX-LENGTH)
                           db-records @*fake-db-records*

                           filter-by-date (fn [a-record]
                                            (let [[id-key value] a-record
                                                  the-id (name id-key)]
                                              (clojure.string/starts-with? the-id begins-with)))
                           filtered-recs (filter filter-by-date db-records)

                           filtered-vals (vals filtered-recs)]
                       filtered-vals))

        my-db-alive? (fn db-alive? []
                       true)

        my-get-url   (fn get-url [begins-with page-to-check]
                             (println "get-url page-to-check" page-to-check)
                       (let [date-urls (my-get-all begins-with)
                             _ (println "get-url date-urls" date-urls)

                             filter-by-url (fn [a-record]
                                             (let [check-url (:check-url a-record)]
                                               (println "in get url 66666666666666666" check-url page-to-check)
                                               (clojure.string/starts-with? check-url page-to-check)

))
                             filtered-recs (filter filter-by-url date-urls)]
                         filtered-recs))]

    (compact-hash my-delete-table my-db-alive? my-purge-table my-get-all my-get-url my-put-item my-put-items)))
