


(def ^:const GET-TWO-MONTHS-FILE "./test/sff_audio_test/html-render/get-two-months.edn")

(defn get-two-months-test1 [db-type]
  (let [[my-db-obj _ _ _] (build-db DB-TEST-NAME
                                    {}
                                    db-type
                                    TEST-CONFIG-FILE
                                    IGNORE-ENV-VARS)
        purge-table (:purge-table my-db-obj)
        put-items (:put-items my-db-obj)
        test-many [{:the-url "www.sffaudio.com"
                    :the-date "2019-10-19-01:54:03.800Z"
                    :the-html "blah 1111"
                    :the-status true
                    :the-time 1234}
                   {:the-url "sffaudio.herokuapp.com_rsd_rss"
                    :the-date "2019-09-19-01:54:03.800Z"
                    :the-html "bluhss 4444"
                    :the-status false
                    :the-time 12346}]

        expected-edn  (slurp GET-TWO-MONTHS-FILE)
        expected-compressed (strip-white-space expected-edn)]

    (purge-table)
    (put-items test-many)
    (let [actual-edn (get-two-months my-db-obj "2019-10")
          actual-compressed (strip-white-space actual-edn)]
     ;(reset-test-to-actual-data GET-TWO-MONTHS-FILE actual-edn)
      (is (= expected-compressed actual-compressed)))))

(deftest test-get-two-months
  (testing "test-get-two-months :"
    (get-two-months-test1 :monger-db)))



