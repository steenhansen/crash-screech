
 
  
 (def ^:const BEFORE-THE-DATA {:the-url "www.sffaudio.com",
                               :the-date "2019-06-19-01:54:03.800Z",
                               :the-html "123456789",
                               :the-accurate true,
                               :the-time 1234})

(def ^:const AFTER-CHECK-DATA {:check-url "www.sffaudio.com",
                               :check-date "2019-06-19-01-54-03.800Z",
                               :check-html "123456789",
                               :check-bytes 9
                               :check-accurate true,
                               :check-time 1234})

(deftest test-derive-data
  (testing "test-dervive-data : cccccccccccccccccccccc "
    (let [derived-data (derive-data BEFORE-THE-DATA)]
      (is (= derived-data AFTER-CHECK-DATA)))))

