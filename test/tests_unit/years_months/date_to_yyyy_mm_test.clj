
(ns tests-unit.years-months.date-to-yyyy-mm-test
  (:require [java-time.local :as j-time])
  (:require [crash-screech.years-months :refer :all])
  (:require [clojure.test :refer :all])
  (:require [global-consts  :refer :all])
  (:require [prepare-tests  :refer :all]))







 (deftest test-date-to-yyyy-mm
   (testing "test-date-to-yyyy-mm : java.date to YYYY-MM like 2000-12 "
     (let [expected-yyyy-mm "1999-12"
           actual-yyyy-mm (date-to-yyyy-mm (j-time/local-date 1999 12 31))]

       (is (= expected-yyyy-mm actual-yyyy-mm)))))
