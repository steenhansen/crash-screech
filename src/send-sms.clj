; main called by Heroku, has no cron-init() job, relies on temporize-func()
(comment "to send sms message to phone"
         (-sms-test "monger-db" "../heroku-config.edn" "use-environment")    )
(defn -sms-test
  [db-type config-file environment-utilize]
  (let [[_ _ _ sms-data] (build-db DB-TABLE-NAME
                                                         THE-CHECK-PAGES
                                                         db-type
                                                         config-file
                                                         environment-utilize)
       ]
    (sms-to-phones sms-data )))
