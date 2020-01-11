
(ns start-local-heroku-db

  (:gen-class)
  (:require [global-consts-vars  :refer :all])
  (:require [crash-sms.data-store :refer [build-db]])
  (:require [crash-sms.web-server :refer [build-express-serve web-init]])
  (:require [crash-sms.years-months :refer [instant-time-fn]])
  (:require [crash-sms.scrape-html :refer [scrape-pages-fn]])
  (:require [crash-sms.singular-service :refer [kill-services]])
  (:require [crash-sms.sms-event :refer [build-sms-send build-web-scrape]]))

(comment "to start"
         (-local-heroku-main USE_MONGER_DB HEROKU_CONFIG)
 ;
         )
(defn -local-heroku-main

  ([db-type config-file] (-local-heroku-main db-type config-file IGNORE-ENV-VARS))

  ([db-type config-file environment-utilize]
   (kill-services)
   (let [the-check-pages (make-check-pages 0)
         [my-db-obj web-port cron-url sms-data] (build-db PRODUCTION-COLLECTION
                                                          the-check-pages
                                                          db-type
                                                          config-file
                                                          environment-utilize)
         int-port (Integer/parseInt web-port)
         testing-sms? true
         web-scraper-fn (build-web-scrape scrape-pages-fn my-db-obj the-check-pages sms-data testing-sms? instant-time-fn)
         under-test? true
         express-server-fn (build-express-serve web-scraper-fn my-db-obj cron-url sms-data under-test? instant-time-fn)]
     (web-init int-port express-server-fn))))
