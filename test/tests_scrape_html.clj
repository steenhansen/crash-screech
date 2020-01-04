


(ns tests-scrape-html

  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as spec-alpha]
            [clojure.spec.gen.alpha :as spec-gen]
            [clojure.spec.test.alpha :as spec-test])

  (:require [global-consts-vars  :refer :all])

  (:require [crash-sms.choose-db :refer :all])

  (:require [crash-sms.sms-event :refer :all])
  (:require [crash-sms.scrape-html :refer :all])

  (:require [crash-sms.html-render  :refer :all])
  (:require [crash-sms.years-months  :refer :all])
  (:require [crash-sms.config-args :refer :all])
  (:require [text-diff :refer [is-html-eq]])
  (:require [java-time :refer [local-date?]])

  (:require [prepare-tests :refer :all]))

(defn scrape-html-specs []
      (print-line "Speccing scrape-html")
      (spec-test/instrument)
      (spec-test/instrument 'count-scrapes))

(def ^:const T-COUNT-SCRAPES-HTML " a_countable_scrape a_countable_scrape ")

(deftest unit-count-scrapes
  (console-test  "unit-count-scrapes"  "scrape-html")
  (let [expected-scrapes 2
        actual-scrapes (count-scrapes T-COUNT-SCRAPES-HTML)]
    (is (= expected-scrapes actual-scrapes))))

(defn sms-send-init [pages-to-check db-type]
  (let [[my-db-obj _ _ sms-data] (build-db T-TEST-COLLECTION
                                           pages-to-check
                                           db-type
                                           TEST-CONFIG-FILE
                                           IGNORE-ENV-VARS)
        purge-table (:purge-table my-db-obj)
        testing-sms? true
        sms-send-fn (build-sms-send sms-data testing-sms?)]

    (defn get-actual-sms [read-from-web?]
      (let [actual-sms (scrape-pages-fn my-db-obj
                                        pages-to-check
                                        instant-time-fn
                                        sms-send-fn
                                        read-from-web?)]
        actual-sms))
    (purge-table)
    (compact-hash my-db-obj sms-data sms-send-fn)))

(comment
  ; "1\n2\n3"
  (sms-send-fn_error USE_MONGER_DB))
(defn sms-send-fn_error [db-type]
  (console-test  "scrape-html sms-send-fn_error"  db-type)
  (let [fail-to-large 12345678
        pages-to-check [{:check-page WWW-SFFAUDIO-COM   ;;"www.sffaudio.com",
                         :enlive-keys [:article :div.blog-item-wrap],
                         :at-least fail-to-large}]
        {:keys [my-db-obj sms-data sms-send-fn]} (sms-send-init pages-to-check db-type)
        read-from-web? false
        expected-sms (make-api-call sms-data  SMS-FOUND-ERROR)]
    (let [actual-sms (get-actual-sms read-from-web?)
          [text-diff-1 text-diff-2] (is-html-eq actual-sms expected-sms)]
     ; (is (= text-diff-1 text-diff-2))
 [text-diff-1 text-diff-2]
)))

; (clojure.test/test-vars [#'tests-scrape-html/test-1000])
(deftest test-1000
  (console-test  "scrape-html test-1000")
 (let [[text-diff-1 text-diff-2] (sms-send-fn_error  USE_AMAZONICA_DB)]
    (is (= text-diff-1 text-diff-2)))
 (let [[text-diff-1 text-diff-2] (sms-send-fn_error USE_FAKE_DB)]      ; deftest f-
    (is (= text-diff-1 text-diff-2)))
 (let [[text-diff-1 text-diff-2] (sms-send-fn_error USE_MONGER_DB)]
    (is (= text-diff-1 text-diff-2)))
)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn sms-send-fn_ok [db-type]
 (console-test  "scrape-html sms-send-fn_ok"  db-type)
  (let [pass-small 1
        pages-to-check [{:check-page  WWW-SFFAUDIO-COM   ; "www.sffaudio.com",
                         :enlive-keys [:article :div.blog-item-wrap],
                         :at-least pass-small}]
        {:keys [my-db-obj sms-data sms-send-fn]}  (sms-send-init pages-to-check db-type)
        read-from-web? false
       ; expected-sms SMS-NO-ERROR
        expected-sms (make-api-call sms-data SMS-NEW-MONTH)]
    (let [actual-sms (get-actual-sms read-from-web?)
          [text-diff-1 text-diff-2] (is-html-eq actual-sms expected-sms)]
      [text-diff-1 text-diff-2]
      )))

; (clojure.test/test-vars [#'tests-scrape-html/test-1002])
(deftest test-1002
  (console-test  "scrape-html test-1002")
  (let [[text-diff-1 text-diff-2] (sms-send-fn_ok  USE_AMAZONICA_DB)]
     (is (= text-diff-1 text-diff-2)))
 (let [[text-diff-1 text-diff-2] (sms-send-fn_ok  USE_FAKE_DB)]        ;;;; f-
    (is (= text-diff-1 text-diff-2)))
  (let [[text-diff-1 text-diff-2] (sms-send-fn_ok USE_MONGER_DB)]
     (is (= text-diff-1 text-diff-2)))
)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defn init_1_2_3_4_months [db-type]
  (console-test "scrape-html init_1_2_3_4_months" db-type)
  (let [the-check-pages (make-check-pages 0)
            [my-db-obj _ _ _] (build-db T-TEST-COLLECTION
                                    the-check-pages
                                    db-type
                                    TEST-CONFIG-FILE
                                    IGNORE-ENV-VARS)
        purge-table (:purge-table my-db-obj)
        put-items (:put-items my-db-obj)
     ;   get-all (:get-all my-db-obj)
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

        NOV-2000-MONTH "2000-11"    ; 0 and 1 record
        DEC-2000-MONTH "2000-12"    ; 1 and 2 records
        JAN-2001-MONTH "2001-01"    ; 2 and 3 records
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
   fake-db-records (put-items test-many)
]
    (let [are-testing? true    ;; under-test?
          [oct_a_0 nov_a_1] (get-two-months my-db-obj NOV-2000-MONTH are-testing?)
          [nov_b_1 dec_b_2] (get-two-months my-db-obj DEC-2000-MONTH are-testing?)
          [dec_c_2 jan_c_3] (get-two-months my-db-obj JAN-2001-MONTH are-testing?)
          [jan_d_3 feb_d_4] (get-two-months my-db-obj FEB-2001-MONTH are-testing?)
          [feb_e_4 mar_e_0] (get-two-months my-db-obj MAR-2001-MONTH are-testing?)
          [mar_f_0 apr_f_0] (get-two-months my-db-obj APR-2001-MONTH are-testing?)
          actual-counts  [(count oct_a_0) (count nov_a_1) (count nov_b_1) (count dec_b_2) (count dec_c_2) (count jan_c_3)
                          (count jan_d_3) (count feb_d_4) (count feb_e_4) (count mar_e_0) (count mar_f_0) (count apr_f_0)]
          expected-counts [0                1       1       2       2       3
                          3                4       4       0       0       0]]
      [actual-counts expected-counts])))

;   (clojure.test/test-vars [#'tests-scrape-html/fast-month-count])
(deftest fast-month-count
(console-test  "tests-scrape-html fast-month-count")
   (let [[actual-counts expected-counts] (init_1_2_3_4_months USE_FAKE_DB)]
     (is (= actual-counts expected-counts)))
)

;   (clojure.test/test-vars [#'tests-scrape-html/real-month-count])
(deftest real-month-count

  (if (execute-tests)
    (do
  (console-test  "tests-scrape-html real-month-count")
   (let [[actual-counts expected-counts] (init_1_2_3_4_months USE_AMAZONICA_DB)]
     (is (= actual-counts expected-counts)))
   (let [[actual-counts expected-counts] (init_1_2_3_4_months USE_MONGER_DB)]
      (is (= actual-counts expected-counts)))
)))

(defn do-tests []
(reset! *run-all-tests* true)
 (reset! *testing-namespace* "fast-all-tests-running")
  (scrape-html-specs)
  (run-tests 'tests-scrape-html)
(reset! *testing-namespace* "no-tests-running")
)

(defn fast-tests []
   (reset! *run-all-tests* false)
 (reset! *testing-namespace* "fast-all-tests-running")
(scrape-html-specs)
  (run-tests 'tests-scrape-html)
(reset! *testing-namespace* "no-tests-running"))
