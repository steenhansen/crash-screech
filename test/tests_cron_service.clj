

(ns tests-cron-service
  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as spec-alpha]
            [clojure.spec.gen.alpha :as spec-gen]
            [clojure.spec.test.alpha :as spec-test])

  (:require [global-consts-vars  :refer :all])

  (:require [crash-sms.cron-service :refer :all])
  (:require [crash-sms.choose-db :refer [build-db]])

  (:require [java-time :refer [local-date?]])

  (:require [prepare-tests :refer :all]))



(alias 's 'clojure.spec.alpha)
(alias 'c-s 'crash-sms.cron-service)

(s/fdef c-s/build-cron-func
  :args (s/cat :cron-job function?
               :my-db-obj coll?
               :pages-to-check vector?
               :sms-data :sms-data?/test-specs))

(s/fdef c-s/start-cron
  :args (s/cat :cron-job function?
               :my-db-obj coll?
               :pages-to-check vector?
               :sms-data :sms-data?/test-specs))


(s/fdef c-s/cron-init
  :args (s/cat :cron-job function?
               :my-db-obj coll?
               :pages-to-check vector?
               :sms-data :sms-data?/test-specs))



(defn cron-service-specs []
  (println "Speccing cron-service")
      (spec-test/instrument)
      (spec-test/instrument 'build-cron-func)
      (spec-test/instrument 'start-cron))

(deftest unit-build-cron-func
    (console-test "unit-build-cron-func" "cron-service")
    (let [cron-job (fn my-cron-job [])
          pass-small 1
          pages-to-check [{:check-page "www.sffaudio.com",
                           :enlive-keys [:article :div.blog-item-wrap],
                           :at-least pass-small}]
          db-type USE_MONGER_DB
          environment-utilize USE_ENVIRONMENT
          the-check-pages (make-check-pages 0)
          [my-db-obj _ _ sms-data] (build-db T-TEST-COLLECTION
                                             the-check-pages
                                             db-type
                                             TEST-CONFIG-FILE
                                             environment-utilize)
          a-cron-func (build-cron-func cron-job my-db-obj pages-to-check sms-data)]
      (is (function? a-cron-func))))

(deftest unit-start-cron
    (console-test "unit-start-cron-func" "cron-service")
    (let [cron-job (fn my-cron-job [])
          pass-small 1
          pages-to-check [{:check-page "www.sffaudio.com",
                           :enlive-keys [:article :div.blog-item-wrap],
                           :at-least pass-small}]
          db-type USE_MONGER_DB
          environment-utilize USE_ENVIRONMENT
          the-check-pages (make-check-pages 0)
          [my-db-obj _ _ sms-data] (build-db T-TEST-COLLECTION
                                             the-check-pages
                                             db-type
                                             TEST-CONFIG-FILE
                                             environment-utilize)
          a-cron-func (start-cron cron-job my-db-obj pages-to-check sms-data)
          cron-type (str (type a-cron-func))]
      (is (= cron-type "class overtone.at_at.RecurringJob"))))

(deftest unit-cron-init
    (console-test "unit-start-cron-func" "cron-service")
    (let [cron-job (fn my-cron-job [])
          pass-small 1
          pages-to-check [{:check-page "www.sffaudio.com",
                           :enlive-keys [:article :div.blog-item-wrap],
                           :at-least pass-small}]
          db-type USE_MONGER_DB
          environment-utilize USE_ENVIRONMENT
          the-check-pages (make-check-pages 0)
          [my-db-obj _ _ sms-data] (build-db T-TEST-COLLECTION
                                             the-check-pages
                                             db-type
                                             TEST-CONFIG-FILE
                                             environment-utilize)
          a-cron-func (cron-init cron-job my-db-obj pages-to-check sms-data)
          cron-type (str (type a-cron-func))]
      (is (= cron-type "class overtone.at_at.RecurringJob"))))


(defn do-tests []
(cron-service-specs)
  (run-tests 'tests-cron-service))
