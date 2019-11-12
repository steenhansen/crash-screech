

; A. in core_test.clj hit [F5] for cider-jack-in (once in session)
; C. in core_test.clj hit [F4] for cider-ns-reload-all
; B. in core_test.clj hit [F3] for cider-repl-set-ns    

; core-test> (do-tests)

; core-test> (test-sms "../heroku-config.edn") 

  
   
(ns core-test
  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as spec-alpha]
            [clojure.spec.gen.alpha :as spec-gen]
            [clojure.spec.test.alpha :as spec-test])
  (:require [global-consts-vars  :refer :all])
  (:require [crash-screech.singular-service :refer  [kill-services]])
  (:require [java-time :refer [local-date?]])
  (:require [tests-check-data  :refer [check-data-specs]])
  (:require [tests-choose-db  :refer [choose-db-specs]])
  (:require [tests-config-args  :refer [config-args-specs]])
  (:require [tests-cron-service  :refer [cron-service-specs]])
  (:require [tests-dynamo-db  :refer [dynamo-db-specs]])
  (:require [tests-html-render  :refer [html-render-specs]])
  (:require [tests-mongo-db  :refer [mongo-db-specs]])
  (:require [tests-scrape-html  :refer [scrape-html-specs]])
  (:require [tests-singular-service  :refer [singular-service-specs]])
  (:require [tests-sms-event  :refer [sms-event-specs]])
  (:require [tests-years-months  :refer [years-months-specs]])
  (:require [tests-web-server  :refer [web-server-specs]])
  (:require [text-diff :refer [is-html-eq]])
  (:require   [sms-test :refer :all])      ; (-test-sms "../heroku-config.edn")
  (:require [prepare-tests :refer :all]))

(defn check-testing []
  (local-dynamodb-on?)
  (dampen-mongo-logging)
  (local-mongo-on?)
  (sms-is-in-test "monger-db"))

(defn console-divider []
  (println "............................................")
  (println "............................................")
  (println "............................................")
  (println "............................................")
  (println "............................................")
  (println "............................................"))

(defn do-tests []
  (reset! *we-be-testing* true)
  (kill-services)
  (check-testing)
  (console-divider)
  (run-tests
   'tests-check-data
   'tests-choose-db
   'tests-config-args
   'tests-cron-service
   'tests-dynamo-db
   'tests-html-render
   'tests-mongo-db
   'tests-scrape-html
   'tests-sms-event
   'tests-web-server
   'tests-years-months)
  (check-data-specs)
  (choose-db-specs)
  (config-args-specs)
  (cron-service-specs)
  (dynamo-db-specs)
  (html-render-specs)
  (mongo-db-specs)
  (scrape-html-specs)
  (sms-event-specs)
  (web-server-specs)
  (years-months-specs)
  (run-tests))


