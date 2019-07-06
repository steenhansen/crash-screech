


 ; expected-record "2019-07-04-04-18-46.173Z"
 
 
 (deftest test-get-phone-nums
   (testing "test-get-phone-nums : cccccccccccccccccccccc "
            (let [
                   expected-phone-nums ["123-4567" "234-5678"]
                   actual-phone-nums (get-phone-nums " 123-4567, 234-5678 ")
                  ] 
              ;(println "dddddddddddddd actual-day-hour-min = " actual-day-hour-min)
               (is (= expected-phone-nums actual-phone-nums))
              )))
 
 

