
(defn start-cron
  [cron-job my-db-obj pages-to-check sms-data]
  (let [sms-send-fn (build-sms-send sms-data)
        read-from-web false]
    (defn cron-func
      []
      (cron-job my-db-obj
                pages-to-check
                instant-time-fn
                sms-send-fn
                read-from-web))
    (let [thread-pool (at-at/mk-pool)
          scheduled-task (at-at/every CRON-MILL-SECS cron-func thread-pool)]
      (defn remove-crons
        []
        (println "Removing all cron-services")
        (try (at-at/stop-and-reset-pool! thread-pool)
             (catch Exception e
               (println " -- caught exception " (.getMessage e)))))
      scheduled-task)))

(defn cron-init
  [cron-job my-db-obj pages-to-check sms-data]
  (let [scheduled-task (start-cron cron-job my-db-obj pages-to-check sms-data)]
    (defn kill-cron [] (at-at/stop scheduled-task))
    (remove-service "start-cron")
    (add-service "start-cron" kill-cron)))
