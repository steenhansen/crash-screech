


(ns tests-scrape-html

  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as t])
  (:require [global-consts-vars  :refer :all])
  (:require [crash-sms.data-store :refer :all])
  (:require [crash-sms.sms-event :refer :all])
  (:require [crash-sms.scrape-html :refer :all])
  (:require [crash-sms.html-render  :refer :all])
  (:require [crash-sms.years-months  :refer :all])
  (:require [crash-sms.config-args :refer :all])
  (:require [text-diff :refer [is-html-eq]])
  (:require [java-time :refer [local-date?]])

  (:require [prepare-tests :refer :all]))

(s/fdef count-scrapes
  :args (s/cat :some-html string?))

(s/fdef matching-css-sections
  :args (s/cat :pages-html string?
               :css-match vector?))

(s/fdef matches-accurate
  :args (s/cat :actual-matches integer?
               :wanted-matches integer?))


(s/fdef enough-sections?
  :args (s/cat :the-html string?
               :css-match string?
               :wanted-matches integer?
))

(s/fdef remove-tags
  :args (s/cat :the-html string?))

(s/fdef read-html
  :args (s/cat :check-page string?
               :read-from-web? boolean?))

(s/fdef send-first-day-sms?
  :args (s/cat :my-db-obj map?))

(s/fdef figure-interval?
  :args (s/cat :start-time integer?))

(s/fdef get-db-objs
  :args (s/cat :my-db-obj map?))

(s/fdef first-error-today?
  :args (s/cat :prev-errors-today? boolean?
               :my-db-obj map?))

(s/fdef send-sms-message
  :args (s/cat :prev-errors-today? boolean?
               :my-db-obj map?
:send-hello-sms? boolean?
:sms-send-fn function?
))



