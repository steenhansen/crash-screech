
(ns crash-sms.web-server
  (:require [ring.util.response :as ring-response])
  (:require [ring.adapter.jetty :as ring-jetty])
  (:require [global-consts-vars  :refer :all])
  (:require [ring.middleware.reload :as ring-reload])
  (:require [crash-sms.singular-service :refer [add-service remove-service]])
  (:require [crash-sms.html-render  :refer [get-index]])
  (:require [crash-sms.sms-event  :refer [sms-to-phones]])
  (:require [crash-sms.years-months  :refer [current-yyyy-mm current-month
                                             prev-month prev-yyyy-mm date-to-yyyy-mm]]))

(def ^:const WEB-SERVER-HANDLE "web-server-handle")

(defn show-data
  ([my-db-obj the-date-func]
   (show-data my-db-obj the-date-func false))
  ([my-db-obj the-date-func under-test?]
   (let [the-date-time (the-date-func)
         yyyy-mm (current-yyyy-mm the-date-time)]
     (ring-response/content-type (ring-response/response (get-index my-db-obj yyyy-mm under-test?))
                                 "text/html"))))

(defn show-data-cron
  [my-db-obj the-uri cron-url web-scraper-fn the-date-func under-test?]
  (if (= the-uri cron-url) (web-scraper-fn))
  (show-data my-db-obj the-date-func under-test?))

(defn build-express-serve
  [web-scraper-fn my-db-obj cron-url sms-data testing-sms? under-test? the-date-func]
  (fn express-server
    [request]
    (let [the-uri (:uri request)
          send-test-sms-url (:send-test-sms-url sms-data)]
      (condp = the-uri
        "/" (show-data my-db-obj the-date-func)
        cron-url (show-data-cron my-db-obj the-uri cron-url web-scraper-fn the-date-func under-test?)
        send-test-sms-url (sms-to-phones sms-data testing-sms?)
        "/base-styles.css" (ring-response/resource-response "base-styles.css" {:root ""})
        (ring-response/not-found "404")))))

; https://stackoverflow.com/questions/54056579/how-to-avoid-global-state-in-clojure-when-using-wrap-reload
; https://github.com/panta82/clojure-webdev/blob/master/src/webdev/core.clj
(def jetty-reloader #'ring-reload/reloader)

(defn web-reload  []
  (let [reload-jetty! (jetty-reloader ["src"] true)] (reload-jetty!)))

(defn kill-webserver []
 (remove-service WEB-SERVER-HANDLE)
)

(defn web-init
  [server-port express-server-fn]
  (remove-service WEB-SERVER-HANDLE)
  (web-reload)
  (let [web-server (ring-jetty/run-jetty express-server-fn
                                         {:port server-port, :join? false})
        my-kill-web (fn kill-web
                      []
                      (print-line "Killing web-service")
                      (.stop web-server))]
    (add-service WEB-SERVER-HANDLE my-kill-web)
    my-kill-web))
