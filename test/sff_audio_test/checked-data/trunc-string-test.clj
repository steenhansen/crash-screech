 
 (deftest test-trunc-string
   (testing "test-trunc-string : cccccccccccccccccccccc "
            (let [trunced-str (trunc-string "123456789" 3)] 
               (is (= trunced-str "123"))
              )))
 
 

