
(ns tests-unit.years-months.current-month-test
  (:require [crash-screech.years-months :refer :all])
  (:require [clojure.test :refer :all])
  (:require [global-consts  :refer :all])
  (:require [prepare-tests  :refer :all]))


  (deftest test-current-month
    (testing "test-current-month :  "
      (let [expected-month-name "January"
            actual-month-name (current-month "2012-01")]

        (is (= expected-month-name actual-month-name)))))
