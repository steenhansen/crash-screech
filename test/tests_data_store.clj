
(ns tests-data-store
  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as t])
  (:require [global-consts-vars  :refer :all])
  (:require [crash-sms.data-store :refer  :all])
  (:require [java-time :refer [local-date?]])
  (:require [crash-sms.config-args :refer [make-config]])
  (:require [crash-sms.fake-db :refer :all])
  (:require [crash-sms.mongo-db :refer :all])
  (:require [crash-sms.dynamo-db :refer :all])
  (:require [prepare-tests :refer :all]))

(s/fdef get-phone-nums
  :args (s/cat :phone-comma-string :phones-text?/test-specs))

(s/fdef get-db-conn
  :args (s/cat :table-name string?
               :pages-to-check vector?
               :db-type string?
               :config-file :config-map?/test-specs))

(s/fdef build-empty-month?
  :args (s/cat :get-all-fn function?))

(s/fdef build-today-error?
  :args (s/cat :get-all-fn function?))

(s/fdef build-db
  :args (s/cat :table-name string?
               :pages-to-check vector?
               :db-type string?
               :config-file string?
               :environment-utilize string?))

(defn data-store-specs []
  (print-line "Speccing data-store")
  (t/instrument)
  (t/instrument 'get-db-conn)
  (t/instrument 'build-today-error?)
  (t/instrument 'get-phone-nums)
  (t/instrument 'build-db))

(deftest test-get-phone-nums
  (console-test "test-get-phone-nums" "data-store")
  (let [expected-phone-nums ["12345678901" "01234567890"]
        actual-phone-nums (get-phone-nums "12345678901,01234567890")]
    (is (= expected-phone-nums actual-phone-nums))))

(defn unit-get-db-conn-type [db-type]
  (let [the-config (make-config db-type TEST-CONFIG-FILE IGNORE-ENV-VARS)
        my-db-funcs (get-db-conn T-TEST-COLLECTION [] db-type the-config)]
    (is (function? (:my-db-alive? my-db-funcs)))
    (is (function? (:my-delete-table my-db-funcs)))
    (is (function? (:my-get-all my-db-funcs)))
    (is (function? (:my-get-url my-db-funcs)))
    (is (function? (:my-purge-table my-db-funcs)))
    (is (function? (:my-put-item my-db-funcs)))
    (is (function? (:my-put-items my-db-funcs)))
    (is (= (count my-db-funcs) NUM-DB-FUNCS))))

;   (clojure.test/test-vars [#'tests-data-store/t-get-db-conn-fakeDb])
(deftest test-get-db-conn-fakeDb
  (console-test "test-get-db-conn-fakeDb" "data-store")
  (unit-get-db-conn-type USE_FAKE_DB))

(deftest test-get-db-conn-mongoDb
  (console-test "test-get-db-conn-mongoDb" "data-store")
  (unit-get-db-conn-type USE_MONGER_DB))

(deftest test-get-db-conn-dynoDb
  (console-test "test-get-db-conn-dynoDb" "data-store")
  (unit-get-db-conn-type    USE_AMAZONICA_DB))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn unit-empty-month [db-type]
  (console-test "tests-data-store" "unit-empty-month" db-type)
  (let [the-check-pages (make-check-pages 0)
        [my-db-obj _ cron-url sms-data] (build-db T-TEST-COLLECTION
                                                  the-check-pages
                                                  db-type
                                                  TEST-CONFIG-FILE
                                                  IGNORE-ENV-VARS)
        test-one {:the-url WWW-SFFAUDIO-COM
                  :the-date "2000-01-01-01:54:03.800Z",
                  :the-html "blah 5555",
                  :the-accurate true,
                  :the-time 1234}]
    ((:purge-table my-db-obj))
    (is ((:empty-month? my-db-obj) "2000-01"))
    ((:put-item my-db-obj) test-one)
    (is (not ((:empty-month? my-db-obj) "2000-01")))))

;   (clojure.test/test-vars [#'tests-data-store/mock-db-empty-month])
(deftest mock-db-empty-month
  (console-test "tests-data-store" "mock-db-empty-month")
  (unit-empty-month  USE_FAKE_DB))

;   (clojure.test/test-vars [#'tests-data-store/real-db-empty-month])
(deftest real-db-empty-month
  (if (slow-db-tests-allowed)
    (do
      (console-test "tests-data-store" "real-db-empty-month")
      (unit-empty-month USE_MONGER_DB)
      (unit-empty-month USE_AMAZONICA_DB)
)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn unit-today-error [db-type]
(console-test "tests-data-store" "unit-today-error" db-type)
  (let [the-check-pages (make-check-pages 0)
        [my-db-obj _ cron-url sms-data] (build-db T-TEST-COLLECTION
                                                  the-check-pages
                                                  db-type
                                                  TEST-CONFIG-FILE
                                                  IGNORE-ENV-VARS)
        test-one {:the-url  WWW-SFFAUDIO-COM
                  :the-date "2000-01-01-01:54:03.800Z",
                  :the-html "blah 5555",
                  :the-accurate false,
                  :the-time 1234}]
    ((:purge-table my-db-obj))
    (is (not   ((:today-error? my-db-obj) "2000-01-01")))
    ((:put-item my-db-obj) test-one)
    (is ((:today-error? my-db-obj) "2000-01-01"))))

;   (clojure.test/test-vars [#'tests-data-store/mock-db-today-error])
(deftest mock-db-today-error
  (console-test "tests-data-store" "mock-db-today-error")
  (unit-today-error  USE_FAKE_DB))

;   (clojure.test/test-vars [#'tests-data-store/real-db-today-error])
(deftest real-db-today-error
  (if (slow-db-tests-allowed)
    (do
      (console-test "tests-data-store" "real-db-today-error")
      (unit-today-error USE_MONGER_DB)
      (unit-today-error USE_AMAZONICA_DB)
)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;


(defn unit-put-item [db-object]
  (let [purge-table (:purge-table db-object)
        put-item (:put-item db-object)
        get-all (:get-all db-object)
        type-db (:type-db db-object)
        test-rec  {:the-url WWW-SFFAUDIO-COM
                   :the-date "2011-06-19-01:54:03.800Z",
                   :the-html "blah 1111",
                   :the-accurate true,
                   :the-time 1234}
        test-rec2  {:the-url WWW-SFFAUDIO-COM
                    :the-date "2011-06-18-01:54:03.802Z",
                    :the-html "blah 1111",
                    :the-accurate true,
                    :the-time 1234}
        _ (purge-table)
        _ (put-item test-rec)
        _ (put-item test-rec2)
        all-db  (get-all "2011-06")]
    (console-test "tests-data-store" "unit-put-item" type-db)
    [(count all-db) 2]))

;   (clojure.test/test-vars [#'tests-data-store/mock-db-put-item])
(deftest mock-db-put-item
  (console-test  "test-web-server" "mock-db-put-item")
  (let [[fake-db] (build-db T-TEST-COLLECTION TEST-CHECK-PAGES USE_FAKE_DB TEST-CONFIG-FILE IGNORE-ENV-VARS)
        [text-diff-1 text-diff-2] (unit-put-item fake-db)]
    (is (= text-diff-1 text-diff-2))))

;   (clojure.test/test-vars [#'tests-data-store/real-db-put-item])
(deftest real-db-put-item
  (if (slow-db-tests-allowed)
    (do
      (console-test  "test-web-server" "real-db-put-item")
      (let [[mongo-db] (build-db T-TEST-COLLECTION TEST-CHECK-PAGES USE_MONGER_DB TEST-CONFIG-FILE IGNORE-ENV-VARS)
            [text-diff-1 text-diff-2] (unit-put-item mongo-db)]
        (is (= text-diff-1 text-diff-2)))
      (let [[dynamo-db] (build-db T-TEST-COLLECTION TEST-CHECK-PAGES USE_AMAZONICA_DB TEST-CONFIG-FILE IGNORE-ENV-VARS)
            [text-diff-1 text-diff-2] (unit-put-item dynamo-db)]
        (is (= text-diff-1 text-diff-2))))))
;;;;;;;;;;;;;;;;


;;;;;;;;;;;;;


(defn unit-functions [db-object]
  (is (function? (:delete-table db-object)))
  (is (function? (:db-alive? db-object)))
  (is (function? (:purge-table db-object)))
  (is (function? (:get-all db-object)))
  (is (function? (:get-url db-object)))
  (is (function? (:put-item db-object)))
  (is (function? (:put-items db-object)))
  (is (function? (:empty-month? db-object)))
  (is (function? (:today-error? db-object)))
  (console-test "t-functions"  (:type-db db-object))
  [(count db-object) NUM-DATA-STORE-PROPERTIES])

;   (clojure.test/test-vars [#'tests-data-store/all-t-functions])
(deftest test-obj-functions
  (console-test  "test-web-server all-t-functions")
  (let [[fake-db] (build-db T-TEST-COLLECTION TEST-CHECK-PAGES USE_FAKE_DB TEST-CONFIG-FILE IGNORE-ENV-VARS)
        [func-count-1 func-count-2] (unit-functions fake-db)]
    (is (= func-count-1 func-count-2)))
  (let [[mongo-db] (build-db T-TEST-COLLECTION TEST-CHECK-PAGES USE_MONGER_DB TEST-CONFIG-FILE IGNORE-ENV-VARS)
        [func-count-1 func-count-2] (unit-functions mongo-db)]
    (is (= func-count-1 func-count-2)))
  (let [[dynamo-db] (build-db T-TEST-COLLECTION TEST-CHECK-PAGES USE_AMAZONICA_DB TEST-CONFIG-FILE IGNORE-ENV-VARS)
        [func-count-1 func-count-2] (unit-functions dynamo-db)]
    (is (= func-count-1 func-count-2))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defn unit-get-items [db-object]
  (let [purge-table (:purge-table db-object)
        put-items (:put-items db-object)
        get-url (:get-url db-object)
        type-db (:type-db db-object)
        _ (purge-table)
        _ (put-items TEST-MANY-PAGES)
        sff-2016 (get-url "2016" WWW-SFFAUDIO-COM)
        sff-2016-07 (get-url "2016-07" WWW-SFFAUDIO-COM)
        sff-2016-07-08 (get-url "2016-07-08" WWW-SFFAUDIO-COM) ]
    (console-test "t-get-items" type-db)
    (is (= (count sff-2016) 2))
    (is (= (count sff-2016-07) 2))
    (is (= (count sff-2016-07-08) 1))))

;   (clojure.test/test-vars [#'tests-data-store/fast-t-get-items])
(deftest mock-t-get-items
  (console-test  "test-web-server fast-t-put-item")
  (let [[fake-db] (build-db T-TEST-COLLECTION TEST-CHECK-PAGES USE_FAKE_DB TEST-CONFIG-FILE IGNORE-ENV-VARS)]
    (unit-get-items fake-db)))

;   (clojure.test/test-vars [#'tests-data-store/real-t-get-items])
(deftest real-t-get-items
  (if (slow-db-tests-allowed)
    (do
      (console-test  "web-server real-t-put-item")
      (let [[mongo-db] (build-db T-TEST-COLLECTION TEST-CHECK-PAGES USE_MONGER_DB TEST-CONFIG-FILE IGNORE-ENV-VARS)]
        (unit-get-items mongo-db))
      (let [[dynamo-db] (build-db T-TEST-COLLECTION TEST-CHECK-PAGES USE_AMAZONICA_DB TEST-CONFIG-FILE IGNORE-ENV-VARS)]
        (unit-get-items dynamo-db)))))

(defn all-tests []
  (reset! *T-REAL-DB-ASSERTIONS* true)
  (reset! *T-ASSERTIONS-VIA-REPL* false)
  (data-store-specs)
  (run-tests 'tests-data-store)
  (reset! *T-ASSERTIONS-VIA-REPL* true))

(defn mock-tests []
  (reset! *T-REAL-DB-ASSERTIONS* false)
  (reset! *T-ASSERTIONS-VIA-REPL* false)
  (data-store-specs)
  (run-tests 'tests-data-store)
  (reset! *T-ASSERTIONS-VIA-REPL* true))
