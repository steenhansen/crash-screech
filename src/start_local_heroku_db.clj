
(ns start-local-heroku_db

  (:gen-class)
  (:require [global-consts  :refer :all])
  (:require [global-vars  :refer :all])

  (:require [crash-screech.choose-db :refer [build-db]])
  (:require [crash-screech.cron-service :refer [cron-init]])
  (:require [crash-screech.html-render :refer [make-request-fn make-request-fn web-init]])
  (:require [crash-screech.scrape-html :refer [scrape-pages-fn]])
  (:require [crash-screech.singular-service :refer [kill-services]])
  (:require [crash-screech.sms-event :refer [build-sms-send single-cron-fn]]))

(comment "to start"
         (-local-heroku-main "monger-db" "../heroku-config.edn"))
(defn -local-heroku-main

  ([db-type config-file] (-local-heroku-main db-type config-file IGNORE-ENV-VARS))

  ([db-type config-file environment-utilize]
   (kill-services)
   (reset! *we-be-testing* false)
   (let [[my-db-obj web-port cron-url sms-data] (build-db DB-TABLE-NAME
                                                          THE-CHECK-PAGES
                                                          db-type
                                                          config-file
                                                          environment-utilize)
         int-port (Integer/parseInt web-port)
         temporize-func
         (single-cron-fn scrape-pages-fn my-db-obj THE-CHECK-PAGES sms-data)
         request-handler (make-request-fn temporize-func my-db-obj cron-url sms-data)]
     (web-init int-port request-handler))))
