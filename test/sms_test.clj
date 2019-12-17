
; /test/test_sms.clj

; (-test-sms HEROKU_CONFIG)
; (-test-sms HEROKU_CONFIG false)
; (-test-sms HEROKU_CONFIG true USE_ENVIRONMENT)

(ns sms-test

  (:require [global-consts-vars  :refer :all])
  (:require [crash-screech.choose-db :refer [build-db]])
  (:require [crash-screech.sms-event :refer [sms-to-phones]]))

(comment "to send sms message to phone"
         (-sms-test HEROKU_CONFIG)
         (-sms-test HEROKU_CONFIG USE_ENVIRONMENT))

(defn -test-sms
  ([config-file] (-test-sms config-file true))

  ([config-file testing-sms?] (-test-sms config-file testing-sms? IGNORE-ENV-VARS))

  ([config-file testing-sms? environment-utilize]
   (let [db-type USE_MONGER_DB
         [_ _ _ sms-data] (build-db T-DB-TEST-NAME
                                    THE-CHECK-PAGES
                                    db-type
                                    config-file
                                    environment-utilize)]
     (sms-to-phones sms-data testing-sms?))))
