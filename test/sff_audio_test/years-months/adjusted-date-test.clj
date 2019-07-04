 
 
 ;(println (str (java-time/instant)))
 (deftest test-adjusted-date 
   (testing "test-adjusted-date : java.date to YYYY-MM like 1999-12 if it is 2000-1-1 "
            (let [
                   expected-yyyy-mm "2019-07-04-04-18-46.173Z"
                   actual-yyyy-mm (adjusted-date "2019-07-04T04:18:46.173Z")
                  ] 
              
               (is (= expected-yyyy-mm actual-yyyy-mm))
              )))

 
 
 
