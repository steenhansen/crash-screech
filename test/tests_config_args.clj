



(ns tests-config-args
  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as s]

            [clojure.spec.test.alpha :as t])

  (:require [global-consts-vars  :refer :all])

  (:require [crash-sms.config-args :refer :all])

  (:require [java-time :refer [local-date?]])

  (:require [prepare-tests :refer :all]))




(s/fdef read-config-file
  :args (s/cat :config-file :edn-filename?/test-specs))

(s/fdef merge-environment
  :args (s/cat :accum-environment :map-entry-or-emtpy?/test-specs
               :env-object vector?))

(s/fdef make-config
  :args (s/cat :db-type string?
               :config-file :edn-filename?/test-specs
               :environment-utilize string? ))


(defn config-args-specs []
  (print-line "Speccing config-args")
      (t/instrument)
      (t/instrument 'read-config-file)
      (t/instrument 'merge-environment)
      (t/instrument 'make-config))

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
  :fake-db {}
   :monger-db {:MONGODB_URI "mongodb://localhost:27017/local"}})

;  (clojure.test/test-vars [#'tests-config-args/t-read-config-file])
(deftest test-read-config-file
    (console-test "test-read-config-file" "config-args")
    (let [config-file LOCAL_CONFIG
          config-map (read-config-file config-file)]
      (is (= config-map TEST-READ-CONFIG))))

(deftest test-merge-environment
      (console-test "test-merge-environment" "config-args")
    (let [a-map-entry (first {:not_exist_key "a_value"})
          start-accum {}
          new-env (merge-environment start-accum a-map-entry)]
      (is (= new-env {:not_exist_key "a_value"}))))

(deftest test-make-config
      (console-test "test-make-config" "config-args")
    (let [config-map (make-config USE_MONGER_DB LOCAL_CONFIG IGNORE-ENV-VARS)]
      (is (= config-map TEST-MAKE-CONFIG))))

(defn all-tests []
  (reset! *T-REAL-DB-ASSERTIONS* true)
  (reset! *T-ASSERTIONS-VIA-REPL* false)
(config-args-specs)
  (run-tests 'tests-config-args)
  (reset! *T-ASSERTIONS-VIA-REPL* true))



(defn fast-tests []
  (reset! *T-REAL-DB-ASSERTIONS* false)
  (reset! *T-ASSERTIONS-VIA-REPL* false)
(config-args-specs)
  (run-tests 'tests-config-args)
  (reset! *T-ASSERTIONS-VIA-REPL* true))
