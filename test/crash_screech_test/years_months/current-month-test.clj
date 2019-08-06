  (deftest test-current-month
    (testing "test-current-month :  "
      (let [expected-month-name "January"
            actual-month-name (current-month "2012-01")]

        (is (= expected-month-name actual-month-name)))))