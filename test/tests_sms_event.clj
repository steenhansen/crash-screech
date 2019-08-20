(ns tests-sms-event
  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as spec-alpha]
            [clojure.spec.gen.alpha :as spec-gen]
            [clojure.spec.test.alpha :as spec-test])
  (:require [global-consts  :refer :all])
  (:require [global-vars  :refer :all])
  (:require [crash-screech.sms-event :refer :all])
  (:require [crash-screech.scrape-html :refer :all])
  (:require [crash-screech.choose-db :refer :all])
  (:require [java-time :refer [local-date?]])
  (:require [prepare-tests :refer :all])
  (:require [spec-calls :refer :all]))

(spec-test/instrument 'make-api-call)
(spec-test/instrument 'build-sms-send)
(spec-test/instrument 'sms-to-phones)
(spec-test/instrument 'single-cron-fn)

(def ^:const TEST-SMS-MAP-SHORT
  {:till-url "https://platform.tillmobile.com/api/send?username=abcdefghijklmnopqrstuvwxyz1234&api_key=1234567890abcdefghijklmnopqrstuvwxyz1234"
   :sms-params {:form-params {:phone ["12345678901" "12345678901" "12345678901"],
                              :text "my-test-mess - https://fathomless-woodland-85635.herokuapp.com/"}
                :content-type :json}})

(deftest uni-make-api-call
  (testing "test-adjusted-date : java.date to YYYY-MM like 1999-12 if it is 2000-1-1 "
    (console-test "uni-make-api-call" "sms-event")

    (let [db-type "monger-db"
          environment-utilize "use-environment"
          [_ _ _ sms-data] (build-db DB-TABLE-NAME
                                     THE-CHECK-PAGES
                                     db-type
                                     TEST-CONFIG-FILE
                                     environment-utilize)
          testing-sms? true
          test-sms (make-api-call sms-data "my-test-mess")]
      (is (= TEST-SMS-MAP-SHORT  test-sms)))))

(def ^:const TEST-SMS-MAP-LONG
  {:till-url "https://platform.tillmobile.com/api/send?username=abcdefghijklmnopqrstuvwxyz1234&api_key=1234567890abcdefghijklmnopqrstuvwxyz1234"
   :sms-params {:form-params {:phone ["12345678901" "12345678901" "12345678901"],
                              :text "test sms call - https://fathomless-woodland-85635.herokuapp.com/"}
                :content-type :json}
   :testing-sms? true})

(deftest uni-build-sms-send
  (testing "test-adjusted-date : java.date to YYYY-MM like 1999-12 if it is 2000-1-1 "
    (console-test "uni-build-sms-send" "sms-event")

    (let [db-type "monger-db"
          environment-utilize "use-environment"
          [_ _ _ sms-data] (build-db DB-TABLE-NAME
                                     THE-CHECK-PAGES
                                     db-type
                                     TEST-CONFIG-FILE
                                     environment-utilize)
          testing-sms? true
          test-sms (sms-to-phones sms-data testing-sms?)]

      (is (= TEST-SMS-MAP-LONG  test-sms)))))

(deftest uni-sms-to-phones
  (testing "test-adjusted-date : java.date to YYYY-MM like 1999-12 if it is 2000-1-1 "
    (console-test "uni-sms-to-phones" "sms-event")

    (let [db-type "monger-db"
          environment-utilize "use-environment"
          [_ _ _ sms-data] (build-db DB-TABLE-NAME
                                     THE-CHECK-PAGES
                                     db-type
                                     TEST-CONFIG-FILE
                                     environment-utilize)
          testing-sms? true
          test-sms (sms-to-phones sms-data testing-sms?)]

      (is (= TEST-SMS-MAP-LONG  test-sms)))))

(deftest uni-single-cron-fn
  (testing "test-adjusted-date : java.date to YYYY-MM like 1999-12 if it is 2000-1-1 "
    (console-test "uni-single-cron-fn" "sms-event")

    (let [db-type "monger-db"
          environment-utilize "use-environment"
          [my-db-obj _ _ sms-data] (build-db DB-TABLE-NAME
                                             THE-CHECK-PAGES
                                             db-type
                                             TEST-CONFIG-FILE
                                             environment-utilize)
          testing-sms? true
          temporize-func (single-cron-fn scrape-pages-fn my-db-obj THE-CHECK-PAGES sms-data testing-sms?)]
      (is (function? temporize-func)))))
