(ns tests-singular-service
  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as spec-alpha]
            [clojure.spec.gen.alpha :as spec-gen]
            [clojure.spec.test.alpha :as spec-test])

  (:require [global-consts  :refer :all])
  (:require [global-vars  :refer :all])

  (:require [crash-screech.singular-service :refer :all])

  (:require [java-time :refer [local-date?]])


  (:require [prepare-tests :refer :all])
  (:require [spec-calls :refer :all]))



(spec-test/instrument 'add-service)
(spec-test/instrument 'remove-service)
(spec-test/instrument 'kill-services)

(deftest uni-add-service
  (testing "test-adjusted-date : java.date to YYYY-MM like 1999-12 if it is 2000-1-1 "
    (console-test "uni-add-service" "singular-service")
      
    (let [my-kill-func (fn kill-servce [])
          test-spawn-name (add-service "my-spawn1" my-kill-func)]
      (is (= test-spawn-name "my-spawn1")))


))


(deftest uni-remove-service
  (testing "test-adjusted-date : java.date to YYYY-MM like 1999-12 if it is 2000-1-1 "
    (console-test "uni-remove-service" "singular-service")
      
    (let [ test-spawn-name (remove-service "my-spawn2")]
      (is (= test-spawn-name "my-spawn2")))


))

(deftest uni-kill-services
  (testing "test-adjusted-date : java.date to YYYY-MM like 1999-12 if it is 2000-1-1 "
    (console-test "uni-kill-services" "singular-service")
      
    (let [ test-spawn-name (kill-services)]
      )


))

