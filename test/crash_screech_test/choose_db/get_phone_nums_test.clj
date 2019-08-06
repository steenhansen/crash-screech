
(ns crash-screech-test.choose-db.get-phone-nums-test
   (:require [crash-screech.choose-db :refer :all])
    (:require [clojure.test :refer :all])
  )

 
 
(deftest test-get-phone-nums
  (testing "test-get-phone-nums : cccccccccccccccccccccc "
  

      (let [expected-phone-nums ["12345678901" "01234567890"]
            actual-phone-nums (get-phone-nums "12345678901,01234567890")]
        (is (= expected-phone-nums actual-phone-nums)))
      
      
    
    
    ))

