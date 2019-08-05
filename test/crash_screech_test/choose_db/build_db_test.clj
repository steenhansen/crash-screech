


(ns crash-screech-test.choose-db.build-db-test
   (:require [crash-screech.choose-db :refer :all])
   
    (:require [clojure.test :refer :all])
    
      (:require [sff-global-consts  :refer :all])
      
         (:require [test-prepare  :refer :all])
         
   ; (:require [clojure.spec.alpha :as spec-alpha]
   ;          [clojure.spec.gen.alpha :as spec-gen]
   ;          [clojure.spec.test.alpha :as spec-test])
   
  )


;(def ^:const GET-TWO-MONTHS-FILE "./test/crash_screech_test/html-render/get-two-months.edn")

(defn build-db-test [db-type]
  (let [[my-db-obj web-port cron-url sms-data] (build-db DB-TEST-NAME
                                                         []
                                                         db-type
                                                         TEST-CONFIG-FILE
                                                         IGNORE-ENV-VARS)]

    (is (function? (:delete-table my-db-obj)))
    (is (function? (:purge-table my-db-obj)))
    (is (function? (:get-all my-db-obj)))
    (is (function? (:get-url my-db-obj)))
    (is (function? (:put-item my-db-obj)))
    (is (function? (:put-items my-db-obj)))
    (is (is-string-number web-port))
     (is-url-dir cron-url)   ; "/url-for-cron-execution"
                                          (println sms-data)
           ))

(deftest test-build-db-dynoDb
  (testing "test-build-db :"
    (build-db-test :monger-db)))

(deftest test-build-db-mongoDb
  (testing "test-build-db :"
    (build-db-test     :amazonica-db)))



