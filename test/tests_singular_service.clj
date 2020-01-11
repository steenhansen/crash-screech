(ns tests-singular-service
  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as t])
  (:require [global-consts-vars  :refer :all])
  (:require [crash-sms.singular-service :refer :all])
  (:require [java-time :refer [local-date?]])
  (:require [prepare-tests :refer :all]))

(s/fdef add-service
  :args (s/cat :spawning-name string?
               :kill-service function?))
(s/fdef remove-service
  :args (s/cat :spawning-name string?))

(s/fdef kill-services
  :args (s/cat))

(defn singular-service-specs []
  (print-line "Speccing singular-service")
  (t/instrument)
  (t/instrument 'add-service)
  (t/instrument 'remove-service)
  (t/instrument 'kill-services))

(deftest test-add-service
  (console-test "test-singular-service test-add-service")
  (let [my-kill-func (fn kill-servce [])
        test-spawn-name (add-service "my-spawn1" my-kill-func)]
    (is (= test-spawn-name "my-spawn1"))))

(deftest test-remove-service
  (console-test "test-singular-service test-remove-service")
  (let [test-spawn-name (remove-service "my-spawn2")]
    (is (= test-spawn-name "my-spawn2"))))

;   (clojure.test/test-vars [#'tests-singular-service/test-kill-services])
(deftest test-kill-services
  (console-test "test-singular-service unit-kill-services")
  (let [test-spawn-name (kill-services)]))

(defn all-tests []
  (reset! *T-REAL-DB-ASSERTIONS* true)
  (reset! *T-ASSERTIONS-VIA-REPL* false)
  (singular-service-specs)
  (run-tests 'tests-singular-service)
  (reset! *T-ASSERTIONS-VIA-REPL* true))

(defn fast-tests []
  (reset! *T-REAL-DB-ASSERTIONS* false)
  (reset! *T-ASSERTIONS-VIA-REPL* false)
  (singular-service-specs)
  (run-tests 'tests-singular-service)
  (reset! *T-ASSERTIONS-VIA-REPL* true))
