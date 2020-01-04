
(ns tests-choose-db
  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as spec-alpha]
            [clojure.spec.gen.alpha :as spec-gen]
            [clojure.spec.test.alpha :as spec-test])
  (:require [global-consts-vars  :refer :all])
  (:require [crash-sms.choose-db :refer  :all])
  (:require [java-time :refer [local-date?]])
  (:require [crash-sms.config-args :refer [make-config]])
  (:require [crash-sms.fake-db :refer :all])
  (:require [crash-sms.mongo-db :refer :all])
  (:require [crash-sms.dynamo-db :refer :all])
  (:require [prepare-tests :refer :all]))

(alias 's 'clojure.spec.alpha)
(alias 'c-db 'crash-sms.choose-db)

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
  (print-line "Speccing choose-db")
  (spec-test/instrument)
  (spec-test/instrument 'get-db-conn)
  (spec-test/instrument 'build-today-error?)
  (spec-test/instrument 'get-phone-nums)
  (spec-test/instrument 'build-db))

(deftest unit-get-phone-nums
  (console-test "unit-get-phone-nums" "choose-db")
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

;   (clojure.test/test-vars [#'tests-choose-db/t-get-db-conn-fakeDb])
(deftest t-get-db-conn-fakeDb
  (console-test "unit-get-db-conn-mongoDb" "choose-db")
  (unit-get-db-conn-type USE_FAKE_DB))

(deftest unit-get-db-conn-mongoDb
  (console-test "unit-get-db-conn-mongoDb" "choose-db")
  (unit-get-db-conn-type USE_MONGER_DB))

(deftest unit-get-db-conn-dynoDb
  (console-test "unit-get-db-conn-dynoDb" "choose-db")
  (unit-get-db-conn-type    USE_AMAZONICA_DB))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn  unit-build-empty-month-db [db-type]
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
    (is (not ((:empty-month? my-db-obj) "2000-01")))))   ;;; return for testing

;   (clojure.test/test-vars [#'tests-choose-db/t-empty-month-fakeDb])
(deftest t-empty-month-fakeDb
  (console-test "t-empty-month-fakeDb" "choose-db")
  (unit-build-empty-month-db     USE_FAKE_DB))

(deftest unit-build-empty-month-mongoDb
  (console-test "unit-build-empty-month-mongoDb" "choose-db")
  (unit-build-empty-month-db     USE_MONGER_DB))

;   (clojure.test/test-vars [#'tests-choose-db/unit-build-empty-month-dynoDb])
(deftest unit-build-empty-month-dynoDb
  (console-test "unit-build-empty-month-dynoDb" "choose-db")
  (unit-build-empty-month-db     USE_AMAZONICA_DB))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn  build-today-error-db [db-type]
  (let [the-check-pages (make-check-pages 0)
        [my-db-obj _ cron-url sms-data] (build-db T-TEST-COLLECTION
                                                  the-check-pages
                                                  db-type
                                                  TEST-CONFIG-FILE
                                                  IGNORE-ENV-VARS)
        test-one {:the-url  WWW-SFFAUDIO-COM  ;"www.sffaudio.com",
                  :the-date "2000-01-01-01:54:03.800Z",
                  :the-html "blah 5555",
                  :the-accurate false,
                  :the-time 1234}]
    ((:purge-table my-db-obj))
    (is (not   ((:today-error? my-db-obj) "2000-01-01")))
    ((:put-item my-db-obj) test-one)
    (is ((:today-error? my-db-obj) "2000-01-01"))))

;   (clojure.test/test-vars [#'tests-choose-db/t-today-error-fakeDb])
(deftest t-today-error-fakeDb
  (console-test "FFFunit-build-today-error-mongoDb" "FFFFFFFFFFFFchoose-db")
  (build-today-error-db  USE_FAKE_DB))

(deftest unit-build-today-error-mongoDb
  (console-test "unit-build-today-error-mongoDb" "choose-db")
  (build-today-error-db  USE_MONGER_DB))

(deftest unit-build-today-error-dynoDb
  (console-test "unit-build-today-error-dynoDb" "choose-db")
  (build-today-error-db     USE_AMAZONICA_DB))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn unit-build-db-type [db-type]
  (let [[my-db-obj web-port cron-url sms-data] (build-db T-TEST-COLLECTION
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


;   (clojure.test/test-vars [#'tests-choose-db/t-build-db-fakeDb])


(deftest t-build-db-fakeDb
  (console-test "unit-build-db-mongoDb" "choose-db")
  (unit-build-db-type USE_FAKE_DB))

(deftest unit-build-db-mongoDb
  (console-test "unit-build-db-mongoDb" "choose-db")
  (unit-build-db-type USE_MONGER_DB))

(deftest unit-build-db-dynoDb
  (console-test "unit-build-db-mongoDb" "choose-db")
  (unit-build-db-type     USE_AMAZONICA_DB))

(deftest unit-build-db
  (unit-build-db-mongoDb)
  (unit-build-db-dynoDb))








;;;;;;;;;;;;;


(defn t-put-item [db-object]
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
    (console-test "t-put-item" type-db)
    [(count all-db) 2]))

;   (clojure.test/test-vars [#'tests-choose-db/fast-t-put-item])
(deftest fast-t-put-item
  (console-test  "test-web-server fast-t-put-item")
  (let [[fake-db] (build-db T-TEST-COLLECTION TEST-CHECK-PAGES USE_FAKE_DB TEST-CONFIG-FILE IGNORE-ENV-VARS)
        [text-diff-1 text-diff-2] (t-put-item fake-db)]
    (is (= text-diff-1 text-diff-2))))

;   (clojure.test/test-vars [#'tests-choose-db/real-t-put-item])
(deftest real-t-put-item
  (if (execute-tests)
    (do
      (console-test  "web-server real-t-put-item")
      (let [[mongo-db] (build-db T-TEST-COLLECTION TEST-CHECK-PAGES USE_MONGER_DB TEST-CONFIG-FILE IGNORE-ENV-VARS)
            [text-diff-1 text-diff-2] (t-put-item mongo-db)]
        (is (= text-diff-1 text-diff-2)))
      (let [[dynamo-db] (build-db T-TEST-COLLECTION TEST-CHECK-PAGES USE_AMAZONICA_DB TEST-CONFIG-FILE IGNORE-ENV-VARS)
            [text-diff-1 text-diff-2] (t-put-item dynamo-db)]
        (is (= text-diff-1 text-diff-2))))))
;;;;;;;;;;;;;;;;


;;;;;;;;;;;;;


(defn t-functions [db-object]

  (is (function? (:delete-table db-object)))
  (is (function? (:db-alive? db-object)))
  (is (function? (:purge-table db-object)))
  (is (function? (:get-all db-object)))
  (is (function? (:get-url db-object)))
  (is (function? (:put-item db-object)))
  (is (function? (:put-items db-object)))

  (is (function? (:empty-month? db-object)))
  (is (function? (:today-error? db-object)))
   ;  type-db (:type-db db-object)
    ;    table-name (:table-name db-object)
  (console-test "t-functions"  (:type-db db-object))
  [(count db-object) 11])

;   (clojure.test/test-vars [#'tests-choose-db/all-t-functions])
(deftest all-t-functions
  (console-test  "test-web-server all-t-functions")
  (let [[fake-db] (build-db T-TEST-COLLECTION TEST-CHECK-PAGES USE_FAKE_DB TEST-CONFIG-FILE IGNORE-ENV-VARS)
        [func-count-1 func-count-2] (t-functions fake-db)]
    (is (= func-count-1 func-count-2)))

  (let [[mongo-db] (build-db T-TEST-COLLECTION TEST-CHECK-PAGES USE_MONGER_DB TEST-CONFIG-FILE IGNORE-ENV-VARS)
        [func-count-1 func-count-2] (t-functions mongo-db)]
    (is (= func-count-1 func-count-2)))
  (let [[dynamo-db] (build-db T-TEST-COLLECTION TEST-CHECK-PAGES USE_AMAZONICA_DB TEST-CONFIG-FILE IGNORE-ENV-VARS)
        [func-count-1 func-count-2] (t-functions dynamo-db)]
    (is (= func-count-1 func-count-2))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defn t-get-items [db-object]
  (let [purge-table (:purge-table db-object)
        put-items (:put-items db-object)
        get-url (:get-url db-object)
        type-db (:type-db db-object)
        _ (purge-table)
        _ (put-items TEST-MANY-PAGES)
        sff-2016 (get-url "2016" "www.sffaudio.com")
        sff-2016-07 (get-url "2016-07" "www.sffaudio.com")
        sff-2016-07-08 (get-url "2016-07-08" "www.sffaudio.com")]
    (console-test "t-get-items" type-db)
    (is (= (count sff-2016) 2))
    (is (= (count sff-2016-07) 2))
    (is (= (count sff-2016-07-08) 1))))

;   (clojure.test/test-vars [#'tests-choose-db/fast-t-get-items])
(deftest fast-t-get-items
  (console-test  "test-web-server fast-t-put-item")
  (let [[fake-db] (build-db T-TEST-COLLECTION TEST-CHECK-PAGES USE_FAKE_DB TEST-CONFIG-FILE IGNORE-ENV-VARS)]
    (t-get-items fake-db)))

;   (clojure.test/test-vars [#'tests-choose-db/real-t-get-items])
(deftest real-t-get-items
  (if (execute-tests)
    (do
      (console-test  "web-server real-t-put-item")
      (let [[mongo-db] (build-db T-TEST-COLLECTION TEST-CHECK-PAGES USE_MONGER_DB TEST-CONFIG-FILE IGNORE-ENV-VARS)]
        (t-get-items mongo-db))
      (let [[dynamo-db] (build-db T-TEST-COLLECTION TEST-CHECK-PAGES USE_AMAZONICA_DB TEST-CONFIG-FILE IGNORE-ENV-VARS)]
        (t-get-items dynamo-db)))))

(defn do-tests []
  (reset! *run-all-tests* true)
  (reset! *testing-namespace* "fast-all-tests-running")
  (choose-db-specs)
  (run-tests 'tests-choose-db)
  (reset! *testing-namespace* "no-tests-running"))

(defn fast-tests []
  (reset! *run-all-tests* false)
  (reset! *testing-namespace* "fast-all-tests-running")
  (choose-db-specs)
  (run-tests 'tests-choose-db)
  (reset! *testing-namespace* "no-tests-running"))
