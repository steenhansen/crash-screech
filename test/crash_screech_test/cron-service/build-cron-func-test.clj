


 
 
 (deftest test-build-cron-func
   (testing "test-build-cron-func : 3237777774cccccccccccccccccccccc "
     (let [expected-phone-nums ["123-4567" "234-5678"]
           actual-phone-nums (get-phone-nums " 123-4567, 234-5678 ")]
       (is (= expected-phone-nums  expected-phone-nums)))))


