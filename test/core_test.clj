

; A. cider-jack-in core.clj [==]
; B. cider-repl-set-ns core_test.clj [--]  
; C. cider-ns-reload-all  [F12]

; core-test> (-do-tests)

; core-test> (-test-sms "../heroku-config.edn") =

   

;(def ^:const SAVE-ACTUAL-TO-EXPECTED-DATA true)



(def ^:const TEST-CHECK-DATA false)
(def ^:const TEST-CHOOSE-DB false)
(def ^:const TEST-CONFIG-ARGS false)
(def ^:const TEST-CRON-SERVICE false)
(def ^:const TEST-DYNAMO-DB false)
(def ^:const TEST-HTML-RENDER false)
(def ^:const TEST-MONGO-DB false)
(def ^:const TEST-SCRAPE-HTML false)   
(def ^:const TEST-SINGULAR-SERVICE false)
(def ^:const TEST-SMS-EVENT false)

(def ^:const TEST-WEB-SERVER true)


(def ^:const TEST-YEARS-MONTHSXX false)

(ns core-test
  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as spec-alpha]
            [clojure.spec.gen.alpha :as spec-gen]
            [clojure.spec.test.alpha :as spec-test])

  (:require [global-consts-vars  :refer :all])

  (:require [crash-screech.singular-service :refer  [kill-services]])

  (:require [java-time :refer [local-date?]])

  (:require [tests-check-data  :refer :all])
  (:require [tests-choose-db  :refer :all])

  (:require [tests-config-args  :refer :all])
  (:require [tests-cron-service  :refer :all])
  (:require [tests-dynamo-db  :refer :all])

  (:require [tests-html-render  :refer :all])
  (:require [tests-mongo-db  :refer :all])
  (:require [tests-scrape-html  :refer :all])

  (:require [tests-singular-service  :refer :all])

  (:require [tests-sms-event  :refer :all])
  (:require [tests-years-months  :refer :all])
  (:require [tests-web-server  :refer :all])

  (:require   [sms-test :refer :all])      ; (-test-sms "../heroku-config.edn")
  (:require [prepare-tests :refer :all]))

(load "spec-types/shared-types")
(load "spec-types/check-data-specs")
(load "spec-types/choose-db-specs")
(load "spec-types/config-args-specs")
(load "spec-types/cron-service-specs")
(load "spec-types/dynamo-db-specs")
(load "spec-types/html-render-specs")
(load "spec-types/mongo-db-specs")
(load "spec-types/scrape-html-specs")
(load "spec-types/singular-service-specs")
(load "spec-types/sms-event-specs")
(load "spec-types/web-server-specs")
(load "spec-types/years-months-specs")

(defn check-data-tests  []
  (tests-check-data/check-data-specs)
  (tests-check-data/unit-count-string)
  (tests-check-data/unit-trunc-string)
  (tests-check-data/unit-derive-data)
  (tests-check-data/unit-uniquely-id)
  (tests-check-data/unit-ensure-has-date)
  (tests-check-data/unit-prepare-data))

(defn choose-db-tests []
  (tests-choose-db/choose-db-specs)
  (tests-choose-db/unit-get-phone-nums)
  (tests-choose-db/unit-get-db-conn-dynoDb)
  (tests-choose-db/unit-get-db-conn-mongoDb)
  (tests-choose-db/unit-build-empty-month-mongoDb)
  (tests-choose-db/unit-build-empty-month-dynoDb)
  (tests-choose-db/unit-build-today-error-mongoDb)
  (tests-choose-db/unit-build-today-error-dynoDb)
  (tests-choose-db/unit-build-db))

(defn config-args-tests []
  (tests-config-args/config-args-specs)
  (tests-config-args/unit-read-config-file)
  (tests-config-args/unit-merge-environment)
  (tests-config-args/unit-make-config))

(defn cron-service-tests []
  (tests-cron-service/cron-service-specs)
  (tests-cron-service/unit-build-cron-func)
  (tests-cron-service/unit-start-cron)
  (tests-cron-service/unit-cron-init))

