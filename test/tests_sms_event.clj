(ns tests-sms-event
  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as spec-alpha]
            [clojure.spec.gen.alpha :as spec-gen]
            [clojure.spec.test.alpha :as spec-test])
  (:require [global-consts-vars  :refer :all])
  (:require [crash-screech.sms-event :refer :all])
  (:require [crash-screech.years-months :refer [adjusted-date]])
  (:require [crash-screech.scrape-html :refer :all])
  (:require [crash-screech.choose-db :refer :all])
  (:require [java-time :refer [local-date?]])
  (:require [prepare-tests :refer :all])

  (:require [text-diff :refer [is-html-eq]])
)

;(load "spec-types/shared-types")
;(load "spec-types/sms-event-specs")


(defn sms-event-specs []
  (if RUN-SPEC-TESTS
    (do
(println "Speccing sms-event")
      (spec-test/instrument)
(spec-test/instrument 'make-api-call)
(spec-test/instrument 'build-sms-send)
(spec-test/instrument 'sms-to-phones)
(spec-test/instrument 'build-web-scrape))))

(def ^:const TEST-SMS-MAP-SHORT
  {:till-url "https://platform.tillmobile.com/api/send?username=abcdefghijklmnopqrstuvwxyz1234&api_key=1234567890abcdefghijklmnopqrstuvwxyz1234"
   :sms-params {:form-params {:phone ["12345678901" "12345678901" "12345678901"],
                              :text "my-test-mess - https://fathomless-woodland-85635.herokuapp.com/"}
                :content-type :json}})

(deftest unit-make-api-call
  (testing "test-adjusted-date : java.date to YYYY-MM like 1999-12 if it is 2000-1-1 "
    (console-test "unit-make-api-call" "sms-event")

    (let [db-type USE_MONGER_DB
          environment-utilize USE_ENVIRONMENT
the-check-pages (make-check-pages 0)
          [_ _ _ sms-data] (build-db T-TEST-COLLECTION
                                     the-check-pages
                                     db-type
                                     TEST-CONFIG-FILE
                                     environment-utilize)
          testing-sms? true
          test-sms (make-api-call sms-data "my-test-mess")]
      (is (= TEST-SMS-MAP-SHORT  test-sms)))))

(def ^:const TEST-SMS-MAP-LONG
  {:till-url "https://platform.tillmobile.com/api/send?username=abcdefghijklmnopqrstuvwxyz1234&api_key=1234567890abcdefghijklmnopqrstuvwxyz1234"
   :sms-params {:form-params {:phone ["12345678901" "12345678901" "12345678901"],
                              :text "Test sms call - https://fathomless-woodland-85635.herokuapp.com/"}
                :content-type :json}
;; not expected   :testing-sms? true
})

(deftest unit-build-sms-send
  (testing "test-adjusted-date : java.date to YYYY-MM like 1999-12 if it is 2000-1-1 "
    (console-test "unit-build-sms-send" "sms-event")

    (let [db-type USE_MONGER_DB
          environment-utilize USE_ENVIRONMENT
 the-check-pages (make-check-pages 0)
          [_ _ _ sms-data] (build-db T-TEST-COLLECTION
                                     the-check-pages
                                     db-type
                                     TEST-CONFIG-FILE
                                     environment-utilize)
          testing-sms? true
          test-sms (sms-to-phones sms-data testing-sms?)

 [text-diff-1 text-diff-2] (is-html-eq TEST-SMS-MAP-LONG test-sms)]
      (is (= text-diff-1 text-diff-2))



;;      (is (= TEST-SMS-MAP-LONG  test-sms))
)))

(deftest unit-sms-to-phones
  (testing "test-adjusted-date : java.date to YYYY-MM like 1999-12 if it is 2000-1-1 "
    (console-test "unit-sms-to-phones" "sms-event")

    (let [db-type USE_MONGER_DB
          environment-utilize USE_ENVIRONMENT
the-check-pages (make-check-pages 0)
          [_ _ _ sms-data] (build-db T-TEST-COLLECTION
                                     the-check-pages
                                     db-type
                                     TEST-CONFIG-FILE
                                     environment-utilize)
          testing-sms? true
          test-sms (sms-to-phones sms-data testing-sms?)]

      (is (= TEST-SMS-MAP-LONG  test-sms)))))

(deftest unit-build-web-scrape
  (testing "test-adjusted-date : java.date to YYYY-MM like 1999-12 if it is 2000-1-1 "
    (console-test "unit-build-web-scrape" "sms-event")

    (let [db-type USE_MONGER_DB
          environment-utilize USE_ENVIRONMENT
the-check-pages (make-check-pages 0)
          [my-db-obj _ _ sms-data] (build-db T-TEST-COLLECTION
                                             the-check-pages
                                             db-type
                                             TEST-CONFIG-FILE
                                             environment-utilize)
          testing-sms? true
          temporize-func (build-web-scrape scrape-pages-fn my-db-obj the-check-pages sms-data testing-sms? (adjusted-date "2019-07-04T04:18:46.173Z"))]
      (is (function? temporize-func)))))


(defn do-tests []
 (sms-event-specs)
  (run-tests 'tests-sms-event))
