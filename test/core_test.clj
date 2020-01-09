

; A. in core_test.clj hit [F5] for cider-jack-in (once in session)
; C. in core_test.clj hit [F4] for cider-ns-reload-all
; B. in core_test.clj hit [F3] for cider-repl-set-ns

; core-test> (all-tests)

; core-test> (test-sms HEROKU_CONFIG)



(ns core-test
  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as t])
  (:require [global-consts-vars  :refer :all])
  (:require [crash-sms.singular-service :refer  [kill-services]])
  (:require [java-time :refer [local-date?]])
  (:require [tests-check-data  :refer [check-data-specs]])
  (:require [tests-data-store  :refer [data-store-specs]])
  (:require [tests-config-args  :refer [config-args-specs]])
  (:require [tests-dynamo-db  :refer [dynamo-db-specs]])
  (:require [tests-html-render  :refer [html-render-specs]])
  (:require [tests-fake-db  :refer [fake-db-specs]])
  (:require [tests-mongo-db  :refer [mongo-db-specs]])
  (:require [tests-scrape-html  :refer [scrape-html-specs]])
  (:require [tests-singular-service  :refer [singular-service-specs]])
  (:require [tests-sms-event  :refer [sms-event-specs]])
  (:require [tests-years-months  :refer [years-months-specs]])
  (:require [tests-web-server  :refer [web-server-specs]])
  (:require [text-diff :refer [is-html-eq]])
  (:require   [sms-test :refer :all])
  (:require [crash-sms.data-store :refer  :all])
  (:require [crash-sms.fake-db :refer  :all])
  (:require [prepare-tests :refer :all])
)


(defn check-testing []
  (let [the-check-pages (make-check-pages 0)
        [my-db-dynamo _ _ _] (build-db T-TEST-COLLECTION
                                       the-check-pages
                                       USE_AMAZONICA_DB
                                       TEST-CONFIG-FILE
                                       IGNORE-ENV-VARS)
        [my-db-mongo _ _ _] (build-db T-TEST-COLLECTION
                                      the-check-pages
                                      USE_MONGER_DB
                                      TEST-CONFIG-FILE
                                      IGNORE-ENV-VARS)]
(s/check-asserts true)
    (dampen-mongo-logging)
    (sms-is-in-test USE_MONGER_DB))
)

(defn console-divider []
  (print-line "............................................")
  (print-line "............................................")
  (print-line "............................................")
  (print-line "............................................")
  (print-line "............................................")
  (print-line "............................................"))

(defn check-specs []
  (check-data-specs)
  (data-store-specs)
  (config-args-specs)
  (dynamo-db-specs)
  (html-render-specs)
  (fake-db-specs)
  (mongo-db-specs)
  (scrape-html-specs)
  (singular-service-specs)
  (sms-event-specs)
  (web-server-specs)
  (years-months-specs))


(defn start-tests []
  (kill-services)
  (check-testing)
  (console-divider)
  (check-specs)
  (run-tests
   'tests-check-data
   'tests-data-store
   'tests-config-args
   'tests-dynamo-db
   'tests-html-render
   'tests-mongo-db
   'tests-scrape-html
   'tests-sms-event
   'tests-web-server
   'tests-years-months)
)

(defn all-tests []
  (reset! *T-REAL-DB-ASSERTIONS* true)
  (reset! *T-ASSERTIONS-VIA-REPL* false)
  (start-tests)
  (reset! *T-ASSERTIONS-VIA-REPL* true)
)

(defn mock-tests []
  (reset! *T-REAL-DB-ASSERTIONS* false)
  (reset! *T-ASSERTIONS-VIA-REPL* false)
  (start-tests)
 (reset! *T-ASSERTIONS-VIA-REPL* true)
)
