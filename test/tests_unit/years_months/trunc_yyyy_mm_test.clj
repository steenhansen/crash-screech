
(ns tests-unit.years-months.trunc-yyyy-mm-test
  (:require [crash-screech.years-months :refer :all])
  (:require [clojure.test :refer :all])
  (:require [global-consts  :refer :all])
  (:require [prepare-tests  :refer :all]))

(deftest test-trunc-yyyy-mm
  (testing "test-adjusted-date : java.date to YYYY-MM like 1999-12 if it is 2000-1-1 "
    (let [expected-yyyy-mm "2019-07-04-04-18-46.173Z"
          actual-yyyy-mm (adjusted-date "2019-07-04T04:18:46.173Z")]
      (is (= expected-yyyy-mm actual-yyyy-mm)))))

