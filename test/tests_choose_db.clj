
(ns tests-choose-db
  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as spec-alpha]
            [clojure.spec.gen.alpha :as spec-gen]
            [clojure.spec.test.alpha :as spec-test])
  (:require [global-consts-vars  :refer :all])
  (:require [crash-sms.choose-db :refer  :all])
  (:require [java-time :refer [local-date?]])
  (:require [crash-sms.config-args :refer [make-config]])
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
  (println "Speccing choose-db")
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
    (is (function? (:my-delete-table my-db-funcs)))
    (is (function? (:my-purge-table my-db-funcs)))
    (is (function? (:my-get-all my-db-funcs)))
    (is (function? (:my-get-url my-db-funcs)))
    (is (function? (:my-put-item my-db-funcs)))
    (is (function? (:my-put-items my-db-funcs)))))

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
  (let [ the-check-pages (make-check-pages 0)
              [my-db-obj _ cron-url sms-data] (build-db T-TEST-COLLECTION
                                                  the-check-pages
                                                  db-type
                                                  TEST-CONFIG-FILE
                                                  IGNORE-ENV-VARS)
        test-one {:the-url WWW-SFFAUDIO-COM-SLASH
                  :the-date "2000-01-01-01:54:03.800Z",
                  :the-html "blah 5555",
                  :the-accurate true,
                  :the-time 1234}]
  ((:purge-table my-db-obj))
    (is ((:empty-month? my-db-obj) "2000-01"))
   ((:put-item my-db-obj) test-one)
    (is (not ((:empty-month? my-db-obj) "2000-01")))))

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
        test-one {:the-url  WWW-SFFAUDIO-COM-SLASH  ;"www.sffaudio.com",
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



(defn do-tests []
  (choose-db-specs)
  (run-tests 'tests-choose-db))
