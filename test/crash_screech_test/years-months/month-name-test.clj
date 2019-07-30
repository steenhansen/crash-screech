 (deftest test-month-name_0
   (testing "test-month-name : no offest "
     (let [expected-month-name "December"
           actual-month-name (month-name 0 "2012-12")]

       (is (= expected-month-name actual-month-name)))))

(deftest test-month-name_1
  (testing "test-month-name : offset +1 over year "
    (let [expected-month-name "January"
          actual-month-name (month-name 1 "2012-12")]

      (is (= expected-month-name actual-month-name)))))

(deftest test-month-name_-1
  (testing "test-month-name : offset -1 over year "
    (let [expected-month-name "December"
          actual-month-name (month-name -1 "2012-01")]

      (is (= expected-month-name actual-month-name)))))