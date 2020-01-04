(ns tests-sms-event
  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as spec-alpha]
            [clojure.spec.gen.alpha :as spec-gen]
            [clojure.spec.test.alpha :as spec-test])
  (:require [global-consts-vars  :refer :all])
  (:require [crash-sms.sms-event :refer :all])
  (:require [crash-sms.years-months :refer [adjusted-date]])
  (:require [crash-sms.scrape-html :refer :all])
  (:require [crash-sms.choose-db :refer :all])
  (:require [java-time :refer [local-date?]])
  (:require [prepare-tests :refer :all])
  (:require [text-diff :refer [is-html-eq]]))

(defn sms-event-specs []
      (print-line "Speccing sms-event")
      (spec-test/instrument)
      (spec-test/instrument 'make-api-call)
      (spec-test/instrument 'build-sms-send)
      (spec-test/instrument 'sms-to-phones)
      (spec-test/instrument 'build-web-scrape))

(def ^:const TEST-SMS-MAP-SHORT
  {:till-url "https://platform.tillmobile.com/api/send?username=abcdefghijklmnopqrstuvwxyz1234&api_key=1234567890abcdefghijklmnopqrstuvwxyz1234"
   :sms-params {:form-params {:phone ["12345678901" "12345678901" "12345678901"],
                              :text "my-test-mess - https://fathomless-woodland-85635.herokuapp.com/"}
                :content-type :json}})

;   (clojure.test/test-vars [#'tests-sms-event/unit-make-api-call])
(deftest unit-make-api-call
  (console-test "tests-sms-event unit-make-api-call" "sms-event")
  (let [the-check-pages (make-check-pages 0)
        [_ _ _ sms-data] (build-db T-TEST-COLLECTION
                                   the-check-pages
                                   USE_MONGER_DB
                                   TEST-CONFIG-FILE
                                   USE_ENVIRONMENT)
        testing-sms? true
        test-sms (make-api-call sms-data "my-test-mess")]
    (is (= TEST-SMS-MAP-SHORT  test-sms))))

(def ^:const TEST-SMS-MAP-LONG
  {:till-url "https://platform.tillmobile.com/api/send?username=abcdefghijklmnopqrstuvwxyz1234&api_key=1234567890abcdefghijklmnopqrstuvwxyz1234"
   :sms-params {:form-params {:phone ["12345678901" "12345678901" "12345678901"],
                              :text "Test sms call - https://fathomless-woodland-85635.herokuapp.com/"}
                :content-type :json}
;; not expected   :testing-sms? true
   })

(deftest unit-build-sms-send
  (console-test "test-sms-event unit-build-sms-send")
  (let [the-check-pages (make-check-pages 0)
        [_ _ _ sms-data] (build-db T-TEST-COLLECTION
                                   the-check-pages
                                   USE_MONGER_DB
                                   TEST-CONFIG-FILE
                                   USE_ENVIRONMENT)
        testing-sms? true
        test-sms (sms-to-phones sms-data testing-sms?)
        [text-diff-1 text-diff-2] (is-html-eq TEST-SMS-MAP-LONG test-sms)]
    (is (= text-diff-1 text-diff-2))))

;   (clojure.test/test-vars [#'tests-sms-server/unit-sms-to-phones])
(deftest unit-sms-to-phones
  (console-test "test-sms-event unit-build-sms-send")
  (let [the-check-pages (make-check-pages 0)
        [_ _ _ sms-data] (build-db T-TEST-COLLECTION
                                   the-check-pages
                                   USE_MONGER_DB
                                   TEST-CONFIG-FILE
                                   USE_ENVIRONMENT)
        testing-sms? true
        test-sms (sms-to-phones sms-data testing-sms?)
        [text-diff-1 text-diff-2] (is-html-eq TEST-SMS-MAP-LONG test-sms)]
    (is (= text-diff-1 text-diff-2))))

;   (clojure.test/test-vars [#'tests-sms-server/unit-sms-to-phones])
(deftest unit-sms-to-phones
  (console-test "test-sms-event unit-sms-to-phones")
  (let [the-check-pages (make-check-pages 0)
        [_ _ _ sms-data] (build-db T-TEST-COLLECTION
                                   the-check-pages
                                   USE_MONGER_DB
                                   TEST-CONFIG-FILE
                                   USE_ENVIRONMENT)
        testing-sms? true
        test-sms (sms-to-phones sms-data testing-sms?)]
    (is (= TEST-SMS-MAP-LONG  test-sms))))

(deftest unit-build-web-scrape
  (console-test "test-sms-event unit-build-web-scrape")
  (let [the-check-pages (make-check-pages 0)
        [my-db-obj _ _ sms-data] (build-db T-TEST-COLLECTION
                                           the-check-pages
                                           USE_MONGER_DB
                                           TEST-CONFIG-FILE
                                           USE_ENVIRONMENT)
        testing-sms? true
        web-scraper (build-web-scrape scrape-pages-fn my-db-obj the-check-pages sms-data testing-sms? (adjusted-date "2019-07-04T04:18:46.173Z"))]
    (is (function? web-scraper))))

(defn do-tests []
  (reset! *run-all-tests* true)
  (reset! *testing-namespace* "fast-all-tests-running")
  (sms-event-specs)
  (run-tests 'tests-sms-event)
  (reset! *testing-namespace* "no-tests-running"))

(defn fast-tests []
  (reset! *run-all-tests* false)
  (reset! *testing-namespace* "fast-all-tests-running")
  (sms-event-specs)
  (run-tests 'tests-sms-event)
  (reset! *testing-namespace* "no-tests-running"))
