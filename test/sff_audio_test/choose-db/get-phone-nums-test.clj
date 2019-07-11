


 
 
 (deftest test-get-phone-nums
   (testing "test-get-phone-nums : cccccccccccccccccccccc "
     (let [expected-phone-nums ["123-4567" "234-5678"]
           actual-phone-nums (get-phone-nums " 123-4567, 234-5678 ")]
       (is (= expected-phone-nums actual-phone-nums)))))



