; /test/core_test.clj  
; (-do-tests)





(ns tests-unit.years-months.units-years-months
  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as spec-alpha]
            [clojure.spec.gen.alpha :as spec-gen]
            [clojure.spec.test.alpha :as spec-test])

  (:require [global-consts  :refer :all])
  (:require [global-vars  :refer :all])

  (:require [crash-screech.singular-service :refer  [kill-services]])


(:require [java-time :refer [local-date?]])




  (:require [tests-unit.years-months.adjusted-date-test :as adjusted-date-test])
  (:require [tests-unit.years-months.current-month-test :as current-month-test])
  (:require [tests-unit.years-months.current-yyyy-mm-dd-test :as current-yyyy-mm-dd-test])
  (:require [tests-unit.years-months.current-yyyy-mm-test :as current-yyyy-mm-test])
  (:require [tests-unit.years-months.date-to-yyyy-mm-test :as date-to-yyyy-mm-test])
  (:require [tests-unit.years-months.month-name-test :as month-name-test])
  (:require [tests-unit.years-months.prev-month-test :as prev-month-test])
  (:require [tests-unit.years-months.prev-yyyy-mm-test :as prev-yyyy-mm-test])
  (:require [tests-unit.years-months.yyyy-mm-to-ints-test :as yyyy-mm-to-ints-test])
  
  (:require [tests-unit.years-months.instant-time-fn-test :as instant-time-fn-test])
;  (:require [tests-unit.years-months.trunc-yyyy-mm-dd-test :as trunc-yyyy-mm-dd-test])
;  (:require [tests-unit.years-months.yyyy-mm-to-ints-test :as yyyy-mm-to-ints-test])




  (:require [prepare-tests :refer :all])
  (:require [spec-calls :refer :all]))




 (defn years-months-units []
  (adjusted-date-test/test-adjusted-date)

   (current-month-test/test-current-month)

  (spec-test/instrument 'current-yyyy-mm-dd)
   (current-yyyy-mm-dd-test/test-current-yyyy-mm-dd)

  (spec-test/instrument 'current-yyyy-mm)
   (current-yyyy-mm-test/test-current-yyyy-mm)


  (spec-test/instrument 'date-to-yyyy-mm)
   (date-to-yyyy-mm-test/test-date-to-yyyy-mm)

     (spec-test/instrument 'instant-time-fn)
      (instant-time-fn-test/test-instant-time-fn)
   
  (spec-test/instrument 'month-name)
   (month-name-test/test-month-name)


  (spec-test/instrument 'prev-month)
   (prev-month-test/test-prev-month)

  (spec-test/instrument 'prev-yyyy-mm)
   (prev-yyyy-mm-test/test-prev-yyyy-mm)

;;  (spec-test/instrument 'trunc-yyyy-mm-dd)
;;   (trunc-yyyy-mm-dd-test/test-trunc-yyyy-mm-dd)

;;  (spec-test/instrument 'trunc-yyyy-mm)
;;   (trunc-yyyy-mm-test/test-trunc-yyyy-mm)



  (spec-test/instrument 'yyyy-mm-to-ints)
   (yyyy-mm-to-ints-test/test-yyyy-mm-to-ints)






)
