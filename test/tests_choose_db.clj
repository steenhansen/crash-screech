
(ns tests-choose-db
  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as spec-alpha]
            [clojure.spec.gen.alpha :as spec-gen]
            [clojure.spec.test.alpha :as spec-test])
  (:require [global-consts-vars  :refer :all])
  (:require [crash-screech.choose-db :refer  :all])
  (:require [java-time :refer [local-date?]])
  (:require [crash-screech.config-args :refer [make-config]])
  (:require [prepare-tests :refer :all]))



(alias 's 'clojure.spec.alpha)
(alias 'c-db 'crash-screech.choose-db)

(s/fdef c-db/get-phone-nums
  :args (s/cat :phone-comma-string :phones-text?/test-specs))

(s/fdef c-db/get-db-conn
  :args (s/cat :table-name string?
               :pages-to-check vector?
               :db-type string?
               :config-file :config-map?/test-specs))

(s/fdef c-db/build-empty-month?
  :args (s/cat :get-all-fn function?))

(s/fdef c-db/build-today-error?
  :args (s/cat :get-all-fn function?))

(s/fdef c-db/build-db
  :args (s/cat :table-name string?
               :pages-to-check vector?
               :db-type string?
               :config-file string?
               :environment-utilize string?))

(defn choose-db-specs []
 (if RUN-SPEC-TESTS
    (do
  (println "Speccing choose-db")
      (spec-test/instrument)
      (spec-test/instrument 'get-db-conn)
      (spec-test/instrument 'build-today-error?)
      (spec-test/instrument 'get-phone-nums)
      (spec-test/instrument 'build-db))))

(deftest unit-get-phone-nums
  (testing "test-get-phone-nums : cccccccccccccccccccccc "
    (console-test "unit-get-phone-nums" "choose-db")
    (let [expected-phone-nums ["12345678901" "01234567890"]
          actual-phone-nums (get-phone-nums "12345678901,01234567890")]
      (is (= expected-phone-nums actual-phone-nums)))))

(defn unit-get-db-conn-type [db-type]
  (let [the-config (make-config db-type TEST-CONFIG-FILE IGNORE-ENV-VARS)
        my-db-funcs (get-db-conn T-DB-TEST-NAME [] db-type the-config)]
    (is (function? (:my-delete-table my-db-funcs)))
    (is (function? (:my-purge-table my-db-funcs)))
    (is (function? (:my-get-all my-db-funcs)))
    (is (function? (:my-get-url my-db-funcs)))
    (is (function? (:my-put-item my-db-funcs)))
    (is (function? (:my-put-items my-db-funcs)))))

(deftest unit-get-db-conn-mongoDb
  (testing "test-build-db :"
    (console-test "unit-get-db-conn-mongoDb" "choose-db")
    (unit-get-db-conn-type USE_MONGER_DB)))

(deftest unit-get-db-conn-dynoDb
  (testing "test-build-db :"
    (console-test "unit-get-db-conn-dynoDb" "choose-db")
    (if T-DO-DYNAMODB-TESTS
      (unit-get-db-conn-type    USE_AMAZONICA_DB))))

(defn  unit-build-empty-month-db [db-type]
  (let [[my-db-obj _ cron-url sms-data] (build-db T-DB-TEST-NAME
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
    (is (false?   ((:empty-month? my-db-obj) "2000-01")))))

(deftest unit-build-empty-month-mongoDb
  (testing "test-build-db :"
    (console-test "unit-build-empty-month-mongoDb" "choose-db")
    (unit-build-empty-month-db  USE_MONGER_DB)))

(deftest unit-build-empty-month-dynoDb
  (testing "test-build-db :"
    (console-test "unit-build-empty-month-dynoDb" "choose-db")
    (if T-DO-DYNAMODB-TESTS
      (unit-build-empty-month-db     USE_AMAZONICA_DB))))

(defn  build-today-error-db [db-type]
  (let [[my-db-obj _ cron-url sms-data] (build-db T-DB-TEST-NAME
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

(deftest unit-build-today-error-mongoDb
  (testing "test-build-db :"
 (console-test "unit-build-today-error-mongoDb" "choose-db")
    (build-today-error-db  USE_MONGER_DB)))

(deftest unit-build-today-error-dynoDb
  (testing "test-build-db :"
 (console-test "unit-build-today-error-dynoDb" "choose-db")
    (if T-DO-DYNAMODB-TESTS
      (build-today-error-db     USE_AMAZONICA_DB))))

(defn unit-build-db-type [db-type]
  (let [[my-db-obj web-port cron-url sms-data] (build-db T-DB-TEST-NAME
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
    (is-url-dir cron-url)))

(deftest unit-build-db-mongoDb
  (testing "test-build-db :"
    (console-test "unit-build-db-mongoDb" "choose-db")
    (unit-build-db-type USE_MONGER_DB)))

(deftest unit-build-db-dynoDb
  (testing "test-build-db :"
    (console-test "unit-build-db-mongoDb" "choose-db")
    (if T-DO-DYNAMODB-TESTS
      (unit-build-db-type     USE_AMAZONICA_DB))))

(deftest unit-build-db
  (testing "test-build-db :"
    (unit-build-db-mongoDb)
    (unit-build-db-dynoDb)))



(defn do-tests []
  (choose-db-specs)
  (run-tests 'tests-choose-db))
