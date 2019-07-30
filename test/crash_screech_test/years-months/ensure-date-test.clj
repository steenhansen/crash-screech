 
 
 
 
 
 
 

 (deftest test-ensure-date
   (testing "test-ensure-date : java.date to YYYY-MM like 1999-12 if it is 2000-1-1 "
     (let [no-date-record {:the-url "www.sffaudio.com"
                           :the-html "some HTML"
                           :the-accurate true
                           :the-time 3}
           characters-in-date 24
           expected-record "2019-07-04-04-18-46.173Z"
           actual-record (ensure-has-date no-date-record)
           new-date (:the-date actual-record)]

       (is (= (count new-date) characters-in-date)))))




