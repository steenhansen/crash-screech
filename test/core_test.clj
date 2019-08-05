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
  (:require [crash-screech-test.choose-db.build-db-test :as build-db-test])
  (:require [crash-screech-test.choose-db.get-phone-nums-test :as get-phone-nums-test])
  (:require [crash-screech.choose-db :refer [get-phone-nums]])
  
  (:require [test-prepare :refer :all])
  (:require [spec-types :refer :all]))

(spec-test/instrument)

;;;; some uses in test-prepare

(defn check-testing []
  (local-dynamodb-on?)
  (dampen-mongo-logging)
  (local-mongo-on?)
  (sms-is-in-test :monger-db))

; (defn check-data-tests []
;   (test-count-string)
;   (test-derive-data)
;   (test-ensure-has-date)
;   (test-prepare-data)
;   (test-sub-string)
;   (test-trunc-string)
;   (test-uniquely-id))a

(defn choose-db-tests []
      (spec-test/instrument 'get-phone-nums)
      (get-phone-nums-test/test-get-phone-nums)


  (spec-test/instrument 'build-db)
  (build-db-test/test-build-db-dynoDb)
  (build-db-test/test-build-db-mongoDb)) 

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

; (defn years-months-tests []
;   (test-adjusted-date)
;   (test-current-month)
;   (test-current-yyyy-mm)
;   (test-ensure-date)
;   (test-month-name_0)
;   (test-month-name_1)
;   (test-month-name_-1)
;   (test-prev-month)
;   (test-prev-yyyy-mm)
;   (test-yyyy-mm-to-ints))


(defn test-suite []
 ; (check-data-tests)
  (choose-db-tests)
  ;(cron-service-tests)
  ;(html-render-tests)
  ;(scrape-html-tests)
  ;(years-months-tests)
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


