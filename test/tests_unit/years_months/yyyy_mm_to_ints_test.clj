

(ns tests-unit.years-months.yyyy-mm-to-ints-test
  (:require [crash-screech.years-months :refer :all])
  (:require [clojure.test :refer :all])
  (:require [global-consts  :refer :all])
  (:require [prepare-tests  :refer :all]))


  (deftest test-yyyy-mm-to-ints
    (testing "test-yyyy-mm-to-ints : offset -1 over year "
      (let [expected-month-name [2234 12]
            actual-month-name (yyyy-mm-to-ints "2234-12")]

        (is (= expected-month-name actual-month-name)))))



