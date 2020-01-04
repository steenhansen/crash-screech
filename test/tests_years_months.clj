

(ns tests-years-months
  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as spec-alpha]
            [clojure.spec.gen.alpha :as spec-gen]
            [clojure.spec.test.alpha :as spec-test])
  (:require [global-consts-vars  :refer :all])
  (:require [crash-sms.years-months :refer :all])
  (:require [java-time :refer [local-date?]])
  (:require [prepare-tests :refer :all])
  (:require [general-specs :refer :all])
)

(defn years-months-specs []
      (print-line "Speccing years-months")
      (spec-test/instrument)
      (spec-test/instrument 'adjusted-date)
      (spec-test/instrument 'current-month)
      (spec-test/instrument 'current-yyyy-mm-dd)
      (spec-test/instrument 'current-yyyy-mm)
      (spec-test/instrument 'date-to-yyyy-mm)
      (spec-test/instrument 'instant-time-fn)
      (spec-test/instrument 'month-name)
      (spec-test/instrument 'prev-month)
      (spec-test/instrument 'prev-yyyy-mm)
      (spec-test/instrument 'trunc-yyyy-mm-dd)
      (spec-test/instrument 'trunc-yyyy-mm)
      (spec-test/instrument 'yyyy-mm-to-ints))

;   (clojure.test/test-vars [#'tests-years-months/semi-to-dash])
(deftest semi-to-dash
    (console-test "years-months semi-to-dash")
    (let [expected-yyyy-mm "2019-07-04-04-18-46.173Z"
          actual-yyyy-mm (adjusted-date "2019-07-04T04:18:46.173Z")]
      (is (= expected-yyyy-mm actual-yyyy-mm))))

;  (clojure.test/test-vars [#'tests-years-months/the-month-name])
(deftest the-month-name
    (console-test  "years-months current-month")
    (let [expected-month-name "January"
          actual-month-name (current-month "2012-01")]
      (is (= expected-month-name actual-month-name))))

;  (clojure.test/test-vars [#'tests-years-months/test-yyyy-mm-dd])
(deftest test-yyyy-mm-dd
    (console-test  "years-months test-yyyy-mm-dd")
    (let [expected-yyyy-mm-dd "1999-12-13"
          actual-yyyy-mm-dd (current-yyyy-mm-dd "1999-12-13-1234")]
      (is (= expected-yyyy-mm-dd actual-yyyy-mm-dd))))

;  (clojure.test/test-vars [#'tests-years-months/test-yyyy-mm-0])
(deftest test-yyyy-mm-0
    (console-test "years-months test-yyyy-mm-0")
    (let [expected-yyyy-mm "1999-12"
          actual-yyyy-mm (current-yyyy-mm "1999-12")]
      (is (= expected-yyyy-mm actual-yyyy-mm))))

;  (clojure.test/test-vars [#'tests-years-months/test-yyyy-mm-days])
(deftest test-yyyy-mm-days
    (console-test "years-months test-yyyy-mm-days")
    (let [expected-yyyy-mm "1999-12"
          actual-yyyy-mm (current-yyyy-mm "1999-12-14")]
      (is (= expected-yyyy-mm actual-yyyy-mm))))

;  (clojure.test/test-vars [#'tests-years-months/date-to-yyyy-mm])
(deftest test-date-to-yyyy-mm
    (console-test "years-months test-date-to-yyyy-mm")
    (let [expected-yyyy-mm "1999-12"
          actual-yyyy-mm (date-to-yyyy-mm (java-time.local/local-date 1999 12 31))]
      (is (= expected-yyyy-mm actual-yyyy-mm))))

;(def ^:const T-TIME-STAMP #"^=(20(0|1|2)[\d])-(0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-9]|3[0-1])T\d\d:\d\d:\d\d\.\d\d\dZ$")

;  (clojure.test/test-vars [#'tests-years-months/])
(deftest test-instant-time-fn
    (console-test  "years-months instant-time-fn")
    (let [actual-instant-time-fn (instant-time-fn)]
      (is (re-matches T-TIME-STAMP actual-instant-time-fn))))

;  (clojure.test/test-vars [#'tests-years-months/month-name_0])
(deftest month-name_0
    (console-test "years-months month-name_0")
    (let [expected-month-name "December"
          actual-month-name (month-name 0 "2012-12")]
      (is (= expected-month-name actual-month-name))))

;  (clojure.test/test-vars [#'tests-years-months/month-name_1])
(deftest month-name_1
    (console-test "years-months month-name_1")
    (let [expected-month-name "January"
          actual-month-name (month-name 1 "2012-12")]
      (is (= expected-month-name actual-month-name))))

;  (clojure.test/test-vars [#'tests-years-months/month-name_2])
(deftest month-name_2
    (console-test "years-months month-name_2")
    (let [expected-month-name "December"
          actual-month-name (month-name -1 "2012-01")]
      (is (= expected-month-name actual-month-name))))

;  (clojure.test/test-vars [#'tests-years-months/test-prev-month])
(deftest test-prev-month
    (console-test "years-months prev-month")
    (let [expected-month-name "December"
          actual-month-name (prev-month "2012-01")]
      (is (= expected-month-name actual-month-name))))

;  (clojure.test/test-vars [#'tests-years-months/test-prev-yyyy-mm])
(deftest test-prev-yyyy-mm
    (console-test  "years-months test-prev-yyyy-mm")
    (let [expected-yyyy-mm "1999-12"
          actual-yyyy-mm (prev-yyyy-mm "2000-01")]
      (is (= expected-yyyy-mm actual-yyyy-mm))))

;  (clojure.test/test-vars [#'tests-years-months/test-trunc-yyyy-mm-dd])
(deftest test-trunc-yyyy-mm-dd
    (console-test "years-months test-trunc-yyyy-mm-dd")
    (let [expected-yyyy-mm "2019-07-04-04-18-46.173Z"
          actual-yyyy-mm (adjusted-date "2019-07-04T04:18:46.173Z")]
      (is (= expected-yyyy-mm actual-yyyy-mm))))

;  (clojure.test/test-vars [#'tests-years-months/test-trunc-yyyy-mm])
(deftest test-trunc-yyyy-mm
    (console-test  "years-months test-trunc-yyyy-mm")
    (let [expected-yyyy-mm "2019-07-04-04-18-46.173Z"
          actual-yyyy-mm (adjusted-date "2019-07-04T04:18:46.173Z")]
      (is (= expected-yyyy-mm actual-yyyy-mm))))

;  (clojure.test/test-vars [#'tests-years-months/test-yyyy-mm-to-ints])
(deftest test-yyyy-mm-to-ints
    (console-test  "years-months test-yyyy-mm-to-ints")
    (let [expected-month-name [2234 12]
          actual-month-name (yyyy-mm-to-ints "2234-12")]
      (is (= expected-month-name actual-month-name))))

(defn do-tests []
  (reset! *run-all-tests* true)
  (reset! *testing-namespace* "fast-all-tests-running")
  (years-months-specs)
  (years-months-specs)
  (run-tests 'tests-years-months)
  (reset! *testing-namespace* "no-tests-running"))


(defn fast-tests []
  (reset! *run-all-tests* false)
  (reset! *testing-namespace* "fast-all-tests-running")
  (years-months-specs)
  (run-tests 'tests-years-months)
  (reset! *testing-namespace* "no-tests-running"))
