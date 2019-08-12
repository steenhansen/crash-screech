
(ns tests-unit.years-months.current-yyyy-mm-test
  (:require [crash-screech.years-months :refer :all])
  (:require [clojure.test :refer :all])
  (:require [global-consts  :refer :all])
  (:require [prepare-tests  :refer :all]))

 (deftest test-current-yyyy-mm-0
   (testing "test-current-yyyy-mm : java.date to YYYY-MM like 2000-12 "
     (let [expected-yyyy-mm "1999-12"
           actual-yyyy-mm (current-yyyy-mm "1999-12")]

       (is (= expected-yyyy-mm actual-yyyy-mm)))))

 (deftest test-current-yyyy-mm-days
   (testing "test-current-yyyy-mm : java.date to YYYY-MM like 2000-12 "
     (let [expected-yyyy-mm "1999-12"
           actual-yyyy-mm (current-yyyy-mm "1999-12-14")]

       (is (= expected-yyyy-mm actual-yyyy-mm)))))

(deftest test-current-yyyy-mm 
  (testing "test-current-yyyy-mm : no offest "
    (test-current-yyyy-mm-0)
    (test-current-yyyy-mm-days)))
