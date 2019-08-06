
; test 1999-12-31

;  also make new file current-yyyy-mm-dd.clj

 (deftest test-current-yyyy-mm
   (testing "test-current-yyyy-mm : java.date to YYYY-MM like 2000-12 "
     (let [expected-yyyy-mm "1999-12"
           actual-yyyy-mm (current-yyyy-mm "1999-12")]

       (is (= expected-yyyy-mm actual-yyyy-mm)))))

 (deftest test-current-yyyy-mm-days
   (testing "test-current-yyyy-mm : java.date to YYYY-MM like 2000-12 "
     (let [expected-yyyy-mm "1999-12"
           actual-yyyy-mm (current-yyyy-mm "1999-12-14")]

       (is (= expected-yyyy-mm actual-yyyy-mm)))))
