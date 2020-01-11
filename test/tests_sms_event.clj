(ns tests-sms-event
  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as s]

            [clojure.spec.test.alpha :as t])
  (:require [global-consts-vars  :refer :all])
  (:require [crash-sms.sms-event :refer :all])
  (:require [crash-sms.years-months :refer [adjusted-date]])
  (:require [crash-sms.scrape-html :refer :all])
  (:require [crash-sms.data-store :refer :all])
  (:require [java-time :refer [local-date?]])
  (:require [prepare-tests :refer :all])
  (:require [text-diff :refer [is-html-eq]]))

(s/fdef make-api-call
  :args (s/cat :sms-data function?
               :testing-sms? boolean?))

(s/fdef build-sms-send
  :args (s/cat :sms-data function?
               :testing-sms? boolean?))

(s/fdef sms-to-phones
  :args (s/cat :sms-data function?
               :testing-sms? boolean?))

(s/fdef build-web-scrape
  :args (s/cat :scrape-pages-fn function?
               :my-db-obj map?
               :pages-to-check vector?
               :sms-data :sms-data?/test-specs
               :under-test? boolean?
               :date-time-fn function?))

(defn sms-event-specs []
  (print-line "Speccing sms-event")
  (t/instrument)
  (t/instrument 'make-api-call)
  (t/instrument 'build-sms-send)
  (t/instrument 'sms-to-phones)
  (t/instrument 'build-web-scrape))

(def ^:const TEST-SMS-MAP-SHORT
  {:till-url "https://platform.tillmobile.com/api/send?username=abcdefghijklmnopqrstuvwxyz1234&api_key=1234567890abcdefghijklmnopqrstuvwxyz1234"
   :sms-params {:form-params {:phone ["12345678901" "12345678901" "12345678901"],
                              :text "my-test-mess - https://fathomless-woodland-85635.herokuapp.com/"}
                :content-type :json}})

;   (clojure.test/test-vars [#'tests-sms-event/test-make-api-call])
(deftest test-make-api-call
  (console-test "sms-event test-make-api-call" "sms-event")
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
                :content-type :json}})

(deftest test-build-sms-send
  (console-test "sms-event test-build-sms-send")
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

;   (clojure.test/test-vars [#'tests-sms-server/test-sms-to-phones])
(deftest test-sms-to-phones
  (console-test "sms-event test-build-sms-send")
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

;   (clojure.test/test-vars [#'tests-sms-server/test-sms-to-phones])
(deftest test-sms-to-phones
  (console-test "sms-event test-sms-to-phones")
  (let [the-check-pages (make-check-pages 0)
        [_ _ _ sms-data] (build-db T-TEST-COLLECTION
                                   the-check-pages
                                   USE_MONGER_DB
                                   TEST-CONFIG-FILE
                                   USE_ENVIRONMENT)
        testing-sms? true
        test-sms (sms-to-phones sms-data testing-sms?)]
    (is (= TEST-SMS-MAP-LONG  test-sms))))

(deftest test-build-web-scrape
  (console-test "sms-event test-build-web-scrape")
  (let [the-check-pages (make-check-pages 0)
        [my-db-obj _ _ sms-data] (build-db T-TEST-COLLECTION
                                           the-check-pages
                                           USE_MONGER_DB
                                           TEST-CONFIG-FILE
                                           USE_ENVIRONMENT)
        testing-sms? true
        web-scraper-fn (build-web-scrape scrape-pages-fn my-db-obj the-check-pages sms-data testing-sms? (adjusted-date "2019-07-04T04:18:46.173Z"))]
    (is (function? web-scraper-fn))))

(defn all-tests []
  (reset! *T-REAL-DB-ASSERTIONS* true)
  (reset! *T-ASSERTIONS-VIA-REPL* false)
  (sms-event-specs)
  (run-tests 'tests-sms-event)
  (reset! *T-ASSERTIONS-VIA-REPL* true))

(defn fast-tests []
  (reset! *T-REAL-DB-ASSERTIONS* false)
  (reset! *T-ASSERTIONS-VIA-REPL* false)
  (sms-event-specs)
  (run-tests 'tests-sms-event)
  (reset! *T-ASSERTIONS-VIA-REPL* true))
