
(ns crash-sms.web-server
  (:require [ring.util.response :as ring-response])
  (:require [ring.adapter.jetty :as ring-jetty])
(:require [global-consts-vars  :refer :all])
  (:require [ring.middleware.reload :as ring-reload])
  (:require [crash-sms.singular-service :refer [add-service remove-service]])
  (:require [crash-sms.html-render  :refer [get-index]])
  (:require [crash-sms.sms-event  :refer [sms-to-phones]])
  (:require [crash-sms.years-months  :refer [current-yyyy-mm current-month
                                                 prev-month prev-yyyy-mm date-to-yyyy-mm]])
  )

(defn show-data
  ([my-db-obj test-date]
   (show-data my-db-obj test-date false))
  ([my-db-obj test-date under-test?]         ;; under-test
   (let [the-date-time (test-date)
         yyyy-mm (current-yyyy-mm the-date-time)]
     (ring-response/content-type (ring-response/response (get-index my-db-obj yyyy-mm under-test?))
                                 "text/html"))))

(defn show-data-cron
  [my-db-obj the-uri cron-url web-scraper test-date  under-test?]
  (if (= the-uri cron-url) (web-scraper))
  (show-data my-db-obj test-date under-test?))   ;;; under-test

(defn build-express-serve
  "has db test"
  [web-scraper my-db-obj cron-url sms-data under-test? test-date]    ;;; under-test???
  (fn express-server
    [request]
    (let [the-uri (:uri request)
          send-test-sms-url (:send-test-sms-url sms-data)]
      (condp = the-uri
        "/" (show-data my-db-obj test-date)
        cron-url (show-data-cron my-db-obj the-uri cron-url web-scraper test-date under-test?)
        send-test-sms-url (sms-to-phones sms-data under-test?)
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
                      (if-not (boolean (resolve 'DB-TEST-NAME)) (print-line "Killing web-service"))
                      (.stop web-server))]
    (add-service "web-init" my-kill-web)
    my-kill-web))
