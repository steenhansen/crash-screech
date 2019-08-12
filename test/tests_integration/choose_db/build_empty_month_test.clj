
(ns tests-integration.choose-db.build-empty-month-test
  (:require [crash-screech.choose-db :refer :all])
  (:require [prepare-tests :refer :all])
     (:require [global-consts  :refer :all])
  (:require [clojure.test :refer :all]))



(defn  test-build-empty-month-db [db-type]
  (let [[my-db-obj _ cron-url sms-data] (build-db DB-TEST-NAME
                                                  THE-CHECK-PAGES ; ["www.sffaudio.com"]
                                                  db-type
                                                  TEST-CONFIG-FILE
                                                  IGNORE-ENV-VARS)
        test-one {:the-url "www.sffaudio.com",
                  :the-date "2000-01-01-01:54:03.800Z",
                  :the-html "blah 5555",
                  :the-accurate true,
                  :the-time 1234}]

  ((:purge-table my-db-obj))
    (is (true?   ((:empty-month? my-db-obj) "2000-01")))

    ((:put-item my-db-obj) test-one)
    (is (false?   ((:empty-month? my-db-obj) "2000-01")))
))

(deftest test-build-empty-month-mongoDb
  (testing "test-build-db :"
    (test-build-empty-month-db  :monger-db)))

(deftest test-build-empty-month-dynoDb
  (testing "test-build-db :"
    (test-build-empty-month-db     :amazonica-db)))

(deftest  test-build-empty-month 
 (testing "test-build-db :"
    (test-build-empty-month-mongoDb)
   (test-build-empty-month-dynoDb)
))
