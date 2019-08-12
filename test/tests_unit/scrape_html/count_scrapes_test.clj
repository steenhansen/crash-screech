
(ns tests-unit.scrape-html.count-scrapes-test
  (:require [crash-screech.scrape-html :refer :all])
  (:require [clojure.test :refer :all])
  (:require [global-consts  :refer :all])
  (:require [prepare-tests  :refer :all]))

(def ^:const COUNT-SCRAPES-HTML " a_countable_scrape a_countable_scrape ")

(deftest test-count-scrapes
  (testing "test-count-scrapes : ssssssss "
    (let [expected-scrapes 2
          actual-scrapes (count-scrapes COUNT-SCRAPES-HTML)]
      (is (= expected-scrapes actual-scrapes)))))
