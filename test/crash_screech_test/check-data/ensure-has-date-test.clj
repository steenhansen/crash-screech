
 (ns check-data-test)
 
 
 (def ^:const ENSURE-DATA {:the-url "www.sffaudio.com",
                           :the-html "123456789",
                           :the-accurate true,
                           :the-time 1234})

(deftest test-ensure-has-date
  (testing "test-ensure-has-date : DDDDDDDDDDDDD "
    (let [has-date (ensure-has-date ENSURE-DATA)]

      (is (contains? has-date :the-date)))))

