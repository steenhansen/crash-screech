; main called by Heroku, has no cron-init() job, relies on temporize-func()
(comment "to start"
         (-heroku-main "monger-db" "./local-config.edn" "use-environment"))
(defn -main
  [db-type config-file environment-utilize]
  (let [[my-db-obj web-port cron-url sms-data] (build-db DB-TABLE-NAME
                                                         THE-CHECK-PAGES
                                                         db-type
                                                         config-file
                                                         environment-utilize)
        int-port (Integer/parseInt web-port)
        temporize-func
          (single-cron-fn scrape-pages-fn my-db-obj THE-CHECK-PAGES sms-data)
        request-handler (make-request-fn temporize-func my-db-obj cron-url)]
    (web-init int-port request-handler)))