(s/fdef scrape-pages-fn
  :args (s/cat :my-db-obj map?
:pages-to-check vector?
:time-fn function?
:sms-send-fn function?
:read-from-web? boolean?
))
(defn scrape-html-specs []
      (print-line "Speccing scrape-html")
      (t/instrument)
      (t/instrument 'count-scrapes)
      (t/instrument 'matching-css-sections)
      (t/instrument 'matches-accurate)
      (t/instrument 'enough-sections?)
      (t/instrument 'remove-tags)
      (t/instrument 'read-html)
      (t/instrument 'send-first-day-sms?)
      (t/instrument 'figure-interval?)
      (t/instrument 'get-db-objs)
      (t/instrument 'first-error-today?)
      (t/instrument 'send-sms-message)
      (t/instrument 'scrape-pages-fn)

;
)

(def ^:const T-COUNT-SCRAPES-HTML " a_countable_scrape a_countable_scrape ")

(deftest test-count-scrapes
  (console-test  "test-count-scrapes"  "scrape-html")
  (let [expected-scrapes 2
        actual-scrapes (count-scrapes T-COUNT-SCRAPES-HTML)]
    (is (= expected-scrapes actual-scrapes))))

(defn unit-sms-send-init [pages-to-check db-type]
  (let [[my-db-obj _ _ sms-data] (build-db T-TEST-COLLECTION
                                           pages-to-check
                                           db-type
                                           TEST-CONFIG-FILE
                                           IGNORE-ENV-VARS)
        purge-table (:purge-table my-db-obj)
        testing-sms? true
        sms-send-fn (build-sms-send sms-data testing-sms?)]
    (defn unit-get-actual-sms [read-from-web?]                   ; side effect, function injected into namespace
      (let [actual-sms (scrape-pages-fn my-db-obj
                                        pages-to-check
                                        instant-time-fn
                                        sms-send-fn
                                        read-from-web?)]
        actual-sms))
    (purge-table)
    sms-data))

(comment
  ; "1\n2\n3"
  (unit-sms-send-fn_error USE_MONGER_DB))
(defn unit-sms-send-fn-error [db-type]
  (console-test  "scrape-html unit-sms-send-fn-error"  db-type)
  (let [fail-to-large 12345678
        pages-to-check [{:check-page WWW-SFFAUDIO-COM
                         :enlive-keys [:article :div.blog-item-wrap],
                         :at-least fail-to-large}]
        sms-data (unit-sms-send-init pages-to-check db-type)   ; side effect, defines (unit-get-actual-sms)
        read-from-web? false
        expected-sms (make-api-call sms-data  SMS-FOUND-ERROR)]
    (let [actual-sms (unit-get-actual-sms read-from-web?)
          [text-diff-1 text-diff-2] (is-html-eq actual-sms expected-sms)]
 [text-diff-1 text-diff-2]
)))

; (clojure.test/test-vars [#'tests-scrape-html/test-sms-send-fn])
(deftest test-sms-send-fn-error
  (console-test  "scrape-html" "test-sms-send-fn-error")
 (let [[text-diff-1 text-diff-2] (unit-sms-send-fn-error  USE_AMAZONICA_DB)]
    (is (= text-diff-1 text-diff-2)))
 (let [[text-diff-1 text-diff-2] (unit-sms-send-fn-error USE_FAKE_DB)]
    (is (= text-diff-1 text-diff-2)))
 (let [[text-diff-1 text-diff-2] (unit-sms-send-fn-error USE_MONGER_DB)]
    (is (= text-diff-1 text-diff-2)))
)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn unit-sms-send-fn-ok [db-type]
 (console-test  "scrape-html" " unit-sms-send-fn-ok"  db-type)
  (let [pass-small 1
        pages-to-check [{:check-page  WWW-SFFAUDIO-COM
                         :enlive-keys [:article :div.blog-item-wrap],
                         :at-least pass-small}]
         sms-data  (unit-sms-send-init pages-to-check db-type)       ; side effect, defines (unit-get-actual-sms)
        read-from-web? false
        expected-sms (make-api-call sms-data SMS-NEW-MONTH)]
    (let [actual-sms (unit-get-actual-sms read-from-web?)
          [text-diff-1 text-diff-2] (is-html-eq actual-sms expected-sms)]
      [text-diff-1 text-diff-2]
      )))

; (clojure.test/test-vars [#'tests-scrape-html/test-sms-send-fn-ok])
(deftest test-sms-send-fn-ok
  (console-test "test-sms-send-fn-ok" "scrape-html")
  (let [[text-diff-1 text-diff-2] (unit-sms-send-fn-ok  USE_AMAZONICA_DB)]
     (is (= text-diff-1 text-diff-2)))
 (let [[text-diff-1 text-diff-2] (unit-sms-send-fn-ok  USE_FAKE_DB)]
    (is (= text-diff-1 text-diff-2)))
  (let [[text-diff-1 text-diff-2] (unit-sms-send-fn-ok USE_MONGER_DB)]
     (is (= text-diff-1 text-diff-2)))
)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defn unit-init_1_2_3_4_months [db-type]
  (console-test "scrape-html" "unit-init_1_2_3_4_months" db-type)
  (let [the-check-pages (make-check-pages 0)
            [my-db-obj _ _ _] (build-db T-TEST-COLLECTION
                                    the-check-pages
                                    db-type
                                    TEST-CONFIG-FILE
                                    IGNORE-ENV-VARS)
        purge-table (:purge-table my-db-obj)
        put-items (:put-items my-db-obj)
        NOV-2000-DATE "2000-11-11-11:11:11.011Z"
        DEC-2000-DATE-0 "2000-12-12-12:12:12.100Z"
        DEC-2000-DATE-1 "2000-12-12-12:12:12.101Z"
        JAN-2001-DATE-0 "2001-01-01-01:01:01.200Z"
        JAN-2001-DATE-1 "2001-01-01-01:01:01.201Z"
        JAN-2001-DATE-2 "2001-01-01-01:01:01.202Z"
        FEB-2001-DATE-0 "2001-02-02-02:02:02.300Z"
        FEB-2001-DATE-1 "2001-02-02-02:02:02.301Z"
        FEB-2001-DATE-2 "2001-02-02-02:02:02.302Z"
        FEB-2001-DATE-3 "2001-02-02-02:02:02.303Z"
        NOV-2000-MONTH "2000-11"    ;0 and 1 record
        DEC-2000-MONTH "2000-12"    ;1 and 2 records
        JAN-2001-MONTH "2001-01"    ;2 and 3 records
        FEB-2001-MONTH "2001-02"    ;3 and 4 records
        MAR-2001-MONTH "2001-03"    ;4 and 0 records
        APR-2001-MONTH "2001-04"    ;0 and 0 records
        test-many [{:the-url WWW-SFFAUDIO-COM :the-date NOV-2000-DATE  }
                   {:the-url WWW-SFFAUDIO-COM :the-date DEC-2000-DATE-0}
                   {:the-url WWW-SFFAUDIO-COM :the-date DEC-2000-DATE-1}
                   {:the-url WWW-SFFAUDIO-COM :the-date JAN-2001-DATE-0}
                   {:the-url WWW-SFFAUDIO-COM :the-date JAN-2001-DATE-1}
                   {:the-url WWW-SFFAUDIO-COM :the-date JAN-2001-DATE-2}
                   {:the-url WWW-SFFAUDIO-COM :the-date FEB-2001-DATE-0 }
                   {:the-url WWW-SFFAUDIO-COM :the-date FEB-2001-DATE-1 }
                   {:the-url WWW-SFFAUDIO-COM :the-date FEB-2001-DATE-2}
                   {:the-url WWW-SFFAUDIO-COM :the-date FEB-2001-DATE-3}]
   _ (purge-table)
   fake-db-records (put-items test-many)]
    (let [under-test? true
          [oct_a_0 nov_a_1] (get-two-months my-db-obj NOV-2000-MONTH under-test?)
          [nov_b_1 dec_b_2] (get-two-months my-db-obj DEC-2000-MONTH under-test?)
          [dec_c_2 jan_c_3] (get-two-months my-db-obj JAN-2001-MONTH under-test?)
          [jan_d_3 feb_d_4] (get-two-months my-db-obj FEB-2001-MONTH under-test?)
          [feb_e_4 mar_e_0] (get-two-months my-db-obj MAR-2001-MONTH under-test?)
          [mar_f_0 apr_f_0] (get-two-months my-db-obj APR-2001-MONTH under-test?)
          actual-counts  [(count oct_a_0) (count nov_a_1) (count nov_b_1) (count dec_b_2) (count dec_c_2) (count jan_c_3)
                          (count jan_d_3) (count feb_d_4) (count feb_e_4) (count mar_e_0) (count mar_f_0) (count apr_f_0)]
          expected-counts [0                1       1       2       2       3
                          3                4       4       0       0       0]]
      [actual-counts expected-counts])))

;   (clojure.test/test-vars [#'tests-scrape-html/mock-month-count])
(deftest mock-month-count
(console-test  "tests-scrape-html fast-month-count")
   (let [[actual-counts expected-counts] (unit-init_1_2_3_4_months USE_FAKE_DB)]
     (is (= actual-counts expected-counts)))
)

;   (clojure.test/test-vars [#'tests-scrape-html/real-month-count])
(deftest real-month-count
  (if (slow-db-tests-allowed)
    (do
  (console-test  "tests-scrape-html real-month-count")
   (let [[actual-counts expected-counts] (unit-init_1_2_3_4_months USE_AMAZONICA_DB)]
     (is (= actual-counts expected-counts)))
   (let [[actual-counts expected-counts] (unit-init_1_2_3_4_months USE_MONGER_DB)]
      (is (= actual-counts expected-counts)))
)))

(defn all-tests []
(reset! *T-REAL-DB-ASSERTIONS* true)
 (reset! *T-ASSERTIONS-VIA-REPL* false)
  (scrape-html-specs)
  (run-tests 'tests-scrape-html)
(reset! *T-ASSERTIONS-VIA-REPL* true)
)

(defn fast-tests []
   (reset! *T-REAL-DB-ASSERTIONS* false)
 (reset! *T-ASSERTIONS-VIA-REPL* false)
(scrape-html-specs)
  (run-tests 'tests-scrape-html)
(reset! *T-ASSERTIONS-VIA-REPL* true))
