
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
      (spec-test/instrument 'build-express-serve))))

(deftest integration-produce-page
  (testing "test-day-hour-min : qqqwwwww "
    (console-test  "integration-build-express-serve"  "web-server"))

  ;(reset-test-to-actual-data test-file actual-data)           ;; to re-save the actual output into expected

   ; like in tests_check_data.clj

   ; need to ignore the-time / check-time
  )

;;  (deftest integration-build-express-serve
;;    (testing "test-day-hour-min : fffffff "
;;      (console-test  "integration-build-express-serve"  "web-server")
;;     (let [db-type USE_MONGER_DB
;;           the-check-pages (make-check-pages 0)
;;           [my-db-obj _ cron-url sms-data] (build-db T-TEST-COLLECTION
;;                                                     the-check-pages
;;                                                     db-type
;;                                                     TEST-CONFIG-FILE
;;                                                     IGNORE-ENV-VARS)
;;           purge-table (:purge-table my-db-obj)
;;           testing-sms? true
;;           test-date (date-with-now-time-fn "2019-10-01")
;;           web-scraper (build-web-scrape scrape-pages-fn my-db-obj the-check-pages sms-data testing-sms? test-date)
;;           express-server (build-express-serve web-scraper my-db-obj cron-url sms-data testing-sms? test-date)
;;           send-test-sms-url (:send-test-sms-url sms-data)
;;           web-page (express-server  {:uri cron-url})
;;           the-body (:body web-page)
;;           file-expected (slurp  (str SCRAPED-TEST-DATA "tests_web_server_1.html"))
;;           conformed-actual (conform-whitespace the-body)
;;           conformed-expected (conform-whitespace file-expected)]
;;       (purge-table)
;;       (let [[text-diff-1 text-diff-2] (is-html-eq conformed-actual conformed-expected)]
;;         (is (= text-diff-1 text-diff-2))))
;; ))



;; (deftest integration-make-request-fails-fn
;;   (testing "test-day-hour-min : cvvvvvvvvvvvvvvvvvvvvvvvvvv "
;;     (console-test  "integration-make-request-FAILSfn"  "web-server")
;;     (let [db-type USE_MONGER_DB
;;           all-fail 123456
;;           the-check-pages (make-check-pages all-fail)
;;           [my-db-obj _ cron-url sms-data] (build-db T-TEST-COLLECTION
;;                                                     the-check-pages
;;                                                     db-type
;;                                                     TEST-CONFIG-FILE
;;                                                     IGNORE-ENV-VARS)
;;           purge-table (:purge-table my-db-obj)
;; _   (purge-table)
;;           testing-sms? true
;;           test-date (date-with-now-time-fn "2019-10-01")
;;           web-scraper (build-web-scrape scrape-pages-fn my-db-obj the-check-pages sms-data testing-sms? test-date)

;;           express-server (build-express-serve web-scraper my-db-obj cron-url sms-data testing-sms? test-date)
;;           send-test-sms-url (:send-test-sms-url sms-data)
;;           web-page (express-server  {:uri cron-url})
;;           the-body (:body web-page)
;;           file-expected (slurp  (str SCRAPED-TEST-DATA "tests_web_server_2.html"))
;;           conformed-actual (conform-whitespace the-body)
;;           conformed-expected (conform-whitespace file-expected)]
;;       ;(purge-table)
;;       (let [[text-diff-1 text-diff-2] (is-html-eq conformed-actual conformed-expected)]
;;         (is (= text-diff-1 text-diff-2))))))


(defn save-record [yyyy-mm-dd my-db-obj sms-data]
  (let [sff-audio [{:check-page WWW-SFFAUDIO-COM
                    :enlive-keys SFFAUDIO-CHECK-KEYS
                    :at-least SFFAUDIO-AMOUNT}]
        testing-sms? true
        record-date (date-with-now-time-fn yyyy-mm-dd)
        scrape-web (build-web-scrape scrape-pages-fn my-db-obj sff-audio sms-data testing-sms? record-date)]
    (scrape-web)))

(defn make-pair [last-month-date this-month-date html-test-file]
  (let [db-type USE_MONGER_DB
        testing-sms? true
        sff-audio [{:check-page WWW-SFFAUDIO-COM
                    :enlive-keys SFFAUDIO-CHECK-KEYS
                    :at-least SFFAUDIO-AMOUNT}]
        [my-db-obj _ cron-url sms-data] (build-db T-TEST-COLLECTION
                                                  sff-audio
                                                  db-type
                                                  TEST-CONFIG-FILE
                                                  IGNORE-ENV-VARS)
        purge-table (:purge-table my-db-obj)
        _ (purge-table)
        _  (save-record last-month-date my-db-obj sms-data)
        _  (save-record this-month-date my-db-obj sms-data)
        test-date (date-with-now-time-fn this-month-date)
        web-scraper (fn [] ())
        express-server (build-express-serve web-scraper my-db-obj cron-url sms-data testing-sms? test-date)
        send-test-sms-url (:send-test-sms-url sms-data)
        web-page (express-server  {:uri cron-url})
        the-body (:body web-page)
        file-expected (slurp  (str SCRAPED-TEST-DATA html-test-file))
        conformed-actual (conform-whitespace the-body)
        conformed-expected (conform-whitespace file-expected)]
    (let [[text-diff-1 text-diff-2] (is-html-eq conformed-actual conformed-expected)]
      (is (= text-diff-1 text-diff-2)))))

(deftest make-request-every-month
  (testing "make-request-every-month : dddd "
    (console-test  "integration-make-request-   CLOCK   "  "web-server")

     (make-pair  "2009-12-01"  "2010-01-01" "tests_01_web_server_jan_2010.html")
     (make-pair  "2010-01-01"  "2010-02-01" "tests_02_web_server_feb_2010.html")
     (make-pair  "2010-02-01"  "2010-03-01" "tests_03_web_server_mar_2010.html")
     (make-pair  "2010-03-01"  "2010-04-01" "tests_04_web_server_apr_2010.html")
     (make-pair  "2010-04-01"  "2010-05-01" "tests_05_web_server_may_2010.html")
     (make-pair  "2010-05-01"  "2010-06-01" "tests_06_web_server_jun_2010.html")
     (make-pair  "2010-06-01"  "2010-07-01" "tests_07_web_server_jul_2010.html")
     (make-pair  "2010-07-01"  "2010-08-01" "tests_08_web_server_aug_2010.html")
     (make-pair  "2010-08-01"  "2010-09-01" "tests_09_web_server_sep_2010.html")
     (make-pair  "2010-09-01"  "2010-10-01" "tests_10_web_server_oct_2010.html")
     (make-pair  "2010-10-01"  "2010-11-01" "tests_11_web_server_nov_2010.html")
     (make-pair  "2010-11-01"  "2010-12-01" "tests_12_web_server_dec_2010.html")
     (make-pair  "2010-12-01"  "2011-01-01" "tests_13_web_server_jan_2011.html")
     (make-pair  "2011-01-01"  "2011-02-01" "tests_14_web_server_feb_2011.html")
))

(defn do-tests []
  (web-server-specs)
  (run-tests 'tests-web-server))
