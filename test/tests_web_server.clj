
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
  (:require [java-time :refer [local-date?]])

  (:require [prepare-tests :refer :all]))

(defn web-server-specs []
  (if RUN-SPEC-TESTS
    (do

      (spec-test/instrument)

      (spec-test/instrument 'make-request-fn))))


 (tests-web-server/integration-produce-page)    ;; SAVE-ACTUAL-TO-EXPECTED-DATA)

(deftest integration-produce-page
  (testing "test-day-hour-min : cccccccccccccccccccccc "
    (console-test  "integration-make-request-fn"  "web-server"))

  (reset-test-to-actual-data test-file actual-data)           ;; to re-save the actual output into expected 

   ; like in tests_check_data.clj

   ; need to ignore the-time / check-time

)


(deftest integration-make-request-fn
  (testing "test-day-hour-min : cccccccccccccccccccccc "
    (console-test  "integration-make-request-fn"  "web-server")
    (let [db-type "monger-db"
          [my-db-obj _ cron-url sms-data] (build-db DB-TABLE-NAME
                                                    THE-CHECK-PAGES
                                                    db-type
                                                    TEST-CONFIG-FILE
                                                    IGNORE-ENV-VARS)
 purge-table (:purge-table my-db-obj)         
 testing-sms? true
          temporize-func (build-web-scrap scrape-pages-fn my-db-obj THE-CHECK-PAGES sms-data testing-sms?)
          request-handler (make-request-fn temporize-func my-db-obj cron-url sms-data testing-sms?)
 send-test-sms-url (:send-test-sms-url sms-data)
;cron-url-address (:uri cron-url)
]

;; if empty db do we send sms, and are pages scraped
 (reset! global-consts-vars/*sms-was-executed* false)
 (reset! global-consts-vars/*pages-were-scraped* false)
 (purge-table)
 (println "ddd" ( request-handler {:uri cron-url }))
 (println  cron-url "*pages-were-scraped*" @global-consts-vars/*pages-were-scraped*)
 (println send-test-sms-url "*sms-was-executed*" @global-consts-vars/*sms-was-executed*)


; does send-sms send smsm
(reset! global-consts-vars/*sms-was-executed* false)
  (println "hhhhh" ( request-handler   {:uri send-test-sms-url}))
 (println send-test-sms-url "*sms-was-executed*" @global-consts-vars/*sms-was-executed*)



;; if empty db did we read 10 sites, by counting "a_countable_scrape"
;; (countable-scraps the-test-return
;; THE-CHECK-PAGES  has 8 members
(purge-table)
(println "ddd" ( request-handler {:uri "/"}))


; we can have the 8 members scraped data, at like a certain set time like 2010
; then we fake read the file data and we should get the saved correct files....


;      (is (= expected-day-hour-min actual-day-hour-min))
      )))



