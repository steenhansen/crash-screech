(ns tests-unit.years-months.current-yyyy-mm-dd-test
  (:require [crash-screech.years-months :refer :all])
  (:require [clojure.test :refer :all])
  (:require [global-consts  :refer :all])
  (:require [prepare-tests  :refer :all]))

 (deftest test-current-yyyy-mm-dd
   (testing "test-current-yyyy-mm-dd : java.date to YYYY-MM like 2000-12 "
     (let [expected-yyyy-mm-dd "1999-12-13"
           actual-yyyy-mm-dd (current-yyyy-mm-dd "1999-12-13-1234")]

       (is (= expected-yyyy-mm-dd actual-yyyy-mm-dd)))))
