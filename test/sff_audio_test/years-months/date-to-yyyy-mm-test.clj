










; (deftest a-test (testing "FIXME, I fail.asdasd" (is (= 1 1))))
;        (identity "2000-01-01")





 (deftest test-date-to-yyyy-mm 
   (testing "test-date-to-yyyy-mm : java.date to YYYY-MM like 2000-12 "
            (let [
                   expected-yyyy-mm "1999-12"
                   actual-yyyy-mm (date-to-yyyy-mm (java-time/local-date 1999 12 31)) 
                  ] 
              
               (is (= expected-yyyy-mm actual-yyyy-mm))
              )))
