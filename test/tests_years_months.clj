

(ns tests-years-months
  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as spec-alpha]
            [clojure.spec.gen.alpha :as spec-gen]
            [clojure.spec.test.alpha :as spec-test])

  (:require [java-time.local :as j-time])

  (:require [global-consts  :refer :all])
  (:require [global-vars  :refer :all])

  (:require [crash-screech.years-months :refer :all])

  (:require [java-time :refer [local-date?]])

  (:require [prepare-tests :refer :all])
  (:require [spec-calls :refer :all]))


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
(spec-test/instrument 'yyyy-mm-to-ints)


(deftest uni-adjusted-date
  (testing "test-adjusted-date : java.date to YYYY-MM like 1999-12 if it is 2000-1-1 "
    (console-test "uni-adjusted-date" "years-months")
    (let [expected-yyyy-mm "2019-07-04-04-18-46.173Z"
          actual-yyyy-mm (adjusted-date "2019-07-04T04:18:46.173Z")]
      (is (= expected-yyyy-mm actual-yyyy-mm)))))

(deftest uni-current-month
  (testing "test-current-month :  "
    (console-test "uni-current-month" "years-months")
    (let [expected-month-name "January"
          actual-month-name (current-month "2012-01")]

      (is (= expected-month-name actual-month-name)))))

(deftest uni-current-yyyy-mm-dd
  (testing "current-yyyy-mm-dd : java.date to YYYY-MM like 2000-12 "
    (console-test "uni-current-yyyy-mm-dd" "years-months")
    (let [expected-yyyy-mm-dd "1999-12-13"
          actual-yyyy-mm-dd (current-yyyy-mm-dd "1999-12-13-1234")]

      (is (= expected-yyyy-mm-dd actual-yyyy-mm-dd)))))

(deftest uni-current-yyyy-mm-0
  (testing "test-current-yyyy-mm : java.date to YYYY-MM like 2000-12 "
    (console-test "uni-current-yyyy-mm-0" "years-months")
    (let [expected-yyyy-mm "1999-12"
          actual-yyyy-mm (current-yyyy-mm "1999-12")]

      (is (= expected-yyyy-mm actual-yyyy-mm)))))

(deftest uni-current-yyyy-mm-days
  (testing "test-current-yyyy-mm : java.date to YYYY-MM like 2000-12 "
    (console-test "uni-current-yyyy-mm-days" "years-months")
    (let [expected-yyyy-mm "1999-12"
          actual-yyyy-mm (current-yyyy-mm "1999-12-14")]

      (is (= expected-yyyy-mm actual-yyyy-mm)))))

(deftest uni-date-to-yyyy-mm
  (testing "test-date-to-yyyy-mm : java.date to YYYY-MM like 2000-12 "
    (console-test "uni-date-to-yyyy-mm" "years-months")
    (let [expected-yyyy-mm "1999-12"
          actual-yyyy-mm (date-to-yyyy-mm (j-time/local-date 1999 12 31))]

      (is (= expected-yyyy-mm actual-yyyy-mm)))))

;(def ^:const T-TIME-STAMP #"^([\d]{4})-(0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-9]|3[0-1])T\d\d:\d\d:\d\d\.\d\d\dZ$")

(deftest uni-instant-time-fn
  (testing "test-adjusted-date : java.date to YYYY-MM like 1999-12 if it is 2000-1-1 "
  (console-test "uni-instant-time-fn" "years-months")
    (let [actual-instant-time-fn (instant-time-fn)]
;(println actual-instant-time-fn "dfffff")
      (is (re-matches T-TIME-STAMP actual-instant-time-fn)))))



(deftest uni-month-name_0
  (testing "test-month-name : no offest "
  (console-test "uni-month-name_0" "years-months")
    (let [expected-month-name "December"
          actual-month-name (month-name 0 "2012-12")]

      (is (= expected-month-name actual-month-name)))))

(deftest uni-month-name_1
  (testing "test-month-name : offset +1 over year "
  (console-test "uni-month-name_1" "years-months")
    (let [expected-month-name "January"
          actual-month-name (month-name 1 "2012-12")]

      (is (= expected-month-name actual-month-name)))))

(deftest uni-month-name_2
  (testing "test-month-name : offset -1 over year "
  (console-test "uni-month-name_2" "years-months")
    (let [expected-month-name "December"
          actual-month-name (month-name -1 "2012-01")]

      (is (= expected-month-name actual-month-name)))))


  (deftest uni-prev-month
    (testing "test-prev-month :  "
  (console-test "uni-prev-month" "years-months")
      (let [expected-month-name "December"
            actual-month-name (prev-month "2012-01")]

        (is (= expected-month-name actual-month-name)))))



 (deftest uni-prev-yyyy-mm
   (testing "test-prev-yyyy-mm : java.date to YYYY-MM like 1999-12 if it is 2000-1-1 "
  (console-test "uni-prev-yyyy-mm" "years-months")    
 (let [expected-yyyy-mm "1999-12"
           actual-yyyy-mm (prev-yyyy-mm "2000-01")]

       (is (= expected-yyyy-mm actual-yyyy-mm)))))


(deftest uni-trunc-yyyy-mm-dd
  (testing "test-adjusted-date : java.date to YYYY-MM like 1999-12 if it is 2000-1-1 "
  (console-test "uni-trunc-yyyy-mm-dd" "years-months")    
(let [expected-yyyy-mm "2019-07-04-04-18-46.173Z"
          actual-yyyy-mm (adjusted-date "2019-07-04T04:18:46.173Z")]
      (is (= expected-yyyy-mm actual-yyyy-mm)))))

(deftest uni-trunc-yyyy-mm
  (testing "test-adjusted-date : java.date to YYYY-MM like 1999-12 if it is 2000-1-1 "
 (console-test "uni-trunc-yyyy-mm" "years-months")   
    (let [expected-yyyy-mm "2019-07-04-04-18-46.173Z"
          actual-yyyy-mm (adjusted-date "2019-07-04T04:18:46.173Z")]
      (is (= expected-yyyy-mm actual-yyyy-mm)))))



  (deftest uni-yyyy-mm-to-ints
    (testing "test-yyyy-mm-to-ints : offset -1 over year "
 (console-test "uni-yyyy-mm-to-ints" "years-months")   
      (let [expected-month-name [2234 12]
            actual-month-name (yyyy-mm-to-ints "2234-12")]

        (is (= expected-month-name actual-month-name)))))


  (spec-test/instrument 'current-yyyy-mm-dd)

  (spec-test/instrument 'current-yyyy-mm)


  (spec-test/instrument 'date-to-yyyy-mm)

     (spec-test/instrument 'instant-time-fn)
   
  (spec-test/instrument 'month-name)


  (spec-test/instrument 'prev-month)

  (spec-test/instrument 'prev-yyyy-mm)

  (spec-test/instrument 'trunc-yyyy-mm-dd)

  (spec-test/instrument 'trunc-yyyy-mm)



  (spec-test/instrument 'yyyy-mm-to-ints)









