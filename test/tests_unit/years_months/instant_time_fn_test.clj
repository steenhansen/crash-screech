
(ns tests-unit.years-months.instant-time-fn-test
  (:require [crash-screech.years-months :refer :all])
  (:require [clojure.test :refer :all])
  (:require [global-consts  :refer :all])
  (:require [prepare-tests  :refer :all]))


(def time-stamp #"^([\d]{4})-(0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-9]|3[0-1])T\d\d:\d\d:\d\d\.\d\d\dZ$")


(deftest test-instant-time-fn
  (testing "test-adjusted-date : java.date to YYYY-MM like 1999-12 if it is 2000-1-1 "
    (let [ actual-instant-time-fn (instant-time-fn)]
      (is (re-matches time-stamp actual-instant-time-fn)))))