(defn dynamo-db-tests []
  (tests-dynamo-db/dynamo-db-specs)
  (tests-dynamo-db/unit-dynamo-build))

(defn html-render-tests []
  (tests-html-render/html-render-specs)               ;;; we are here q*bert
  (tests-html-render/unit-day-hour-min)
  (tests-html-render/unit-get-index)

;  (tests-html-render/unit-show-data)

)

(defn mongo-db-tests []
  (tests-mongo-db/mongo-db-specs)

  (tests-mongo-db/unit-mongolabs-build)

  (tests-mongo-db/unit-next-date-time-a)
  (tests-mongo-db/unit-next-date-time-b)
  (tests-mongo-db/unit-next-date-time-c)
  (tests-mongo-db/unit-next-date-time-d)
  (tests-mongo-db/unit-next-date-time-e)
  (tests-mongo-db/unit-next-date-time-f))

(defn scrape-html-tests []
  (tests-scrape-html/scrape-html-specs)
  (tests-scrape-html/unit-count-scrapes))

(defn singular-service-tests []
  (tests-singular-service/singular-service-specs)
  (tests-singular-service/unit-add-service)
  (tests-singular-service/unit-remove-service)
  (tests-singular-service/unit-kill-services))

(defn sms-event-tests []
  (tests-sms-event/sms-event-specs)
  (tests-sms-event/unit-make-api-call)
  (tests-sms-event/unit-build-sms-send)
  (tests-sms-event/unit-sms-to-phones)
  (tests-sms-event/unit-build-web-scrap))

(defn web-server-tests[]
 (tests-web-server/web-server-specs)
 (tests-web-server/integration-make-request-fn)    ;; SAVE-ACTUAL-TO-EXPECTED-DATA)
 (tests-web-server/integration-produce-page)    ;; SAVE-ACTUAL-TO-EXPECTED-DATA)

)

(defn years-months-tests []
  (tests-years-months/years-months-specs)
  (tests-years-months/unit-adjusted-date)
  (tests-years-months/unit-current-month)
  (tests-years-months/unit-current-yyyy-mm-dd)

  (tests-years-months/unit-current-yyyy-mm-0)
  (tests-years-months/unit-current-yyyy-mm-days)

  (tests-years-months/unit-date-to-yyyy-mm)

  (tests-years-months/unit-instant-time-fn)

  (tests-years-months/unit-month-name_0)
  (tests-years-months/unit-month-name_1)
  (tests-years-months/unit-month-name_2)

  (tests-years-months/unit-prev-month)

  (tests-years-months/unit-prev-yyyy-mm)

  (tests-years-months/unit-trunc-yyyy-mm-dd)
  (tests-years-months/unit-trunc-yyyy-mm)
  (tests-years-months/unit-yyyy-mm-to-ints))

(defn test-ns-hook []
  (if TEST-CHECK-DATA
    (check-data-tests))
  (if TEST-CHOOSE-DB
    (choose-db-tests))
  (if TEST-CONFIG-ARGS
    (config-args-tests))
  (if TEST-CRON-SERVICE
    (cron-service-tests))
  (if TEST-DYNAMO-DB
    (dynamo-db-tests))
  (if TEST-HTML-RENDER
    (html-render-tests))
  (if TEST-MONGO-DB
    (mongo-db-tests))
  (if TEST-SCRAPE-HTML
    (scrape-html-tests))
  (if  TEST-SINGULAR-SERVICE
    (singular-service-tests))
  (if TEST-SMS-EVENT
    (sms-event-tests))

  (if TEST-WEB-SERVER
    (web-server-tests))


  (if TEST-YEARS-MONTHS
    (years-months-tests)))

(defn check-testing []
  (local-dynamodb-on?)
  (dampen-mongo-logging)
  (local-mongo-on?)
  (sms-is-in-test "monger-db"))

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


