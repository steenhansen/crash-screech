
(ns start-local
  (:gen-class)
  (:require [global-consts-vars  :refer :all])

  (:require [crash-screech.choose-db :refer [build-db]])
  (:require [crash-screech.cron-service :refer [cron-init]])
  (:require [crash-screech.web-server :refer [make-request-fn web-init]])
  (:require [crash-screech.scrape-html :refer [scrape-pages-fn]])
  (:require [crash-screech.singular-service :refer  [kill-services]])
  (:require [crash-screech.years-months :refer [instant-time-fn]])
  (:require [crash-screech.sms-event :refer [build-sms-send build-web-scrape]]))

; dev main, has scrape-pages-fn as an at-at scheduled job
; (kill-services) will delete web-server and at-at-scheduled job


(comment
         (-local-main USE_MONGER_DB LOCAL_CONFIG IGNORE-ENV-VARS )   "local monger db"
         (-local-main USE_AMAZONICA_DB LOCAL_CONFIG IGNORE-ENV-VARS)  "local amazonica db"
         (-local-main USE_MONGER_DB HEROKU_CONFIG IGNORE-ENV-VARS)   "real monger db, config file outside project"
;
)

(defn -local-main
  ([db-type config-file] (-local-main db-type config-file IGNORE-ENV-VARS))
  ([db-type config-file environment-utilize]
   (kill-services)
   (let [the-check-pages (make-check-pages 0)
[my-db-obj web-port cron-url sms-data] (build-db PRODUCTION-COLLECTION
                                                          the-check-pages
                                                          db-type
                                                          config-file
                                                          environment-utilize)
         int-port (Integer/parseInt web-port)
         test-many [{:the-url "www.sffaudio.com",
                     :the-date "2019-06-19-01:54:03.800Z",
                     :the-html "blah 1111",
                     :the-accurate true,
                     :the-time 1234}
                    {:the-url "sffaudio.herokuapp.com_rsd_rss",
                     :the-date "2019-06-19-01:54:03.800Z",
                     :the-html "bluh 2222",
                     :the-accurate true,
                     :the-time 12346}
                    {:the-url "www.sffaudio.com",
                     :the-date "2019-05-19-01:54:03.800Z",
                     :the-html "blah 3333",
                     :the-accurate true,
                     :the-time 1234}
                    {:the-url "sffaudio.herokuapp.com_rsd_rss",
                     :the-date "2019-05-19-01:54:03.800Z",
                     :the-html "bluhss 4444",
                     :the-accurate false,
                     :the-time 12346}]
testing-sms? true
         temporize-func (build-web-scrape scrape-pages-fn my-db-obj the-check-pages sms-data testing-sms? instant-time-fn)
         request-handler (make-request-fn temporize-func my-db-obj cron-url sms-data testing-sms? instant-time-fn)
         test-one {:the-url "www.sffaudio.com",
                   :the-date "2019-06-19-01:54:03.800Z",
                   :the-html "blah 5555",
                   :the-accurate true,
                   :the-time 1234}]
    ;((:put-items my-db-obj) test-many)
    ;((:put-item my-db-obj) test-one)
     (web-init int-port request-handler)
     (cron-init scrape-pages-fn my-db-obj the-check-pages sms-data)
;     (println "after cron-init")
)))
