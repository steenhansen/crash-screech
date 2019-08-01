
(ns crash-screech-test.choose-db.get-phone-nums-test
   (:require [crash-screech.choose-db :refer :all])
   
    (:require [clojure.test :refer :all])
   ; (:require [clojure.spec.alpha :as spec-alpha]
   ;          [clojure.spec.gen.alpha :as spec-gen]
   ;          [clojure.spec.test.alpha :as spec-test])
   
  )

 
 
(deftest test-get-phone-nums
  (testing "test-get-phone-nums : cccccccccccccccccccccc "
    (let [expected-phone-nums ["123-4567" "234-5678"]
          actual-phone-nums (get-phone-nums " 123-4567, 234-5678 ")]
      (is (= expected-phone-nums actual-phone-nums)))))

