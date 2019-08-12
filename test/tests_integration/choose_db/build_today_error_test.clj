

(ns tests-integration.choose-db.build-today-error-test
  (:require [crash-screech.choose-db :refer :all])
  (:require [prepare-tests :refer :all])
  (:require [global-consts  :refer :all])
  (:require [clojure.test :refer :all]))

(defn  test-build-today-error-db [db-type]
  (let [[my-db-obj _ cron-url sms-data] (build-db DB-TEST-NAME
                                                  THE-CHECK-PAGES ; ["www.sffaudio.com"][]
                                                  db-type
                                                  TEST-CONFIG-FILE
                                                  IGNORE-ENV-VARS)
        test-one {:the-url "www.sffaudio.com",
                  :the-date "2000-01-01-01:54:03.800Z",
                  :the-html "blah 5555",
                  :the-accurate false,
                  :the-time 1234}]
    ((:purge-table my-db-obj))
    (is (false?   ((:today-error? my-db-obj) "2000-01-01")))
    ((:put-item my-db-obj) test-one)

    (is (true?   ((:today-error? my-db-obj) "2000-01-01")))))

(deftest test-build-today-error-mongoDb
  (testing "test-build-db :"
    (test-build-today-error-db  :monger-db)))

(deftest test-build-today-error-dynoDb
  (testing "test-build-db :"
    (test-build-today-error-db     :amazonica-db)))

(deftest test-build-today-error
  (testing "test-build-db :"
    (test-build-today-error-mongoDb)
    (test-build-today-error-dynoDb)))

