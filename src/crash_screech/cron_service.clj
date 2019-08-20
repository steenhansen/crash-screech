(ns crash-screech.cron-service
  (:require [overtone.at-at :as at-at])
  (:require [crash-screech.singular-service :refer [remove-service add-service]])

  (:require [global-consts  :refer :all])
  (:require [crash-screech.sms-event :refer [build-sms-send]])

  (:require [crash-screech.years-months :refer [instant-time-fn current-yyyy-mm]]))

(defn build-cron-func [cron-job my-db-obj pages-to-check sms-data]
  (let [testing-sms? false
        sms-send-fn (build-sms-send sms-data testing-sms?)
        read-from-web false]
    (fn cron-func
      []
      (cron-job my-db-obj
               pages-to-check
                instant-time-fn
                sms-send-fn
                read-from-web))))

(defn start-cron
  [cron-job my-db-obj pages-to-check sms-data]
  (let [testing-sms? false
        sms-send-fn (build-sms-send sms-data testing-sms?)
        read-from-web false
        my-cron-func (build-cron-func cron-job my-db-obj pages-to-check sms-data)]
    (let [thread-pool (at-at/mk-pool)
          scheduled-task (at-at/every CRON-MILL-SECS my-cron-func thread-pool)]

      (println "dddcron desc" CRON-MILL-SECS)

      (fn remove-crons
        []
        (println "Removing all cron-services")
        (try (at-at/stop-and-reset-pool! thread-pool)
             (catch Exception e
               (println " -- caught exception " (.getMessage e)))))
      scheduled-task)))

(defn cron-init
  [cron-job my-db-obj pages-to-check sms-data]
  (let [scheduled-task (start-cron cron-job my-db-obj pages-to-check sms-data)
        my-kill-cron (fn kill-cron [] (at-at/stop scheduled-task))]
    (remove-service START_CRON)

    (add-service START_CRON my-kill-cron)))
