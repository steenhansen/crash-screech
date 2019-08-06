


 (deftest test-day-hour-min
   (testing "test-day-hour-min : cccccccccccccccccccccc "
     (let [expected-day-hour-min "05-06:07"
           actual-day-hour-min (day-hour-min "2019-04-05-06-07-46.173Z")]
       (is (= expected-day-hour-min actual-day-hour-min)))))



