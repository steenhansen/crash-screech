
; /test/test_sms.clj

; (-test-sms "../heroku-config.edn")
; (-test-sms "../heroku-config.edn" false)
; (-test-sms "../heroku-config.edn" true "use-environment")

(ns sms-test

  (:require [global-consts-vars  :refer :all])
  (:require [crash-screech.choose-db :refer [build-db]])
  (:require [crash-screech.sms-event :refer [sms-to-phones]]))

(comment "to send sms message to phone"
         (-sms-test "../heroku-config.edn")
         (-sms-test "../heroku-config.edn" "use-environment"))

(defn -test-sms
  ([config-file] (-test-sms config-file true))

  ([config-file testing-sms?] (-test-sms config-file testing-sms? IGNORE-ENV-VARS))

  ([config-file testing-sms? environment-utilize]
   (let [db-type MONGER_DB
         [_ _ _ sms-data] (build-db DB-TABLE-NAME
                                    THE-CHECK-PAGES
                                    db-type
                                    config-file
                                    environment-utilize)]
     (sms-to-phones sms-data testing-sms?))))
