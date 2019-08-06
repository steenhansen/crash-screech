  (deftest test-prev-month
    (testing "test-prev-month :  "
      (let [expected-month-name "December"
            actual-month-name (prev-month "2012-01")]

        (is (= expected-month-name actual-month-name)))))