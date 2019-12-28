(ns tests-singular-service
  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as spec-alpha]
            [clojure.spec.gen.alpha :as spec-gen]
            [clojure.spec.test.alpha :as spec-test])
  (:require [global-consts-vars  :refer :all])
  (:require [crash-sms.singular-service :refer :all])
  (:require [java-time :refer [local-date?]])
  (:require [prepare-tests :refer :all]))

(defn singular-service-specs []
      (println "Speccing singular-service")
      (spec-test/instrument)
      (spec-test/instrument 'add-service)
      (spec-test/instrument 'remove-service)
      (spec-test/instrument 'kill-services))

(deftest unit-add-service
  (console-test "test-singular-service unit-add-service" )
  (let [my-kill-func (fn kill-servce [])
        test-spawn-name (add-service "my-spawn1" my-kill-func)]
    (is (= test-spawn-name "my-spawn1"))))

(deftest unit-remove-service
  (console-test "test-singular-service unit-remove-service" )
  (let [test-spawn-name (remove-service "my-spawn2")]
    (is (= test-spawn-name "my-spawn2"))))

;   (clojure.test/test-vars [#'tests-singular-service/unit-kill-services])
(deftest unit-kill-services
  (console-test "test-singular-service unit-kill-services" )
  (let [test-spawn-name (kill-services)]))

(defn do-tests []
  (singular-service-specs)
  (run-tests 'tests-singular-service))
