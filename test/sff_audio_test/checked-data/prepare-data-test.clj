 
 
 (def ^:const BEFORE-ENSURE-DATA 

 [{:the-url "www.sffaudio.com",
                    :the-date "2019-06-19-01:54:03.800Z",
                    :the-html "blah 1111",
                    :the-accurate true,
                    :the-time 1234}
                   {:the-url "sffaudio.herokuapp.com_rsd_rss",
                    :the-date "2019-06-19-01:54:03.800Z",
                    :the-html "bluh 2222",
                    :the-accurate true,
                    :the-time 12346}
   ]
)
 
 
 (def ^:const AFTER-ENSURE-DATA 

 [{:check-url "www.sffaudio.com",
                    :check-date "2019-06-19-01-54-03.800Z",
                     :_id "2019-06-19-01-54-03.800Z+0",
                    :check-html "blah 1111",
:check-bytes 9
                    :check-accurate true,
                    :check-time 1234}
                   {:check-url "sffaudio.herokuapp.com_rsd_rss",
                     :_id "2019-06-19-01-54-03.800Z+1",
                    :check-date "2019-06-19-01-54-03.800Z",
                    :check-html "bluh 2222",
:check-bytes 9
                    :check-accurate true,
                    :check-time 12346}
   ]
)

 
 (deftest test-prepare-data
   (testing "test-prepare-data : fffff "
            (let [prepared-data (prepare-data BEFORE-ENSURE-DATA)
                  
                  ] 
; (println "a" prepared-data)
;              (println "b" AFTER-ENSURE-DATA)
               (is (= prepared-data AFTER-ENSURE-DATA))
             
              )))
 
