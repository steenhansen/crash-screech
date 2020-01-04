
(ns tests-mongo-db
  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as spec-alpha]
            [clojure.spec.gen.alpha :as spec-gen]
            [clojure.spec.test.alpha :as spec-test])

  (:require [global-consts-vars  :refer :all])

  (:require [crash-sms.config-args :refer [make-config compact-hash]])
  (:require [crash-sms.mongo-db :refer :all])

  (:require [java-time :refer [local-date?]])

  (:require [prepare-tests :refer :all]))

(alias 's 'clojure.spec.alpha)
(alias 'm-d 'crash-sms.mongo-db)




(s/fdef m-d/next-date-time
  :args (s/cat :yyyy-mm-d   :yyyy?-mm?-dd?-hh?-mm?-ss/test-specs))

(s/fdef m-d/mongolabs-build
  :args (s/cat ::mongolabs-config :mongo-config?/test-specs
               ::table-name string?
               ::pages-to-check vector?))



(defn mongo-db-specs []
(print-line "Speccing mongo-db")
      (spec-test/instrument)
      (spec-test/instrument 'next-date-time)
      (spec-test/instrument 'mongolabs-build))









(deftest unit-next-date-time-a
    (let [future-date (next-date-time "2000-01")]
      (console-test "unit-next-date-time" "mongo-db")
      (is (= future-date "2000-02"))))

(deftest unit-next-date-time-b
    (let [future-date (next-date-time "2001-02-03")]
      (console-test "unit-next-date-time" "mongo-db")
      (is (= future-date "2001-02-04"))))

(deftest unit-next-date-time-c
    (let [future-date (next-date-time "2002-12")]
      (console-test "unit-next-date-time" "mongo-db")
      (is (= future-date "2002-13"))))

(deftest unit-next-date-time-d
    (let [future-date (next-date-time "2003-12-01")]
      (console-test "unit-next-date-time" "mongo-db")
      (is (= future-date "2003-12-02"))))

(deftest unit-next-date-time-e
    (let [future-date (next-date-time "2000")]
      (console-test "unit-next-date-time" "mongo-db")
      (is (= future-date "2001"))))

(deftest unit-next-date-time-f
    (let [future-date (next-date-time "2004-04-04-04")]
      (console-test "unit-next-date-time" "mongo-db")
      (is (= future-date "2004-04-04-05"))))



(defn do-tests []
  (reset! *run-all-tests* true)
 (reset! *testing-namespace* "fast-all-tests-running")
 (mongo-db-specs)
  (run-tests 'tests-mongo-db)
  (reset! *testing-namespace* "no-tests-running"))




(defn fast-tests []
  (reset! *run-all-tests* false)
 (reset! *testing-namespace* "fast-all-tests-running")
 (mongo-db-specs)
  (run-tests 'tests-mongo-db)
  (reset! *testing-namespace* "no-tests-running"))
