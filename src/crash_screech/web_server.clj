
(ns crash-screech.web-server
  (:require [ring.util.response :as ring-response])
  (:require [ring.adapter.jetty :as ring-jetty])
  (:require [ring.middleware.reload :as ring-reload])
  (:require [crash-screech.singular-service :refer [add-service remove-service]])
  (:require [crash-screech.html-render  :refer [get-index]])
  (:require [crash-screech.sms-event  :refer [sms-to-phones]])
  (:require [crash-screech.years-months  :refer [current-yyyy-mm current-month
                                                 prev-month prev-yyyy-mm date-to-yyyy-mm]])
  )

(defn show-data
  ([my-db-obj test-date]
   (let [the-date-time (test-date)
         yyyy-mm (current-yyyy-mm the-date-time)]
     (ring-response/content-type (ring-response/response (get-index my-db-obj
                                                                    yyyy-mm))
                                 "text/html"))))

(defn show-data-cron
  [my-db-obj the-uri cron-url web-scraper test-date]
  (if (= the-uri cron-url) (web-scraper))
  (show-data my-db-obj test-date))

(defn build-express-serve
  "has db test"
  [web-scraper my-db-obj cron-url sms-data testing-sms? test-date]
  (fn express-server
    [request]
    (let [the-uri (:uri request)
          send-test-sms-url (:send-test-sms-url sms-data)]
      (condp = the-uri
        "/" (show-data my-db-obj test-date)
        cron-url (show-data-cron my-db-obj the-uri cron-url web-scraper test-date)
        send-test-sms-url (sms-to-phones sms-data testing-sms?)
        "/base-styles.css" (ring-response/resource-response "base-styles.css" {:root ""})
        (ring-response/not-found "404")))))

;; https://stackoverflow.com/questions/54056579/how-to-avoid-global-state-in-clojure-when-using-wrap-reload
;     https://github.com/panta82/clojure-webdev/blob/master/src/webdev/core.clj
(def jetty-reloader #'ring-reload/reloader)

(defn web-reload
  []
  (let [reload-jetty! (jetty-reloader ["src"] true)] (reload-jetty!)))

(defn web-init
  [server-port express-server]
  (remove-service "web-init")
  (web-reload)
  (let [web-server (ring-jetty/run-jetty express-server
                                         {:port server-port, :join? false})
        my-kill-web (fn kill-web
                      []
                      (if-not (boolean (resolve 'DB-TEST-NAME)) (println "Killing web-service"))
                      (.stop web-server))]
    (add-service "web-init" my-kill-web)
    my-kill-web))
