

(ns tests-unit.years-months.prev-month-test
  (:require [crash-screech.years-months :refer :all])
  (:require [clojure.test :refer :all])
  (:require [global-consts  :refer :all])
  (:require [prepare-tests  :refer :all]))

  (deftest test-prev-month
    (testing "test-prev-month :  "
      (let [expected-month-name "December"
            actual-month-name (prev-month "2012-01")]

        (is (= expected-month-name actual-month-name)))))
