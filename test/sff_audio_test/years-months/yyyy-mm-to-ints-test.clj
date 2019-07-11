



  (deftest test-yyyy-mm-to-ints
    (testing "test-yyyy-mm-to-ints : offset -1 over year "
      (let [expected-month-name [2234 56]
            actual-month-name (yyyy-mm-to-ints "2234-56")]

        (is (= expected-month-name actual-month-name)))))



