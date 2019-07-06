 
 (deftest test-sub-string
   (testing "test-sub-string : cccccccccccccccccccccc "
            (let [subbed-str (sub-string "1234567" 3 6)] 
               (is (= subbed-str "456"))
              )))
 
 

