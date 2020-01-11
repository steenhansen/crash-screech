(ns crash-sms.mongo-db

  (:require [clojure.spec.alpha :as s])

  (:require [monger.core :as mong-core])
  (:require [monger.collection :as mong-coll])
  (:require [monger.operators :refer :all])
  (:require [clojure.string :as clj-str])

  (:require [crash-sms.config-args :refer [compact-hash]])
  (:require [crash-sms.check-data :refer [prepare-data trunc-string]])
  (:require [global-consts-vars  :refer :all]))

(defn left-pad2 [my-str] (format "%02d" my-str))

(defn next-date-time
  [yyyy-mm-d]
  (let [date-vector (clj-str/split yyyy-mm-d #"-")
        last-string (last date-vector)
        last-int (Integer/parseInt last-string)
        last-plus1 (inc last-int)
        last-padded (left-pad2 last-plus1)
        but-last (pop date-vector)
        plus1-vector (conj but-last last-padded)
        date-plus1 (clj-str/join "-" plus1-vector)]
    date-plus1))

(defn mongolabs-build
  [mongolabs-config table-name pages-to-check]
  (let [mongodb-user-pass-uri (:MONGODB_URI mongolabs-config)

        {:keys [_conn db]} (mong-core/connect-via-uri mongodb-user-pass-uri)
        db-handle db
        my-put-items  (fn put-items [check-records]
                        (s/assert vector? check-records)
                        (s/assert not-empty check-records)
                        (let [fixed-dates (prepare-data check-records table-name)]
                          (mong-coll/insert-batch db-handle table-name fixed-dates)))
        my-delete-table (fn delete-table []
                          (mong-coll/drop db-handle table-name))
        my-purge-table  (fn purge-table []
                          (mong-coll/purge-many db-handle [table-name]))

        my-put-item      (fn put-item [check-record]
                           (s/assert map? check-record)
                           (let [fixed-dates (prepare-data [check-record] table-name)
                                 fixed-rec (first fixed-dates)]
;;  {:the-url "www.sffaudio.com",
;;                     :the-date "2019-06-19-01:54:03.800Z",
;;                     :the-html "blah 1111",
;;                     :the-accurate true,
;;                     :the-time 1234}
                             (mong-coll/insert db-handle table-name fixed-rec)))



    ;(comment "" ((:get-all my-db-obj) "2019-06-19-01-54-03"))
    ;(comment "" ((:get-all my-db-obj) "2019-05"))


        my-get-all (fn get-all [begins-with]
                     (s/assert string? begins-with)
                     (s/assert not-empty begins-with)
                     (let [begin-date (trunc-string begins-with DATE-MAX-LENGTH)
                           date-plus1 (next-date-time begin-date)
                           my-conditions {:_id {$gte begins-with, $lt date-plus1}}
                           all-records (mong-coll/find-maps db-handle
                                                            table-name
                                                            my-conditions)]
                       all-records))

        my-db-alive? (fn db-alive? []
                       (try
                         (my-get-all "1939-10-21")
                         true
                         (catch Exception e
                           (print-line "MongoDb is not alive " (.getMessage e))
                           false)))

        my-get-url   (fn get-url [begins-with page-to-check]
                       (assert (and (string? begins-with) (< 0 (count begins-with))))
                       (assert (and (string? page-to-check) (< 0 (count page-to-check))))
                       (let [date-plus1 (next-date-time begins-with)
                             my-conditions  {$and [{:_id {$gte begins-with, $lt date-plus1}}
                                                   {:check-url page-to-check}]}]
                         (mong-coll/find-maps db-handle
                                              table-name
                                              my-conditions)))]

    (compact-hash my-delete-table my-db-alive? my-purge-table my-get-all my-get-url my-put-item my-put-items)))
