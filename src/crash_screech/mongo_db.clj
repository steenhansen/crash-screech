(ns crash-screech.mongo-db
  (:require [monger.core :as mong-core])
  (:require [monger.collection :as mong-coll])
  (:require [monger.operators :refer :all])
  (:require [clojure.string :as clj-str])

  (:require [crash-screech.config-args :refer [compact-hash]])
  (:require [crash-screech.check-data :refer [prepare-data trunc-string]])
  (:require [sff-global-consts  :refer :all]))

(defn left-pad2 [my-str] (format "%02d" my-str))

(defn date-plus-1
  [begins-with]
  (let [date-vector (clj-str/split begins-with #"-")
        last-string (last date-vector)
        last-int (Integer/parseInt last-string)
        last-plus1 (inc last-int)
        last-padded (left-pad2 last-plus1)
        but-last (pop date-vector)
        plus1-vector (conj but-last last-padded)
        date-plus1 (clj-str/join "-" plus1-vector)]
    date-plus1))

(defn mongolabs-build
  [mongolabs-config my-collection pages-to-check]
  (let [mongodb-user-pass-uri (:MONGODB_URI mongolabs-config)
        {:keys [_conn db]} (mong-core/connect-via-uri mongodb-user-pass-uri)
        db-handle db

        my-put-items  (fn put-items [check-records]
                        (let [fixed-dates (prepare-data check-records)]
                          (mong-coll/insert-batch db-handle my-collection fixed-dates)))

        my-delete-table (fn delete-table [] 
                               (mong-coll/drop db-handle my-collection))

        my-purge-table  (fn purge-table [] 
                            (mong-coll/purge-many db-handle [my-collection]))

        my-put-item      (fn put-item [check-record]
                           (let [fixed-dates (prepare-data [check-record])
                                 fixed-rec (first fixed-dates)]
                             (mong-coll/insert db-handle my-collection fixed-rec)))
    ;(comment "" ((:get-all my-db-obj) "2019-06-19-01-54-03"))
    ;(comment "" ((:get-all my-db-obj) "2019-05"))
        my-get-all (fn get-all [begins-with]
                     (let [begin-date (trunc-string begins-with DATE-MAX-LENGTH)
                           date-plus1 (date-plus-1 begin-date)]
                       (mong-coll/find-maps db-handle
                                            my-collection
                                            {:_id {$gte begins-with, $lt date-plus1}})))
     ;       (comment "year of www.sffaudio"
      ;       ((:get-url-monger my-db-obj) "2019" "www.sffaudio.com"))
        my-get-url   (fn get-url
                       [begins-with page-to-check]
                       (let [date-plus1 (date-plus-1 begins-with)]
                         (mong-coll/find-maps db-handle
                                              my-collection
                                              {$and [{:_id {$gte begins-with, $lt date-plus1}}
                                                     {:check-url page-to-check}]})))]

    (compact-hash my-delete-table my-purge-table my-get-all my-get-url my-put-item my-put-items)))
