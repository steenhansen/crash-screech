
; /src/send_sms_test.clj

(ns send-sms-test
  (:use [sff-global-consts  :refer :all])
  (:use [sff-audio.choose-db :refer [build-db]])
  (:use [sff-audio.sms-event :refer [sms-to-phones]]))

(comment "to send sms message to phone"
         (-sms-test "../heroku-config.edn")
         (-sms-test "../heroku-config.edn" "use-environment"))

(defn -sms-test
  ([config-file] (-sms-test IGNORE-ENV-VARS))

  ([config-file environment-utilize]
   (let [db-type MONGER_DB
         [_ _ _ sms-data] (build-db DB-TABLE-NAME
                                    THE-CHECK-PAGES
                                    db-type
                                    config-file
                                    environment-utilize)]
     (sms-to-phones sms-data))))
