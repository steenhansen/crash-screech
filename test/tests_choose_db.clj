; (-do-tests)



(ns tests-choose-db
  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as spec-alpha]
            [clojure.spec.gen.alpha :as spec-gen]
            [clojure.spec.test.alpha :as spec-test])

  (:require [global-consts  :refer :all])
  (:require [global-vars  :refer :all])

  (:require [crash-screech.choose-db :refer  :all])

  (:require [java-time :refer [local-date?]])

 (:require [crash-screech.config-args :refer [make-config]])

  (:require [prepare-tests :refer :all])
  (:require [spec-calls :refer :all]))

(defn uni-build-db-type [db-type]
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
                                          ;(println "sms-data here" sms-data)
    ))

(deftest uni-build-db-mongoDb
  (testing "test-build-db :"
    (console-test "uni-derive-data" "choose-db")
    (uni-build-db-type :monger-db)))

(deftest uni-build-db-dynoDb
  (testing "test-build-db :"
    (if DO-DYNAMODB-TESTS
      (uni-build-db-type     :amazonica-db))))

(deftest uni-build-db
  (testing "test-build-db :"
    (uni-build-db-mongoDb)
    (uni-build-db-dynoDb)))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn uni-get-db-conn-type [db-type]
  (let [the-config (make-config db-type TEST-CONFIG-FILE IGNORE-ENV-VARS)
        my-db-funcs (get-db-conn DB-TEST-NAME [] db-type the-config)]

    (is (function? (:my-delete-table my-db-funcs)))
    (is (function? (:my-purge-table my-db-funcs)))
    (is (function? (:my-get-all my-db-funcs)))
    (is (function? (:my-get-url my-db-funcs)))
    (is (function? (:my-put-item my-db-funcs)))
    (is (function? (:my-put-items my-db-funcs)))))

(deftest uni-get-db-conn-dynoDb
  (testing "test-build-db :"
    (uni-get-db-conn-type :monger-db)))

(deftest uni-get-db-conn-mongoDb
  (if DO-DYNAMODB-TESTS
    (testing "test-build-db :"
      ( uni-get-db-conn-type    :amazonica-db))))

(deftest uni-get-db-conn
  (testing "test-build-db :"
    (uni-get-db-conn-dynoDb)
    (uni-get-db-conn-mongoDb)))

;;;;;;;;;;;;;;;;;;;;;;;;
 
(deftest uni-get-phone-nums
  (testing "test-get-phone-nums : cccccccccccccccccccccc "
  

      (let [expected-phone-nums ["12345678901" "01234567890"]
            actual-phone-nums (get-phone-nums "12345678901,01234567890")]
        (is (= expected-phone-nums actual-phone-nums)))
      
      
    
    
    ))
