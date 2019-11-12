


(ns tests-scrape-html

  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as spec-alpha]
            [clojure.spec.gen.alpha :as spec-gen]
            [clojure.spec.test.alpha :as spec-test])

  (:require [global-consts-vars  :refer :all])

  (:require [crash-screech.choose-db :refer :all])

  (:require [crash-screech.sms-event :refer :all])
  (:require [crash-screech.scrape-html :refer :all])

  (:require [crash-screech.html-render  :refer :all])
  (:require [crash-screech.years-months  :refer :all])
  (:require [crash-screech.config-args :refer :all])

  (:require [java-time :refer [local-date?]])

  (:require [prepare-tests :refer :all]))

;(load "spec-types/shared-types")
;(load "spec-types/scrape-html-specs")


(defn scrape-html-specs []
  (if RUN-SPEC-TESTS
    (do
    (println "Speccing html-specs")
      (spec-test/instrument)
      (spec-test/instrument 'count-scrapes))))

(def ^:const T-COUNT-SCRAPES-HTML " a_countable_scrape a_countable_scrape ")

(deftest unit-count-scrapes
  (testing "test-count-scrapes : ssssssss "
    (console-test  "unit-count-scrapes"  "scrape-html")
    (let [expected-scrapes 2
          actual-scrapes (count-scrapes T-COUNT-SCRAPES-HTML)]
      (is (= expected-scrapes actual-scrapes)))))







(defn sms-send-init [pages-to-check db-type]
  (let [[my-db-obj _ _ sms-data] (build-db T-DB-TEST-NAME
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
;(println "AAAA in get-actual-sms " actual-sms)
        actual-sms))

    (purge-table)
    (compact-hash my-db-obj sms-data sms-send-fn)))

(defn sms-send-fn_error [db-type]
  (let [fail-to-large 12345678
        pages-to-check [{:check-page "www.sffaudio.com",
                         :enlive-keys [:article :div.blog-item-wrap],
                         :at-least fail-to-large}]
        {:keys [my-db-obj sms-data sms-send-fn]} (sms-send-init pages-to-check db-type)
        read-from-web? false
        expected-sms (make-api-call sms-data SMS-FOUND-ERROR)]
    (let [actual-sms (get-actual-sms read-from-web?)]
;(println "BBBB expected " expected-sms)
;(println "CCCC   actual " actual-sms)
      (is (= expected-sms actual-sms)))))



 (deftest test-1000
   (testing "test-1000 :amazonica-db should send an sms message in sms-send-fn"
     (sms-send-fn_error "amazonica-db")))

 (deftest test-1001
   (testing "test-1001 :monger-db should send an sms message in sms-send-fn"
     (sms-send-fn_error "monger-db")))




(defn sms-send-fn_ok [db-type]
  (let [pass-small 1
        pages-to-check [{:check-page "www.sffaudio.com",
                         :enlive-keys [:article :div.blog-item-wrap],
                         :at-least pass-small}]
        {:keys [my-db-obj sms-data sms-send-fn]}  (sms-send-init pages-to-check db-type)
        read-from-web? false
       ; expected-sms SMS-NO-ERROR
 expected-sms (make-api-call sms-data SMS-NEW-MONTH)
]
    (let [actual-sms (get-actual-sms read-from-web?)]
;(println "XXXX expected " expected-sms)
;(println "VVVV   actual " actual-sms)
      (is (= expected-sms actual-sms)))))


 (deftest test-1002
   (testing "test-1002 :amazonica-db should send an sms message in sms-send-fn"
     (sms-send-fn_ok "amazonica-db")))

 (deftest test-1003
   (testing "test-1003 :monger-db should send an sms message in sms-send-fn"
     (sms-send-fn_ok "monger-db")))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defn init_1_2_3_4_months [db-type]
  (let [[my-db-obj _ _ _] (build-db T-DB-TEST-NAME
                                    []
                                    db-type
                                    TEST-CONFIG-FILE
                                    IGNORE-ENV-VARS)
        purge-table (:purge-table my-db-obj)
        put-items (:put-items my-db-obj)
     ;   get-all (:get-all my-db-obj)
        NOV-2000-DATE "2000-11-11-11:11:11.011Z"
        DEC-2000-DATE "2000-12-12-12:12:12.012Z"
        JAN-2001-DATE "2001-01-01-01:01:01.001Z"
        FEB-2001-DATE "2001-02-02-02:02:02.002Z"

        NOV-2000-MONTH "2000-11"    ; 0 and 1 record
        DEC-2000-MONTH "2000-12"    ; 1 and 2 records
        JAN-2001-MONTH "2001-01"    ; 2 and 3 records
        FEB-2001-MONTH "2001-02"    ;3 and 4 records
        MAR-2001-MONTH "2001-03"    ;4 and 0 records
        APR-2001-MONTH "2001-04"    ;0 and 0 records
        test-many [{:the-url "november-1", :the-date NOV-2000-DATE}

                   {:the-url "december-2", :the-date DEC-2000-DATE}
                   {:the-url "december-2", :the-date DEC-2000-DATE}

                   {:the-url "january-3",  :the-date JAN-2001-DATE}
                   {:the-url "january-3",  :the-date JAN-2001-DATE}
                   {:the-url "january-3",  :the-date JAN-2001-DATE}

                   {:the-url "february-4", :the-date FEB-2001-DATE}
                   {:the-url "february-4", :the-date FEB-2001-DATE}
                   {:the-url "february-4", :the-date FEB-2001-DATE}
                   {:the-url "february-4", :the-date FEB-2001-DATE}]]
    (purge-table)
    (put-items test-many)
    (let [[oct_a_0 nov_a_1] (get-two-months my-db-obj NOV-2000-MONTH)
          [nov_b_1 dec_b_2] (get-two-months my-db-obj DEC-2000-MONTH)
          [dec_c_2 jan_c_3] (get-two-months my-db-obj JAN-2001-MONTH)
          [jan_d_3 feb_d_4] (get-two-months my-db-obj FEB-2001-MONTH)
          [feb_e_4 mar_e_0] (get-two-months my-db-obj MAR-2001-MONTH)
          [mar_f_0 apr_f_0] (get-two-months my-db-obj APR-2001-MONTH)
          actual-counts  [(count oct_a_0) (count nov_a_1) (count nov_b_1) (count dec_b_2) (count dec_c_2) (count jan_c_3)
                          (count jan_d_3) (count feb_d_4) (count feb_e_4) (count mar_e_0) (count mar_f_0) (count apr_f_0)]
          epected-counts [0                1       1       2       2       3
                          3                4       4       0       0       0]]
      (is (= actual-counts epected-counts)))))

(deftest test-2000
  (testing "test-2000 :monger-db should send an sms message in sms-send-fn"
    (init_1_2_3_4_months "monger-db")))

(deftest test-2001
  (testing "test-2000 :monger-db should send an sms message in sms-send-fn"
    (init_1_2_3_4_months "monger-db")))




(defn do-tests []
 (scrape-html-specs)
  (run-tests 'tests-scrape-html))






