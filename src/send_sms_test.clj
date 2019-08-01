
; /src/send_sms_test.clj

(ns send-sms-test
  
  (:require [sff-global-consts  :refer :all])
  (:require [crash-screech.choose-db :refer [build-db]])
  (:require [crash-screech.sms-event :refer [sms-to-phones]])
  
  
;  (:use [sff-global-consts  :refer :all])
 ; (:use [crash-screech.choose-db :refer [build-db]])
  ;(:use [crash-screech.sms-event :refer [sms-to-phones]])
  
  
  )

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
