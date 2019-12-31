
(ns tests-web-server
  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as spec-alpha]
            [clojure.spec.gen.alpha :as spec-gen]
            [clojure.spec.test.alpha :as spec-test])
  (:require [global-consts-vars  :refer :all])
  (:require [crash-sms.choose-db :refer [build-db]])
  (:require [crash-sms.web-server :refer :all])
  (:require [crash-sms.sms-event :refer [build-sms-send build-web-scrape]])
  (:require [crash-sms.scrape-html :refer [scrape-pages-fn]])
  (:require [crash-sms.years-months :refer [date-with-now-time-fn instant-time-fn]])
  (:require [java-time :refer [local-date?]])
  (:require [text-diff :refer [is-html-eq]])
  (:require [prepare-tests :refer :all]))

(defn web-server-specs []
      (println "Speccing web-server")
      (spec-test/instrument)
      (spec-test/instrument 'build-express-serve))

(defn html-correct [db-type]
  (console-test "web-server html-correct" db-type)
  (let [the-check-pages (make-check-pages 0)
        [my-db-obj _ cron-url sms-data] (build-db T-TEST-COLLECTION
                                                  the-check-pages
                                                  db-type
                                                  TEST-CONFIG-FILE
                                                  IGNORE-ENV-VARS)
        purge-table (:purge-table my-db-obj)
        _ (purge-table)
        testing-sms? true
        test-date (date-with-now-time-fn "2017-05-31")
        web-scraper (build-web-scrape scrape-pages-fn my-db-obj the-check-pages sms-data testing-sms? test-date)
        express-server (build-express-serve web-scraper my-db-obj cron-url sms-data testing-sms? test-date)
        send-test-sms-url (:send-test-sms-url sms-data)
        web-page (express-server  {:uri cron-url})
        the-body (:body web-page)
        file-expected (slurp  (str SCRAPED-TEST-DATA "tests_web_server_all_correct.html"))
        conformed-actual (conform-whitespace the-body)
        conformed-expected (conform-whitespace file-expected)]
    (let [[text-diff-1 text-diff-2] (is-html-eq conformed-actual conformed-expected)]
      [text-diff-1 text-diff-2])))

;   (clojure.test/test-vars [#'tests-web-server/all-correct])
(deftest all-correct
  (console-test  "web-server all-correct")
  (let [[text-diff-1 text-diff-2] (html-correct  USE_FAKE_DB)]
    (is (= text-diff-1 text-diff-2)))

  ;; (let [[text-diff-1 text-diff-2] (html-correct  USE_MONGER_DB)]
  ;;   (is (= text-diff-1 text-diff-2)))

  ;; (let [[text-diff-1 text-diff-2] (html-correct  USE_AMAZONICA_DB)]
  ;;   (is (= text-diff-1 text-diff-2)))
)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defn html-wrong [db-type]
  (console-test  "web-server html-wrong" db-type)
  (let [all-fail 123456
        the-check-pages (make-check-pages all-fail)
        [my-db-obj _ cron-url sms-data] (build-db T-TEST-COLLECTION
                                                  the-check-pages
                                                  db-type
                                                  TEST-CONFIG-FILE
                                                  IGNORE-ENV-VARS)
        purge-table (:purge-table my-db-obj)
        _   (purge-table)
        testing-sms? true
        test-date (date-with-now-time-fn "2019-10-01")
        web-scraper (build-web-scrape scrape-pages-fn my-db-obj the-check-pages sms-data testing-sms? test-date)
        express-server (build-express-serve web-scraper my-db-obj cron-url sms-data testing-sms? test-date)
        send-test-sms-url (:send-test-sms-url sms-data)
        web-page (express-server  {:uri cron-url})
        the-body (:body web-page)
        file-expected (slurp  (str SCRAPED-TEST-DATA "tests_web_server_all_wrong.html"))
        conformed-actual (conform-whitespace the-body)
        conformed-expected (conform-whitespace file-expected)]
    (let [[text-diff-1 text-diff-2] (is-html-eq conformed-actual conformed-expected)]
      (is (= text-diff-1 text-diff-2)))))

;   (clojure.test/test-vars [#'tests-web-server/all-wrong])
(deftest all-wrong
  (console-test  "web-server all-wrong")
  (html-wrong USE_FAKE_DB)
 ; (html-wrong USE_MONGER_DB)
 ; (html-wrong USE_AMAZONICA_DB)
)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; below actually reads the real web pages, making it slow
(defn save-record [yyyy-mm-dd my-db-obj sms-data db-type]
  (console-test  "web-server save-record" db-type yyyy-mm-dd)
  (let [sff-audio [{:check-page WWW-SFFAUDIO-COM
                    :enlive-keys SFFAUDIO-CHECK-KEYS
                    :at-least SFFAUDIO-AMOUNT}]
        testing-sms? true
        record-date (date-with-now-time-fn yyyy-mm-dd)
        scrape-web (build-web-scrape scrape-pages-fn my-db-obj sff-audio sms-data testing-sms? record-date)]
    (scrape-web)))

(defn make-pair [last-month-date this-month-date html-test-file db-type]
  (let [testing-sms? true
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
        _  (save-record last-month-date my-db-obj sms-data db-type)
        _  (save-record this-month-date my-db-obj sms-data db-type)
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
      [text-diff-1 text-diff-2])))

(defn one-year [db-type]
  (console-test  "web-server one-year" db-type)
  (let [[jan-act jan-exp] (make-pair "2009-12-01" "2010-01-01" "tests_01_web_server_jan_2010.html" db-type)]
    (is (= jan-act jan-exp)))
  (let [[feb-act feb-exp] (make-pair "2010-01-01" "2010-02-01" "tests_02_web_server_feb_2010.html" db-type)]
    (is (= feb-act feb-exp)))
  (let [[mar-act mar-exp] (make-pair "2010-02-01" "2010-03-01" "tests_03_web_server_mar_2010.html" db-type)]
    (is (= mar-act mar-exp)))
  (let [[apr-act apr-exp] (make-pair "2010-03-01" "2010-04-01" "tests_04_web_server_apr_2010.html" db-type)]
    (is (= apr-act apr-exp)))
  (let [[may-act may-exp] (make-pair "2010-04-01" "2010-05-01" "tests_05_web_server_may_2010.html" db-type)]
    (is (= may-act may-exp)))
  (let [[jun-act jun-exp] (make-pair "2010-05-01" "2010-06-01" "tests_06_web_server_jun_2010.html" db-type)]
    (is (= jun-act jun-exp)))
  (let [[jul-act jul-exp] (make-pair "2010-06-01" "2010-07-01" "tests_07_web_server_jul_2010.html" db-type)]
    (is (= jul-act jul-exp)))
  (let [[aug-act aug-exp] (make-pair "2010-07-01" "2010-08-01" "tests_08_web_server_aug_2010.html" db-type)]
    (is (= aug-act aug-exp)))
  (let [[sep-act sep-exp] (make-pair "2010-08-01" "2010-09-01" "tests_09_web_server_sep_2010.html" db-type)]
    (is (= sep-act sep-exp)))
  (let [[oct-act oct-exp] (make-pair "2010-09-01" "2010-10-01" "tests_10_web_server_oct_2010.html" db-type)]
    (is (= oct-act oct-exp)))
  (let [[nov-act nov-exp] (make-pair "2010-10-01" "2010-11-01" "tests_11_web_server_nov_2010.html" db-type)]
    (is (= nov-act nov-exp)))
  (let [[dec-act dec-exp] (make-pair "2010-11-01" "2010-12-01" "tests_12_web_server_dec_2010.html" db-type)]
    (is (= dec-act dec-exp)))
  (let [[jan-act jan-exp] (make-pair "2010-12-01" "2011-01-01" "tests_13_web_server_jan_2011.html" db-type)]
    (is (= jan-act jan-exp)))
  (let [[feb-act feb-exp] (make-pair "2011-01-01" "2011-02-01" "tests_14_web_server_feb_2011.html" db-type)]
    (is (= feb-act feb-exp))))




;   (clojure.test/test-vars [#'tests-web-server/every-month])
(deftest every-month
  (console-test  "web-server every-month")
  (one-year USE_FAKE_DB)
;  (one-year USE_MONGER_DB)
 ; (one-year USE_AMAZONICA_DB)
  )

; tests-web-server> (do-tests)
(defn do-tests []
  (web-server-specs)
  (run-tests 'tests-web-server))
