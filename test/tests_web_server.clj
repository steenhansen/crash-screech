
(ns tests-web-server
  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as s]

            [clojure.spec.test.alpha :as t])
  (:require [global-consts-vars  :refer :all])
  (:require [crash-sms.data-store :refer [build-db]])
  (:require [crash-sms.web-server :refer :all])
  (:require [crash-sms.sms-event :refer [build-sms-send build-web-scrape]])
  (:require [crash-sms.scrape-html :refer [scrape-pages-fn]])
  (:require [crash-sms.years-months :refer [date-with-now-time-fn instant-time-fn]])
  (:require [java-time :refer [local-date?]])
  (:require [text-diff :refer [is-html-eq]])
  (:require [prepare-tests :refer :all]))

(s/fdef show-data
  :args (s/alt :binary (s/cat :my-db-obj map?
                              :the-date-func function?)
               :ternary (s/cat :my-db-obj map?
                               :the-date-func function?
                               :under-test? boolean?)))

(s/fdef show-data-cron
  :args (s/cat :my-db-obj map?
               :the-uri string?
               :cron-url string?
               :web-scraper-fn function?
               :the-date-func function?
               :under-test? boolean?))

(s/fdef build-express-serve
  :args (s/cat :web-scraper-fn function?
               :my-db-obj map?
               :cron-url string?
               :sms-data :sms-data?/test-specs  :sms-data :sms-data?/test-specs
               :under-test? boolean?
               :the-date-func function?))

(s/fdef web-reload
  :args (s/cat))

(s/fdef web-init
  :args (s/cat :server-port integer?
               :express-server-fn function?))

