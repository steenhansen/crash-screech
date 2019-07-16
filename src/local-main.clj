
; dev main, has scrape-pages-fn as an at-at scheduled job
; (kill-services) will delete web-server and at-at-scheduled job
(comment "local monger db"
         (-local-main "monger-db" "./local-config.edn" "ignore-environment"))

(comment "local amazonica db"
         (-local-main "amazonica-db" "./local-config.edn" "ignore-environment"))
(comment "real monger db, config file outside project"
         (-local-main "monger-db" "../heroku-config.edn" "ignore-environment"))

(defn -local-main
  [db-type config-file environment-utilize]
  (let [[my-db-obj web-port cron-url sms-data] (build-db DB-TABLE-NAME
                                                         THE-CHECK-PAGES
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
        temporize-func
        (single-cron-fn scrape-pages-fn my-db-obj THE-CHECK-PAGES sms-data)
        request-handler (make-request-fn temporize-func my-db-obj cron-url sms-data)
        test-one {:the-url "www.sffaudio.com",
                  :the-date "2019-06-19-01:54:03.800Z",
                  :the-html "blah 5555",
                  :the-accurate true,
                  :the-time 1234}]
    ;((:put-items my-db-obj) test-many)
    ;((:put-item my-db-obj) test-one)
    (web-init int-port request-handler)
    (cron-init scrape-pages-fn my-db-obj THE-CHECK-PAGES sms-data)))
