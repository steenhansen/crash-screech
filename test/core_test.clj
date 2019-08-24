

; A. cider-jack-in core.clj [==]
; B. cider-repl-set-ns core_test.clj [--]  
; C. cider-ns-reload-all  [F12]

; core-test> (-do-tests)

; core-test> (-test-sms "../heroku-config.edn") =

   




(def ^:const TEST-CHECK-DATA false)
(def ^:const TEST-CHOOSE-DB false)
(def ^:const TEST-CONFIG-ARGS false)
(def ^:const TEST-CRON-SERVICE true)
(def ^:const TEST-DYNAMO-DB false)
(def ^:const TEST-HTML-RENDER false)
(def ^:const TEST-MONGO-DB false)
(def ^:const TEST-SCRAPE-HTML false)     ;; weird qqq
(def ^:const TEST-SINGULAR-SERVICE false)
(def ^:const TEST-SMS-EVENT false)
(def ^:const TEST-YEARS-MONTHS false)

(ns core-test
  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as spec-alpha]
            [clojure.spec.gen.alpha :as spec-gen]
            [clojure.spec.test.alpha :as spec-test])

  (:require [global-consts  :refer :all])
  (:require [global-vars  :refer :all])

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
(load "spec-types/years-months-specs")

(defn check-data-tests  []
  (tests-check-data/check-data-specs)
  (tests-check-data/uni-count-string)
  (tests-check-data/uni-trunc-string)
  (tests-check-data/uni-derive-data)
  (tests-check-data/uni-uniquely-id)
  (tests-check-data/uni-ensure-has-date)
  (tests-check-data/uni-prepare-data)
)

(defn choose-db-tests []
  (tests-choose-db/choose-db-specs)
  (tests-choose-db/uni-get-phone-nums)
  (tests-choose-db/uni-get-db-conn-dynoDb)
  (tests-choose-db/uni-get-db-conn-mongoDb)
  (tests-choose-db/uni-build-empty-month-mongoDb)
  (tests-choose-db/uni-build-empty-month-dynoDb)
  (tests-choose-db/uni-build-today-error-mongoDb)
  (tests-choose-db/uni-build-today-error-dynoDb)
  (tests-choose-db/uni-build-db)
)

(defn config-args-tests []                 
  (tests-config-args/config-args-specs)
  (tests-config-args/uni-read-config-file)
  (tests-config-args/uni-merge-environment)
  (tests-config-args/uni-make-config))

(defn cron-service-tests [] 
  (tests-cron-service/cron-service-specs)
  (tests-cron-service/uni-build-cron-func)
  (tests-cron-service/uni-start-cron)
  (tests-cron-service/uni-cron-init))

(defn dynamo-db-tests []
  (tests-dynamo-db/dynamo-db-specs)
  (tests-dynamo-db/uni-dynamo-build))

(defn html-render-tests []
  (tests-html-render/html-render-specs)               ;;; we are here q*bert
  (tests-html-render/uni-day-hour-min)
)

(defn mongo-db-tests []
  (tests-mongo-db/mongo-db-specs)


  (tests-mongo-db/uni-mongolabs-build)

  (tests-mongo-db/uni-next-date-time-a)
  (tests-mongo-db/uni-next-date-time-b)
  (tests-mongo-db/uni-next-date-time-c)
  (tests-mongo-db/uni-next-date-time-d)
  (tests-mongo-db/uni-next-date-time-e)
  (tests-mongo-db/uni-next-date-time-f))

(defn scrape-html-tests []
  (tests-scrape-html/scrape-html-specs)
  (tests-scrape-html/uni-count-scrapes))

(defn singular-service-tests []
  (tests-singular-service/singular-service-specs)
  (tests-singular-service/uni-add-service)
  (tests-singular-service/uni-remove-service)
  (tests-singular-service/uni-kill-services))

(defn sms-event-tests []
  (tests-sms-event/sms-event-specs)
  (tests-sms-event/uni-make-api-call)
  (tests-sms-event/uni-build-sms-send)
  (tests-sms-event/uni-sms-to-phones)
  (tests-sms-event/uni-single-cron-fn))

(defn years-months-tests []
  (tests-years-months/years-months-specs)
  (tests-years-months/uni-adjusted-date)
  (tests-years-months/uni-current-month)
  (tests-years-months/uni-current-yyyy-mm-dd)

  (tests-years-months/uni-current-yyyy-mm-0)
  (tests-years-months/uni-current-yyyy-mm-days)

  (tests-years-months/uni-date-to-yyyy-mm)

  (tests-years-months/uni-instant-time-fn)

  (tests-years-months/uni-month-name_0)
  (tests-years-months/uni-month-name_1)
  (tests-years-months/uni-month-name_2)

  (tests-years-months/uni-prev-month)

  (tests-years-months/uni-prev-yyyy-mm)

  (tests-years-months/uni-trunc-yyyy-mm-dd)
  (tests-years-months/uni-trunc-yyyy-mm)
  (tests-years-months/uni-yyyy-mm-to-ints))

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