(defn web-server-specs []
  (print-line "Speccing web-server")
  (t/instrument)
  (t/instrument 'show-data)
  (t/instrument 'show-data-cron)
  (t/instrument 'build-express-serve)
  (t/instrument 'web-reload)
  (t/instrument 'web-init))

(defn unit-html-correct [db-type]
  (console-test "web-server unit-html-correct" db-type)
  (let [the-check-pages (make-check-pages 0)
        [my-db-obj _ cron-url sms-data] (build-db T-TEST-COLLECTION
                                                  the-check-pages
                                                  db-type
                                                  TEST-CONFIG-FILE
                                                  IGNORE-ENV-VARS)
        purge-table (:purge-table my-db-obj)
        _ (purge-table)
        testing-sms? true
        test-date-func (date-with-now-time-fn "2017-05-31")
        web-scraper-fn (build-web-scrape scrape-pages-fn my-db-obj the-check-pages sms-data testing-sms? test-date-func)
        under-test? true
        express-server-fn (build-express-serve web-scraper-fn my-db-obj cron-url sms-data under-test? test-date-func)
        send-test-sms-url (:send-test-sms-url sms-data)
        web-page (express-server-fn  {:uri cron-url})
        the-body (:body web-page)
        file-expected (slurp  (str SCRAPED-TEST-DATA "tests_web_server_all_correct.html"))
        conformed-actual (conform-whitespace the-body)
        conformed-expected (conform-whitespace file-expected)]
    (let [[text-diff-1 text-diff-2] (is-html-eq conformed-actual conformed-expected)]
      [text-diff-1 text-diff-2])))

;   (clojure.test/test-vars [#'tests-web-server/mock-html-correct])
(deftest mock-db-html-correct
  (console-test  "tests-web-server mock-db-html-correct")
  (let [[text-diff-1 text-diff-2] (unit-html-correct  USE_FAKE_DB)]
    (is (= text-diff-1 text-diff-2))))

;   (clojure.test/test-vars [#'tests-web-server/real-html-correct])
(deftest real-db-html-correct
  (if (slow-db-tests-allowed)
    (do
      (console-test  "tests-web-server real-db-html-correct")
      (let [[text-diff-1 text-diff-2] (unit-html-correct  USE_MONGER_DB)]
        (is (= text-diff-1 text-diff-2)))

      (let [[text-diff-1 text-diff-2] (unit-html-correct  USE_AMAZONICA_DB)]
        (is (= text-diff-1 text-diff-2))))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defn unit-html-wrong [db-type]
  (console-test  "web-server unit-html-wrong" db-type)
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
        test-date-func (date-with-now-time-fn "2019-10-01")
        web-scraper-fn (build-web-scrape scrape-pages-fn my-db-obj the-check-pages sms-data testing-sms? test-date-func)
        under-test? true
        express-server-fn (build-express-serve web-scraper-fn my-db-obj cron-url sms-data under-test? test-date-func)
        send-test-sms-url (:send-test-sms-url sms-data)
        web-page (express-server-fn  {:uri cron-url})
        the-body (:body web-page)

        file-expected (slurp  (str SCRAPED-TEST-DATA "tests_web_server_all_wrong.html"))
        conformed-actual (conform-whitespace the-body)
        conformed-expected (conform-whitespace file-expected)]
    (let [[text-diff-1 text-diff-2] (is-html-eq conformed-actual conformed-expected)]
      [text-diff-1 text-diff-2] ;;;(is (= text-diff-1 text-diff-2))
      )))

;   (clojure.test/test-vars [#'tests-web-server/mock-db-html-wrong])
(deftest mock-db-html-wrong
  (console-test  "test-web-server mock-html-wrong")
  (let [[text-diff-1 text-diff-2] (unit-html-wrong  USE_FAKE_DB)]
    (is (= text-diff-1 text-diff-2))))

;   (clojure.test/test-vars [#'tests-web-server/real-db-html-wrong])
(deftest real-db-html-wrong
  (if (slow-db-tests-allowed)
    (do
      (console-test  "web-server real-html-wrong")
      (let [[text-diff-1 text-diff-2] (unit-html-wrong  USE_MONGER_DB)]
        (is (= text-diff-1 text-diff-2)))

      (let [[text-diff-1 text-diff-2] (unit-html-wrong  USE_AMAZONICA_DB)]
        (is (= text-diff-1 text-diff-2))))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn unit-save-record [yyyy-mm-dd my-db-obj sms-data db-type]
  (console-test  "web-server unit-save-record" db-type yyyy-mm-dd)
  (let [sff-audio [{:check-page WWW-SFFAUDIO-COM
                    :enlive-keys SFFAUDIO-CHECK-KEYS
                    :at-least SFFAUDIO-AMOUNT}]
        testing-sms? true
        record-date (date-with-now-time-fn yyyy-mm-dd)
        scrape-web (build-web-scrape scrape-pages-fn my-db-obj sff-audio sms-data testing-sms? record-date)]
    (scrape-web)))

(defn unit-make-pair [last-month-date this-month-date html-test-file db-type]
 (console-test  "web-server"  "unit-make-pair" db-type)
  (let [under-test? true
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
        _  (unit-save-record last-month-date my-db-obj sms-data db-type)
        _  (unit-save-record this-month-date my-db-obj sms-data db-type)
        test-date-func (date-with-now-time-fn this-month-date)
        web-scraper (fn [] ())
        express-server-fn (build-express-serve web-scraper my-db-obj cron-url sms-data under-test? test-date-func)
        send-test-sms-url (:send-test-sms-url sms-data)
        web-page (express-server-fn  {:uri cron-url})
        the-body (:body web-page)
        file-expected (slurp  (str SCRAPED-TEST-DATA html-test-file))
        conformed-actual (conform-whitespace the-body)
        conformed-expected (conform-whitespace file-expected)]
    (let [[text-diff-1 text-diff-2] (is-html-eq conformed-actual conformed-expected)]
      [text-diff-1 text-diff-2])))

(defn unit-one-year [db-type]
  (console-test  "web-server unit-one-year" db-type)
  (let [[jan-act jan-exp] (unit-make-pair "2009-12-01" "2010-01-01" "tests_01_web_server_jan_2010.html" db-type)]
    (is (= jan-act jan-exp)))
  (let [[feb-act feb-exp] (unit-make-pair "2010-01-01" "2010-02-01" "tests_02_web_server_feb_2010.html" db-type)]
    (is (= feb-act feb-exp)))
  (let [[mar-act mar-exp] (unit-make-pair "2010-02-01" "2010-03-01" "tests_03_web_server_mar_2010.html" db-type)]
    (is (= mar-act mar-exp)))
  (let [[apr-act apr-exp] (unit-make-pair "2010-03-01" "2010-04-01" "tests_04_web_server_apr_2010.html" db-type)]
    (is (= apr-act apr-exp)))
  (let [[may-act may-exp] (unit-make-pair "2010-04-01" "2010-05-01" "tests_05_web_server_may_2010.html" db-type)]
    (is (= may-act may-exp)))
  (let [[jun-act jun-exp] (unit-make-pair "2010-05-01" "2010-06-01" "tests_06_web_server_jun_2010.html" db-type)]
    (is (= jun-act jun-exp)))
  (let [[jul-act jul-exp] (unit-make-pair "2010-06-01" "2010-07-01" "tests_07_web_server_jul_2010.html" db-type)]
    (is (= jul-act jul-exp)))
  (let [[aug-act aug-exp] (unit-make-pair "2010-07-01" "2010-08-01" "tests_08_web_server_aug_2010.html" db-type)]
    (is (= aug-act aug-exp)))
  (let [[sep-act sep-exp] (unit-make-pair "2010-08-01" "2010-09-01" "tests_09_web_server_sep_2010.html" db-type)]
    (is (= sep-act sep-exp)))
  (let [[oct-act oct-exp] (unit-make-pair "2010-09-01" "2010-10-01" "tests_10_web_server_oct_2010.html" db-type)]
    (is (= oct-act oct-exp)))
  (let [[nov-act nov-exp] (unit-make-pair "2010-10-01" "2010-11-01" "tests_11_web_server_nov_2010.html" db-type)]
    (is (= nov-act nov-exp)))
  (let [[dec-act dec-exp] (unit-make-pair "2010-11-01" "2010-12-01" "tests_12_web_server_dec_2010.html" db-type)]
    (is (= dec-act dec-exp)))
  (let [[jan-act jan-exp] (unit-make-pair "2010-12-01" "2011-01-01" "tests_13_web_server_jan_2011.html" db-type)]
    (is (= jan-act jan-exp)))
  (let [[feb-act feb-exp] (unit-make-pair "2011-01-01" "2011-02-01" "tests_14_web_server_feb_2011.html" db-type)]
    (is (= feb-act feb-exp))))

;   (clojure.test/test-vars [#'tests-web-server/mock-one-year])
(deftest mock-db-one-year
  (console-test  "test-web-server mock-one-year")
  (unit-one-year USE_FAKE_DB))

;   (clojure.test/test-vars [#'tests-web-server/real-one-year])
(deftest real-db-one-year
  (if (slow-db-tests-allowed)
    (do
      (console-test  "web-server real-one-year")
      (unit-one-year USE_MONGER_DB)
      (unit-one-year USE_AMAZONICA_DB))))

(defn all-tests []
  (reset! *T-REAL-DB-ASSERTIONS* true)
  (reset! *T-ASSERTIONS-VIA-REPL* false)
  (web-server-specs)
  (run-tests 'tests-web-server)
  (reset! *T-ASSERTIONS-VIA-REPL* true))

(defn fast-tests []
  (reset! *T-REAL-DB-ASSERTIONS* false)
  (reset! *T-ASSERTIONS-VIA-REPL* false)
  (web-server-specs)
  (run-tests 'tests-web-server)
  (reset! *T-ASSERTIONS-VIA-REPL* true))
