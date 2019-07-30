





(def ^:const GET-TWO-MONTHS-FILE "./test/crash_screech_test/html-render/get-two-months.edn")

(defn build-db-test1 [db-type]
  (let [[my-db-obj web-port cron-url sms-data] (build-db DB-TEST-NAME
                                                         {}
                                                         db-type
                                                         TEST-CONFIG-FILE
                                                         IGNORE-ENV-VARS)]

    (is (function? (:delete-table my-db-obj)))
    (is (function? (:purge-table my-db-obj)))
    (is (function? (:get-all my-db-obj)))
    (is (function? (:get-url my-db-obj)))
    (is (function? (:put-item my-db-obj)))
    (is (function? (:put-items my-db-obj)))
    (is (is-string-number web-port))

    (println cron-url)   ; q*BERT            (is-url-dir cron-url)
  ;  (println sms-data)
    ))

(deftest test-build-db-3333
  (testing "test-build-db :"
    (build-db-test1 :monger-db)))

(deftest test-build-db-3334
  (testing "test-build-db :"
    (build-db-test1     :amazonica-db)))



