; /test/core_test.clj  
; (-do-tests)



(ns tests-integration.choose-db.integrations-choose-db


  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as spec-alpha]
            [clojure.spec.gen.alpha :as spec-gen]
            [clojure.spec.test.alpha :as spec-test])

  (:require [global-consts  :refer :all])
  (:require [global-vars  :refer :all])

  (:require [crash-screech.singular-service :refer  [kill-services]])


(:require [java-time :refer [local-date?]])




  (:require [tests-integration.choose-db.build-empty-month-test :as build-empty-month-test])
  (:require [tests-integration.choose-db.build-today-error-test :as build-today-error-test])






  (:require [prepare-tests :refer :all])
  (:require [spec-calls :refer :all]))




(defn choose-db-integrations []
  (spec-test/instrument 'build-empty-month?)
  (build-empty-month-test/test-build-empty-month)   

  (spec-test/instrument 'build-today-error?)
  (build-today-error-test/test-build-today-error)

  
) 


