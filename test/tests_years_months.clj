

(ns tests-years-months
  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as s]

            [clojure.spec.test.alpha :as t])
 ; (:require [java.util.Date  :refer :all])

  (:require [global-consts-vars  :refer :all])
  (:require [crash-sms.years-months :refer :all])
  (:require [java-time :refer [local-date?]])
  (:require [prepare-tests :refer :all])
  (:require [general-specs :refer :all])
)


(import java.util.Date)

(s/fdef trunc-yyyy-mm
  :args (s/cat :ymd-extra string?))

(s/fdef trunc-yyyy-mm-dd
  :args (s/cat :ymd-extra string?))

(s/fdef date-to-yyyy-mm
  :args (s/cat :ymd-date (inst?(Date.))))

(s/fdef date-to-yyyy-mm-dd
  :args (s/cat :ymd-date (inst?(Date.))))


(s/fdef month-name
  :args (s/alt :unary (s/cat  ::month-offset integer?)
               :binary (s/cat ::month-offset integer? :yyyy-mm string?)))

(s/fdef prev-month
  :args (s/alt :nillary (s/cat)
               :unary (s/cat :yyyy-mm string?)))

(s/fdef current-month
  :args (s/alt :nillary (s/cat)
               :unary (s/cat :yyyy-mm string?)))

(s/fdef yyyy-mm-to-ints
  :args (s/cat :yyyy-mm string?))

(s/fdef current-yyyy-mm
  :args (s/alt :nillary (s/cat)
               :unary (s/cat :yyyy-mm string?)))

(s/fdef prev-yyyy-mm
  :args (s/alt :nillary (s/cat)
               :unary (s/cat :yyyy-mm string?)))

(s/fdef instant-time-fn
  :args (s/cat))

(s/fdef adjusted-date
  :args (s/cat  :date-str string?))

(s/fdef date-with-now-time-fn
  :args (s/cat  :the-date string?))

(defn years-months-specs []
      (print-line "Speccing years-months")
      (t/instrument)
      (t/instrument 'adjusted-date)
      (t/instrument 'current-month)
      (t/instrument 'current-yyyy-mm-dd)
      (t/instrument 'current-yyyy-mm)
      (t/instrument 'date-to-yyyy-mm)
      (t/instrument 'instant-time-fn)
      (t/instrument 'month-name)
      (t/instrument 'prev-month)
      (t/instrument 'prev-yyyy-mm)
      (t/instrument 'trunc-yyyy-mm-dd)
      (t/instrument 'trunc-yyyy-mm)
      (t/instrument 'yyyy-mm-to-ints))

;   (clojure.test/test-vars [#'tests-years-months/test-semi-to-dash])
(deftest test-semi-to-dash
    (console-test "years-months test-semi-to-dash")
    (let [expected-yyyy-mm "2019-07-04-04-18-46.173Z"
          actual-yyyy-mm (adjusted-date "2019-07-04T04:18:46.173Z")]
      (is (= expected-yyyy-mm actual-yyyy-mm))))

;  (clojure.test/test-vars [#'tests-years-months/test-the-month-name])
(deftest test-the-month-name
    (console-test  "years-months test-the-month-name")
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

;  (clojure.test/test-vars [#'tests-years-months/test-month-name_0])
(deftest test-month-name_0
    (console-test "years-months test-month-name_0")
    (let [expected-month-name "December"
          actual-month-name (month-name 0 "2012-12")]
      (is (= expected-month-name actual-month-name))))

;  (clojure.test/test-vars [#'tests-years-months/test-month-name_1])
(deftest test-month-name_1
    (console-test "years-months test-month-name_1")
    (let [expected-month-name "January"
          actual-month-name (month-name 1 "2012-12")]
      (is (= expected-month-name actual-month-name))))

;  (clojure.test/test-vars [#'tests-years-months/test-month-name_2])
(deftest test-month-name_2
    (console-test "years-months test-month-name_2")
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

(defn all-tests []
  (reset! *T-REAL-DB-ASSERTIONS* true)
  (reset! *T-ASSERTIONS-VIA-REPL* false)
  (years-months-specs)
  (run-tests 'tests-years-months)
  (reset! *T-ASSERTIONS-VIA-REPL* true))


(defn fast-tests []
  (reset! *T-REAL-DB-ASSERTIONS* false)
  (reset! *T-ASSERTIONS-VIA-REPL* false)
  (years-months-specs)
  (run-tests 'tests-years-months)
  (reset! *T-ASSERTIONS-VIA-REPL* true))
