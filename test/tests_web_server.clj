
(ns tests-web-server
  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as spec-alpha]
            [clojure.spec.gen.alpha :as spec-gen]
            [clojure.spec.test.alpha :as spec-test])
  (:require [global-consts-vars  :refer :all])
  (:require [crash-screech.choose-db :refer [build-db]])
  (:require [crash-screech.web-server :refer :all])
  (:require [crash-screech.sms-event :refer [build-sms-send build-web-scrape]])
  (:require [crash-screech.scrape-html :refer [scrape-pages-fn]])
  (:require [crash-screech.years-months :refer [date-with-now-time-fn instant-time-fn]])
  (:require [java-time :refer [local-date?]])
  (:require [text-diff :refer [is-html-eq]])
  (:require [prepare-tests :refer :all]))

(defn web-server-specs []
  (if RUN-SPEC-TESTS
    (do
      (println "Speccing web-server")
      (spec-test/instrument)
      (spec-test/instrument 'make-request-fn))))

(deftest integration-produce-page
  (testing "test-day-hour-min : qqqwwwww "
    (console-test  "integration-make-request-fn"  "web-server"))

  ;(reset-test-to-actual-data test-file actual-data)           ;; to re-save the actual output into expected

   ; like in tests_check_data.clj

   ; need to ignore the-time / check-time
  )

 (deftest integration-make-request-fn
   (testing "test-day-hour-min : fffffff "
     (console-test  "integration-make-request-fn"  "web-server")
    (let [db-type USE_MONGER_DB
          the-check-pages (make-check-pages 0)
          [my-db-obj _ cron-url sms-data] (build-db T-TEST-COLLECTION
                                                    the-check-pages
                                                    db-type
                                                    TEST-CONFIG-FILE
                                                    IGNORE-ENV-VARS)
          purge-table (:purge-table my-db-obj)
          testing-sms? true
          test-date (date-with-now-time-fn "2019-10-01")
          temporize-func (build-web-scrape scrape-pages-fn my-db-obj the-check-pages sms-data testing-sms? test-date)
          request-handler (make-request-fn temporize-func my-db-obj cron-url sms-data testing-sms? test-date)
          send-test-sms-url (:send-test-sms-url sms-data)
          the-request (request-handler  {:uri cron-url})
          the-body (:body the-request)
          file-expected (slurp  (str SCRAPED-TEST-DATA "tests_web_server_1.html"))
          conformed-actual (conform-whitespace the-body)
          conformed-expected (conform-whitespace file-expected)]
      (purge-table)
      (let [[text-diff-1 text-diff-2] (is-html-eq conformed-actual conformed-expected)]
        (is (= text-diff-1 text-diff-2))))
))



(deftest integration-make-request-fails-fn
  (testing "test-day-hour-min : cvvvvvvvvvvvvvvvvvvvvvvvvvv "
    (console-test  "integration-make-request-FAILSfn"  "web-server")
    (let [db-type USE_MONGER_DB
          the-check-pages (make-check-pages 123456)
          [my-db-obj _ cron-url sms-data] (build-db T-TEST-COLLECTION
                                                    the-check-pages
                                                    db-type
                                                    TEST-CONFIG-FILE
                                                    IGNORE-ENV-VARS)
          purge-table (:purge-table my-db-obj)
_   (purge-table)
          testing-sms? true
          test-date (date-with-now-time-fn "2019-10-01")
          temporize-func (build-web-scrape scrape-pages-fn my-db-obj the-check-pages sms-data testing-sms? test-date)
          request-handler (make-request-fn temporize-func my-db-obj cron-url sms-data testing-sms? test-date)
          send-test-sms-url (:send-test-sms-url sms-data)
          the-request (request-handler  {:uri cron-url})
          the-body (:body the-request)
          file-expected (slurp  (str SCRAPED-TEST-DATA "tests_web_server_2.html"))
          conformed-actual (conform-whitespace the-body)
          conformed-expected (conform-whitespace file-expected)]
      ;(purge-table)
      (let [[text-diff-1 text-diff-2] (is-html-eq conformed-actual conformed-expected)]
        (is (= text-diff-1 text-diff-2))))))

(defn do-tests []
  (web-server-specs)
  (run-tests 'tests-web-server))
