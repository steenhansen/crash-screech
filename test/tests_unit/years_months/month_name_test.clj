
(ns tests-unit.years-months.month-name-test
  (:require [crash-screech.years-months :refer :all])
  (:require [clojure.test :refer :all])
  (:require [global-consts  :refer :all])
  (:require [prepare-tests  :refer :all]))

(deftest test-month-name_0
  (testing "test-month-name : no offest "
    (let [expected-month-name "December"
          actual-month-name (month-name 0 "2012-12")]

      (is (= expected-month-name actual-month-name)))))

(deftest test-month-name_1
  (testing "test-month-name : offset +1 over year "
    (let [expected-month-name "January"
          actual-month-name (month-name 1 "2012-12")]

      (is (= expected-month-name actual-month-name)))))

(deftest test-month-name_2
  (testing "test-month-name : offset -1 over year "
    (let [expected-month-name "December"
          actual-month-name (month-name -1 "2012-01")]

      (is (= expected-month-name actual-month-name)))))

(deftest test-month-name
  (testing "test-month-name : no offest "
    (test-month-name_0)
    (test-month-name_1)
    (test-month-name_2)))
