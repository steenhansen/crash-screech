







(defn get-index-test1 [db-type]
  (let [[my-db-obj _ _ _] (build-db DB-TEST-NAME
                                           {}
                                           db-type
                                           TEST-CONFIG-FILE
                                           IGNORE-ENV-VARS)
         purge-table (:purge-table my-db-obj)
         put-items (:put-items my-db-obj)
          test-many [ {:the-url "www.sffaudio.com" 
                      :the-date "2019-06-19-01:54:03.800Z"   
                      :the-html "blah 1111"
                      :the-status true
                      :the-time 1234 }
                    {:the-url "sffaudio.herokuapp.com_rsd_rss" 
                     :the-date "2019-05-19-01:54:03.800Z"		
                     :the-html "bluhss 4444"
                     :the-status false
                     :the-time 12346 } ]
          
          expected-html  (slurp "./test/sff_audio_test/html-render/two-records.html")
          expected-compressed (clj-str/replace expected-html #"\s" "")
           ]
        

  
   (purge-table)
   (put-items test-many)  
   (let [ actual-html (get-index my-db-obj "2019-06")
           actual-compressed (clj-str/replace actual-html #"\s" "")
         ]
   ;  (println "uuuuuuuuuuuuuuuu " expected-compressed)
   ;  (println "xxxxxxxxxxxxxxxxxxx " actual-compressed)
     
      (is (= expected-compressed actual-compressed))
     )
    
    
    
   ))


 (deftest test-get-index
   (testing "test-get-index :"
           (get-index-test1 :monger-db)))