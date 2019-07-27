
(ns local-heroku-start

  (:gen-class)
  (:require [sff-global-consts  :refer :all ] )
    (:require [sff-global-vars  :refer :all ] )
    
  (:require [sff-audio.choose-db :refer [build-db]])
  (:require [sff-audio.cron-service :refer [cron-init]])
  (:require [sff-audio.html-render :refer [make-request-fn make-request-fn web-init]])
  (:require [sff-audio.scrape-html :refer [scrape-pages-fn]])
  (:require [sff-audio.singular-service :refer [kill-services]])
  (:require [  sff-audio.sms-event :refer [build-sms-send single-cron-fn] ])
)




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
    (web-init int-port request-handler)))
  
  )
