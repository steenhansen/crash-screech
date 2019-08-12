; /test/core_test.clj  
; (-do-tests)





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
  (:require [tests-html-render  :refer :all])
  (:require [tests-scrape-html  :refer :all])

  (:require [prepare-tests :refer :all])
  (:require [spec-calls :refer :all]))

(spec-test/instrument)

(defn check-data-tests
  []
  (tests-check-data/uni-count-string)
  (tests-check-data/uni-derive-data)
  (tests-check-data/uni-ensure-has-date)
  (tests-check-data/uni-prepare-data)
  (tests-check-data/uni-trunc-string)
  (tests-check-data/uni-uniquely-id))

(defn choose-db-tests []
  (tests-choose-db/uni-build-db)
  (tests-choose-db/uni-get-db-conn)
  (tests-choose-db/uni-get-phone-nums))

(defn html-render-tests []
  (tests-html-render/uni-day-hour-min))

(defn scrape-html-tests []
  (tests-scrape-html/uni-count-scrapes)
)


(defn test-ns-hook []
  (check-data-tests)
  (choose-db-tests)
  (html-render-tests)
(scrape-html-tests))

(defn check-testing []
  (local-dynamodb-on?)
  (dampen-mongo-logging)
  (local-mongo-on?)
  (sms-is-in-test :monger-db))

(defn test-units []

 ; (tests-unit.scrape-html.units-scrape-html/scrape-html-units)

 ; (tests-unit.years-months.units-years-months/years-months-units)
  )

(defn test-integrations []
 ; (tests-integration.html-render.integrations-html-render/html-render-integrations)
 ; (tests-integration.choose-db.integrations-choose-db/choose-db-integrations)
  )

(defn test-systems [])

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


