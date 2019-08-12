
; /test/test_sms.clj

 ; (-sms-test "../heroku-config.edn")


(ns test-sms
  
  (:require [global-consts  :refer :all])
  (:require [crash-screech.choose-db :refer [build-db]])
  (:require [crash-screech.sms-event :refer [sms-to-phones]])
  
  
;  (:use [global-consts  :refer :all])
 ; (:use [crash-screech.choose-db :refer [build-db]])
  ;(:use [crash-screech.sms-event :refer [sms-to-phones]])
  
  
  )

(comment "to send sms message to phone"
         (-sms-test "../heroku-config.edn")
         (-sms-test "../heroku-config.edn" "use-environment"))

(defn -sms-test
  ([config-file] (-sms-test config-file IGNORE-ENV-VARS))

  ([config-file environment-utilize]
   (let [db-type MONGER_DB
         [_ _ _ sms-data] (build-db DB-TABLE-NAME
                                    THE-CHECK-PAGES
                                    db-type
                                    config-file
                                    environment-utilize)]
     (sms-to-phones sms-data))))
