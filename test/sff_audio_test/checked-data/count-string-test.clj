 (deftest test-count-string
   (testing "test-count-string : cccccccccccccccccccccc "
            (let [occurance-count (count-string "001001001000" #"1")] 
               (is (= occurance-count 3))
              )))
