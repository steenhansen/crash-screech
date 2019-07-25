

(ns send-sms-test
(:use [ sff-global-consts  :refer :all ])
(:use [  sff-audio.choose-db :refer[build-db] ])
(:use [  sff-audio.sms-event :refer [sms-to-phones] ])
)


(comment "to send sms message to phone"
         (-sms-test "monger-db" "../heroku-config.edn" "use-environment"))
(defn -sms-test
  [db-type config-file environment-utilize]
  (let [[_ _ _ sms-data] (build-db DB-TABLE-NAME
                                   THE-CHECK-PAGES
                                   db-type
                                   config-file
                                   environment-utilize)]
    (sms-to-phones sms-data)))
