
; This is where Heroku starts  


(ns start-heroku
  (:gen-class)
  (:require [global-consts  :refer :all])
  (:require [global-vars  :refer :all])

  (:require [crash-screech.choose-db :refer [build-db]])
  (:require [crash-screech.cron-service :refer [cron-init]])
  (:require [crash-screech.html-render :refer [make-request-fn make-request-fn web-init]])
  (:require [crash-screech.scrape-html :refer [scrape-pages-fn]])
  (:require [crash-screech.singular-service :refer [kill-services]])
  (:require [crash-screech.sms-event :refer [build-sms-send single-cron-fn]]))


; main called by Heroku, has no cron-init() job, relies on temporize-func()


(comment "to start"
         (-main "monger-db" "../heroku-config.edn")
         (-main "monger-db" "../heroku-config.edn" "use-environment"))
(defn -main
  ([db-type config-file] (-main db-type config-file USE_ENVIRONMENT))

  ([db-type config-file environment-utilize]
   (reset! *we-be-testing* false)
   (let [[my-db-obj web-port cron-url sms-data] (build-db DB-TABLE-NAME
                                                          THE-CHECK-PAGES
                                                          db-type
                                                          config-file
                                                          environment-utilize)
         int-port (Integer/parseInt web-port)
         testing-sms? false
         temporize-func (single-cron-fn scrape-pages-fn my-db-obj THE-CHECK-PAGES sms-data testing-sms?)
         request-handler (make-request-fn temporize-func my-db-obj cron-url sms-data)]
     (web-init int-port request-handler))))





