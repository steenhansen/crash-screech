; /test/core_test.clj  
; (-do-tests)





(ns core-test
  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as spec-alpha]
            [clojure.spec.gen.alpha :as spec-gen]
            [clojure.spec.test.alpha :as spec-test])

  (:require [sff-global-consts  :refer :all])
  (:require [sff-global-vars  :refer :all])

  (:require [crash-screech.singular-service :refer  [kill-services]])

  (:require [crash-screech-test.check-data.count-string-test :as count-string-test])
  (:require [crash-screech-test.check-data.derive-data-test :as derive-data-test])
 (:require [crash-screech-test.check-data.ensure-has-date-test :as ensure-has-date-test])
 (:require [crash-screech-test.check-data.prepare-data-test :as prepare-data-test])
 (:require [crash-screech-test.check-data.trunc-string-test :as trunc-string-test])
 (:require [crash-screech-test.check-data.uniquely-id-test :as uniquely-id-test])

  (:require [crash-screech-test.choose-db.build-db-test :as build-db-test])
  (:require [crash-screech-test.choose-db.build-empty-month-test :as build-empty-month-test])
  (:require [crash-screech-test.choose-db.build-today-error-test :as build-today-error-test])
  (:require [crash-screech-test.choose-db.get-db-conn-test :as get-db-conn-test])

  (:require [crash-screech-test.choose-db.get-phone-nums-test :as get-phone-nums-test])
  (:require [crash-screech-test.years-months.adjusted-date-test :as adjusted-date-test])
  




  (:require [test-prepare :refer :all])
  (:require [spec-calls :refer :all]))

(spec-test/instrument)


(defn check-testing []
  (local-dynamodb-on?)
  (dampen-mongo-logging)
  (local-mongo-on?)
  (sms-is-in-test :monger-db))

 (defn check-data-tests []
 (spec-test/instrument 'count-string)
  (count-string-test/test-count-string)
 
 (spec-test/instrument 'derive-data)
 (derive-data-test/test-derive-data)


 (spec-test/instrument 'ensure-has-data)
 (ensure-has-date-test/test-ensure-has-date)

  (spec-test/instrument 'prepare-data)
  (prepare-data-test/test-prepare-data)
 
  (spec-test/instrument 'trunc-string)
  (trunc-string-test/test-trunc-string)

  (spec-test/instrument 'uniquely-id)
   (uniquely-id-test/test-uniquely-id)
)

(defn choose-db-tests []
     
  (spec-test/instrument 'build-empty-month?)
  (build-empty-month-test/test-build-empty-month-dynoDb)   
  (build-empty-month-test/test-build-empty-month-mongoDb)

  (spec-test/instrument 'build-today-error?)
  (build-today-error-test/test-build-today-error-dynoDb)
  (build-today-error-test/test-build-today-error-mongoDb)


  (spec-test/instrument 'build-db)
  (build-db-test/test-build-db-dynoDb)
  (build-db-test/test-build-db-mongoDb)



 (spec-test/instrument 'get-db-conn)
      (get-db-conn-test/test-get-db-conn-dynoDb)
      (get-db-conn-test/test-get-db-conn-mongoDb)



 (spec-test/instrument 'get-phone-nums)
      (get-phone-nums-test/test-get-phone-nums)


) 




; (defn cron-service-tests []
;   (test-build-cron-func)
;   )

; (defn html-render-tests []
;   (test-count-scrapes)
;   (test-day-hour-min)
;   (test-get-index)
;   (test-get-two-months)
;   (test-make-request-fn1)
;   (test-make-request-fn2)
;   (test-show-data))

; (defn scrape-html-tests []
;   (test-1000)
;   (test-1001)
;   (test-1002)
;   (test-1003)
;   (test-2000)
;   (test-2001))

 (defn years-months-tests []
  (adjusted-date-test/test-adjusted-date)
  ; (test-current-month)
  ; (test-current-yyyy-mm-dd)
  ; (test-current-yyyy-mm)
  ; (test-ensure-date)
  ; (test-month-name_0)
  ; (test-month-name_1)
  ; (test-month-name_-1)
  ; (test-prev-month)
  ; (test-prev-yyyy-mm)
  ; (test-yyyy-mm-to-ints)
)


(defn test-suite []
 ; (check-data-tests)
 ; (choose-db-tests)
                     ;(cron-service-tests)
                      ;(html-render-tests)
                       ;(scrape-html-tests)
  (years-months-tests)
  )

(defn test-ns-hook [] (test-suite))

(defn -do-tests []
   ; (try           
   ;   (println "Nice stack trace print example, via io.aviso/pretty")
   ;   ("nice_stack_trace_print")
   ;    (catch Exception e (clojure.repl/pst e))   
   ;  )

  (reset! *we-be-testing* true)
  (kill-services)
  (check-testing)
  (run-tests 'core-test))


