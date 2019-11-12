(ns tests-singular-service
  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as spec-alpha]
            [clojure.spec.gen.alpha :as spec-gen]
            [clojure.spec.test.alpha :as spec-test])

  (:require [global-consts-vars  :refer :all])

  (:require [crash-screech.singular-service :refer :all])

  (:require [java-time :refer [local-date?]])


  (:require [prepare-tests :refer :all])
)

;(load "spec-types/shared-types")
;(load "spec-types/singular-service-specs")

(defn singular-service-specs []
  (if RUN-SPEC-TESTS
    (do
(println "Speccing singular-service")
      (spec-test/instrument)
(spec-test/instrument 'add-service)
(spec-test/instrument 'remove-service)
(spec-test/instrument 'kill-services))))


(deftest unit-add-service
  (testing "test-adjusted-date : java.date to YYYY-MM like 1999-12 if it is 2000-1-1 "
    (console-test "unit-add-service" "singular-service")
      
    (let [my-kill-func (fn kill-servce [])
          test-spawn-name (add-service "my-spawn1" my-kill-func)]
      (is (= test-spawn-name "my-spawn1")))


))


(deftest unit-remove-service
  (testing "test-adjusted-date : java.date to YYYY-MM like 1999-12 if it is 2000-1-1 "
    (console-test "unit-remove-service" "singular-service")
      
    (let [ test-spawn-name (remove-service "my-spawn2")]
      (is (= test-spawn-name "my-spawn2")))


))

(deftest unit-kill-services
  (testing "test-adjusted-date : java.date to YYYY-MM like 1999-12 if it is 2000-1-1 "
    (console-test "unit-kill-services" "singular-service")
      
    (let [ test-spawn-name (kill-services)]
      )


))




(defn do-tests []
 (singular-service-specs)
  (run-tests 'tests-singular-service))



