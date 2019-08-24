

(ns tests-cron-service
  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as spec-alpha]
            [clojure.spec.gen.alpha :as spec-gen]
            [clojure.spec.test.alpha :as spec-test])

  (:require [global-consts  :refer :all])
  (:require [global-vars  :refer :all])

  (:require [crash-screech.cron-service :refer :all])
  (:require [crash-screech.choose-db :refer [build-db]])

  (:require [java-time :refer [local-date?]])

  (:require [prepare-tests :refer :all]))

(defn cron-service-specs []
  (if RUN-SPEC-TESTS
    (do
      (spec-test/instrument)
      (spec-test/instrument 'build-cron-func)
      (spec-test/instrument 'start-cron))))

(deftest uni-build-cron-func
  (testing "test-get-phone-nums : cccccccccccccccccccccc "
    (console-test "uni-build-cron-func" "cron-service")
    (let [cron-job (fn my-cron-job [])
          pass-small 1
          pages-to-check [{:check-page "www.sffaudio.com",
                           :enlive-keys [:article :div.blog-item-wrap],
                           :at-least pass-small}]
          db-type "monger-db"
          environment-utilize "use-environment"
          [my-db-obj _ _ sms-data] (build-db DB-TABLE-NAME
                                             THE-CHECK-PAGES
                                             db-type
                                             TEST-CONFIG-FILE
                                             environment-utilize)
          a-cron-func (build-cron-func cron-job my-db-obj pages-to-check sms-data)]
      (is (function? a-cron-func)))))

(deftest uni-start-cron
  (testing "test-get-phone-nums : cccccccccccccccccccccc "
    (console-test "uni-start-cron-func" "cron-service")
    (let [cron-job (fn my-cron-job [])
          pass-small 1
          pages-to-check [{:check-page "www.sffaudio.com",
                           :enlive-keys [:article :div.blog-item-wrap],
                           :at-least pass-small}]
          db-type "monger-db"
          environment-utilize "use-environment"
          [my-db-obj _ _ sms-data] (build-db DB-TABLE-NAME
                                             THE-CHECK-PAGES
                                             db-type
                                             TEST-CONFIG-FILE
                                             environment-utilize)
          a-cron-func (start-cron cron-job my-db-obj pages-to-check sms-data)
          cron-type (str (type a-cron-func))]
      (is (= cron-type "class overtone.at_at.RecurringJob")))))

(deftest uni-cron-init
  (testing "test-get-phone-nums : cccccccccccccccccccccc "
    (console-test "uni-start-cron-func" "cron-service")
    (let [cron-job (fn my-cron-job [])
          pass-small 1
          pages-to-check [{:check-page "www.sffaudio.com",
                           :enlive-keys [:article :div.blog-item-wrap],
                           :at-least pass-small}]
          db-type "monger-db"
          environment-utilize "use-environment"
          [my-db-obj _ _ sms-data] (build-db DB-TABLE-NAME
                                             THE-CHECK-PAGES
                                             db-type
                                             TEST-CONFIG-FILE
                                             environment-utilize)
          a-cron-func (cron-init cron-job my-db-obj pages-to-check sms-data)
          cron-type (str (type a-cron-func))]
      (is (= cron-type "class overtone.at_at.RecurringJob")))))
