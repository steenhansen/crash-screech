
(ns tests-mongo-db
  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as t])
  (:require [global-consts-vars  :refer :all])
  (:require [crash-sms.config-args :refer [make-config compact-hash]])
  (:require [crash-sms.mongo-db :refer :all])
  (:require [java-time :refer [local-date?]])
  (:require [prepare-tests :refer :all]))

(s/check-asserts true)

(s/fdef next-date-time
  :args (s/cat :yyyy-mm-d   :yyyy?-mm?-dd?-hh?-mm?-ss/test-specs))

(s/fdef mongolabs-build
  :args (s/cat ::mongolabs-config :mongo-config?/test-specs
               ::table-name string?
               ::pages-to-check vector?))

(defn mongo-db-specs []
(print-line "Speccing mongo-db")
      (t/instrument)
      (t/instrument 'next-date-time)
      (t/instrument 'mongolabs-build))

(deftest test-next-date-time-a
      (console-test "test-next-date-time" "mongo-db")
    (let [future-date (next-date-time "2000-01")]
      (is (= future-date "2000-02"))))

(deftest test-next-date-time-b
      (console-test "test-next-date-time" "mongo-db")
    (let [future-date (next-date-time "2001-02-03")]
      (is (= future-date "2001-02-04"))))

(deftest test-next-date-time-c
      (console-test "test-next-date-time" "mongo-db")
    (let [future-date (next-date-time "2002-12")]
      (is (= future-date "2002-13"))))

(deftest test-next-date-time-d
      (console-test "test-next-date-time" "mongo-db")
    (let [future-date (next-date-time "2003-12-01")]
      (is (= future-date "2003-12-02"))))

(deftest test-next-date-time-e
      (console-test "test-next-date-time" "mongo-db")
    (let [future-date (next-date-time "2000")]
      (is (= future-date "2001"))))

(deftest testt-next-date-time-f
      (console-test "test-next-date-time" "mongo-db")
    (let [future-date (next-date-time "2004-04-04-04")]
      (is (= future-date "2004-04-04-05"))))



(defn all-tests []
  (reset! *T-REAL-DB-ASSERTIONS* true)
 (reset! *T-ASSERTIONS-VIA-REPL* false)
 (mongo-db-specs)
  (run-tests 'tests-mongo-db)
  (reset! *T-ASSERTIONS-VIA-REPL* true))




(defn fast-tests []
  (reset! *T-REAL-DB-ASSERTIONS* false)
 (reset! *T-ASSERTIONS-VIA-REPL* false)
 (mongo-db-specs)
  (run-tests 'tests-mongo-db)
  (reset! *T-ASSERTIONS-VIA-REPL* true))
