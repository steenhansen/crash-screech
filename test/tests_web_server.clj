
(ns tests-web-server

  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as spec-alpha]
            [clojure.spec.gen.alpha :as spec-gen]
            [clojure.spec.test.alpha :as spec-test])

  (:require [global-consts-vars  :refer :all])

  (:require [crash-screech.choose-db :refer [build-db]])

  (:require [crash-screech.web-server :refer :all])

  (:require [crash-screech.sms-event :refer [build-sms-send build-web-scrap]])
  (:require [crash-screech.scrape-html :refer [scrape-pages-fn]])

  (:require [crash-screech.years-months :refer [date-with-now-time-fn instant-time-fn]])

  (:require [java-time :refer [local-date?]])

  (:require [text-diff :refer [is-html-eq]])

  (:require [prepare-tests :refer :all]))

;(load "spec-types/shared-types")
;(load "spec-types/web-server-specs")

(defn web-server-specs []
  (if RUN-SPEC-TESTS
    (do
      (println "Speccing web-server")
      (spec-test/instrument)
      (spec-test/instrument 'make-request-fn))))


; (tests-web-server/integration-produce-page)    ;; SAVE-ACTUAL-TO-EXPECTED-DATA)


(deftest integration-produce-page
  (testing "test-day-hour-min : cccccccccccccccccccccc "
    (console-test  "integration-make-request-fn"  "web-server"))

  ;(reset-test-to-actual-data test-file actual-data)           ;; to re-save the actual output into expected 

   ; like in tests_check_data.clj

   ; need to ignore the-time / check-time
  )

(deftest integration-make-request-fn
  (testing "test-day-hour-min : cccccccccccccccccccccc "
    (console-test  "integration-make-request-fn"  "web-server")
    (reset! *test-use-test-time* true)

(let [ 
      test1  (date-with-now-time-fn "2019-07-04")
      test2 (date-with-now-time-fn "2019-07-04") ]
  (println "test1" (type test1) test1)
 (println "test2" (type test2) test2)

)

    (let [db-type "monger-db"
          [my-db-obj _ cron-url sms-data] (build-db DB-TABLE-NAME
                                                    THE-CHECK-PAGES
                                                    db-type
                                                    TEST-CONFIG-FILE
                                                    IGNORE-ENV-VARS)
          purge-table (:purge-table my-db-obj)
          testing-sms? true
test-date (date-with-now-time-fn "2019-10-01")     ;  9  aug
;test2 (instant-time-fn)
          temporize-func (build-web-scrap scrape-pages-fn my-db-obj THE-CHECK-PAGES sms-data testing-sms? test-date)
          request-handler (make-request-fn temporize-func my-db-obj cron-url sms-data testing-sms? test-date)
          send-test-sms-url (:send-test-sms-url sms-data)
          the-request (request-handler  {:uri cron-url})
          the-body (:body the-request)
          file-expected (slurp  (str SCRAPED-TEST-DATA "tests_web_server_1.html"))]




      (reset! global-consts-vars/*sms-was-executed* false)
      (reset! global-consts-vars/*pages-were-scraped* false)
      (purge-table)
      (reset! global-consts-vars/*sms-was-executed* false)
      (purge-table)
     (let [ [text-diff-1 text-diff-2] (is-html-eq the-body file-expected)]
          (is (= text-diff-1 text-diff-2)))

)))



;; (defn web-server-tests[]
;;  (tests-web-server/web-server-specs)
;;  (tests-web-server/integration-make-request-fn)    ;; SAVE-ACTUAL-TO-EXPECTED-DATA)
;;  (tests-web-server/integration-produce-page)    ;; SAVE-ACTUAL-TO-EXPECTED-DATA)

;; )


(defn do-tests []
  (web-server-specs)
  (run-tests 'tests-web-server))
