


(ns tests-unit.years-months.prev-yyyy-mm-test
  (:require [crash-screech.years-months :refer :all])
  (:require [clojure.test :refer :all])
  (:require [global-consts  :refer :all])
  (:require [prepare-tests  :refer :all]))

 (deftest test-prev-yyyy-mm
   (testing "test-prev-yyyy-mm : java.date to YYYY-MM like 1999-12 if it is 2000-1-1 "
     (let [expected-yyyy-mm "1999-12"
           actual-yyyy-mm (prev-yyyy-mm "2000-01")]

       (is (= expected-yyyy-mm actual-yyyy-mm)))))
