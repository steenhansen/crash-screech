(ns crash-screech-test.check-data.count-string-test
  (:require [crash-screech.check-data :refer :all])
      (:require [clojure.test :refer :all])
)

 (deftest test-count-string
   (testing "test-count-string : cccccccccccccccccccccc "
     (let [occurance-count (count-string "001001001000" #"1")]
       (is (= occurance-count 3)))))



