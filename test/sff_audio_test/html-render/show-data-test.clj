


(def ^:const SHOW-DATA-FILE "./test/sff_audio_test/html-render/show-data.edn")

(defn show-data-test1 [db-type]
  (let [[my-db-obj _ _ _] (build-db DB-TEST-NAME
                                           {}
                                           db-type
                                           TEST-CONFIG-FILE
                                           IGNORE-ENV-VARS)
         purge-table (:purge-table my-db-obj)
         put-items (:put-items my-db-obj)
          test-many [ {:the-url "www.sffaudio.com" 
                      :the-date "2018-03-19-01:54:03.800Z"   
                      :the-html "blah 1111"
                      :the-status true
                      :the-time 1234 }
                    {:the-url "sffaudio.herokuapp.com_rsd_rss" 
                     :the-date "2018-02-19-01:54:03.800Z"		
                     :the-html "bluhss 4444"
                     :the-status false
                     :the-time 12346 } ]
          
          expected-edn  (slurp SHOW-DATA-FILE)
          expected-compressed (strip-white-space expected-edn)
           ]
        

  
   (purge-table)
   (put-items test-many)  
   (let [ actual-edn (show-data my-db-obj "2018-03")
           actual-compressed (strip-white-space actual-edn)
         ]
     
    ;  (reset-test-to-actual-data SHOW-DATA-FILE actual-edn)
     
     
     
           (is (= expected-compressed actual-compressed))
     )
 
    
    
   ))


 (deftest test-show-data
   (testing "test-show-data :"
           (show-data-test1 :monger-db)))
 
 
 
 
 
 
 
 
 