



(ns tests-config-args
  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as spec-alpha]
            [clojure.spec.gen.alpha :as spec-gen]
            [clojure.spec.test.alpha :as spec-test])

  (:require [global-consts-vars  :refer :all])

  (:require [crash-screech.config-args :refer :all])

  (:require [java-time :refer [local-date?]])

  (:require [prepare-tests :refer :all]))


(alias 's 'clojure.spec.alpha)
(alias 'c-a 'crash-screech.config-args)

(s/fdef c-a/read-config-file
  :args (s/cat :config-file :edn-filename?/test-specs))

(s/fdef c-a/merge-environment
  :args (s/cat :accum-environment :map-entry-or-emtpy?/test-specs
               :env-object vector?))

(s/fdef c-a/make-config
  :args (s/cat :db-type string?
               :config-file :edn-filename?/test-specs
               :environment-utilize string? ))


(defn config-args-specs []
 (if RUN-SPEC-TESTS
    (do
  (println "Speccing config-args")
      (spec-test/instrument)
      (spec-test/instrument 'read-config-file)
      (spec-test/instrument 'merge-environment)
      (spec-test/instrument 'make-config))))

(def ^:const TEST-MAKE-CONFIG
  {:SEND_TEST_SMS_URL "/zxc"
   :PHONE_NUMBERS "12345678901,12345678901,12345678901"
   :HEROKU_APP_NAME "https://fathomless-woodland-85635.herokuapp.com/"
   :MONGODB_URI "mongodb://localhost:27017/local"
   :TILL_USERNAME "abcdefghijklmnopqrstuvwxyz1234"
   :TILL_API_KEY "1234567890abcdefghijklmnopqrstuvwxyz1234"
   :TILL_URL "https://platform.tillmobile.com/api/send"
   :CRON_URL_DIR "/url-for-cron-execution"
   :TESTING_SMS true
   :PORT "8080"})

(def ^:const TEST-READ-CONFIG
  {:amazonica-db {:access-key "local-access"
                  :endpoint "http://localhost:8000"
                  :secret-key "local-secret"}
   :heroku-vars {:SEND_TEST_SMS_URL "/zxc",
                 :PHONE_NUMBERS "12345678901,12345678901,12345678901",
                 :HEROKU_APP_NAME "https://fathomless-woodland-85635.herokuapp.com/"
                 :TILL_USERNAME "abcdefghijklmnopqrstuvwxyz1234"
                 :TILL_API_KEY "1234567890abcdefghijklmnopqrstuvwxyz1234"
                 :TILL_URL "https://platform.tillmobile.com/api/send"
                 :CRON_URL_DIR "/url-for-cron-execution"
                 :TESTING_SMS true,
                 :PORT "8080"},
   :monger-db {:MONGODB_URI "mongodb://localhost:27017/local"}})


(deftest unit-read-config-file
  (testing "read-config-file : cccccccccccccccccccccc "
    (let [config-file "./local-config.edn"
          config-map (read-config-file config-file)]
      (console-test "unit-read-config-file" "config-args")
      (is (= config-map TEST-READ-CONFIG)))))

(deftest unit-merge-environment
  (testing "merge-environment : cccccccccccccccccccccc "
    (let [a-map-entry (first {:not_exist_key "a_value"})
          start-accum {}
          new-env (merge-environment start-accum a-map-entry)]
      (console-test "unit-merge-environment" "config-args")
      (is (= new-env {:not_exist_key "a_value"})))))

(deftest unit-make-config
  (testing "make-config : cccccccccccccccccccccc "
    (let [db-type "monger-db"
          config-file "./local-config.edn"
          environment-utilize  "ignore-environment"
          config-map (make-config db-type config-file environment-utilize)]
      (console-test "unit-make-config" "config-args")
      (is (= config-map TEST-MAKE-CONFIG)))))




(defn do-tests []
(config-args-specs)
  (run-tests 'tests-config-args))



