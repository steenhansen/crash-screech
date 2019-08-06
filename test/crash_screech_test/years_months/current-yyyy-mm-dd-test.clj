
 (deftest test-current-yyyy-mm-dd
   (testing "test-current-yyyy-mm-dd : java.date to YYYY-MM like 2000-12 "
     (let [expected-yyyy-mm-dd "1999-12-13-1234"
           actual-yyyy-mm-dd (current-yyyy-mm-dd "1999-12-13")]

       (is (= expected-yyyy-mm-dd actual-yyyy-mm-dd)))))
