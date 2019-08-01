
 (ns check-data-test)
 
 (def ^:const BEFORE-UNIQUE-ID {:check-url "www.sffaudio.com",
                                :check-date "2019-06-19-01:54:03.800Z",
                                :check-html "123456789",
                                :check-bytes 9
                                :check-accurate true,
                                :check-time 1234})

(def ^:const AFTER-UNIQUE-ID {:check-url "www.sffaudio.com",
                              :check-date "2019-06-19-01:54:03.800Z",
                              :_id "2019-06-19-01:54:03.800Z+1",
                              :check-html "123456789",
                              :check-bytes 9
                              :check-accurate true,
                              :check-time 1234})

(deftest test-uniquely-id
  (testing "test-uniquely-id : cccccccccccccccccccccc "
    (let [unique-data (uniquely-id 1 BEFORE-UNIQUE-ID)]
      (is (= unique-data AFTER-UNIQUE-ID)))))

