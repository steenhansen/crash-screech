
(ns crash-screech.html-render

  (:require [net.cgrand.enlive-html :as enlive-html])
  (:require [clojure.string :as clj-str])
  (:require [ring.util.response :as ring-response])
  (:require [ring.middleware.reload :as ring-reload])

  (:require [ring.adapter.jetty :as ring-jetty])

  (:require [global-consts  :refer :all])

  (:require [crash-screech.years-months  :refer [current-yyyy-mm current-month
                                                 prev-month prev-yyyy-mm date-to-yyyy-mm]])
  (:require [crash-screech.sms-event  :refer [sms-to-phones]])
  (:require [crash-screech.singular-service :refer [add-service remove-service]])
  (:require [java-time.local :as j-time]))

(defn render-parts [html-pieces] (clj-str/join html-pieces))

(defn day-hour-min
  "has test"
  [check-date]
  (let [dd-hh-mm (subs check-date 8 16)
        [days hours minutes] (clj-str/split dd-hh-mm #"-")
        short-date (str days "-" hours ":" minutes)]
    short-date))

(defn fill-accurate
  [check-accurate]
  (enlive-html/do-> (enlive-html/content (if check-accurate "" "FAIL"))
                    (if check-accurate
                      (enlive-html/add-class "status_good")
                      (enlive-html/add-class "status_bad"))))

(defn fill-time
  [check-time]
  (enlive-html/do-> (enlive-html/content (str check-time))
                    (if (> check-time 2000)
                      (enlive-html/add-class "speed_bad")
                      (if (> check-time 1000)
                        (enlive-html/add-class "speed_average")
                        (enlive-html/add-class "speed_good")))))

(defn fill-date
  [check-date]
  (enlive-html/do-> (enlive-html/content (day-hour-min check-date))))

(defn fill-url [check-url] (enlive-html/do-> (enlive-html/content check-url)))

(defn fill-bytes
  "no test"
  [check-bytes]
  (enlive-html/do-> (enlive-html/content (str check-bytes))))

(defn fill-html
  [check-accurate check-html]
  (enlive-html/do-> (enlive-html/content (if check-accurate "" check-html))))

(enlive-html/defsnippet row-selector
  BASE-HTML-TEMPLATE
  [[:.month_content (enlive-html/nth-of-type 1)] :>
   enlive-html/first-child]
  [{:keys [check-url check-date check-bytes check-html
           check-accurate check-time]}]
  [:div.scrape_accurate]
  (fill-accurate check-accurate)
  [:div.scrape_time]
  (fill-time check-time)
  [:div.scrape_date]
  (fill-date check-date)
  [:div.scrape_url]
  (fill-url check-url)
  [:div.scrape_bytes]
  (fill-bytes check-bytes)
  [:div.scrape_html]
  (fill-html check-accurate check-html))

(enlive-html/defsnippet month-selector
  BASE-HTML-TEMPLATE
  {[:.a_month] [[:.month_content
                 (enlive-html/nth-of-type 1)]]}
  [{:keys [month-type month-data]}]
  [:.a_month]
  (enlive-html/content month-type)
  [:.month_content]
  (enlive-html/content (map row-selector month-data)))

(enlive-html/deftemplate index-page
  BASE-HTML-TEMPLATE
  [{:keys [page-title month-sections]}]
  [:#title_of_page]
  (enlive-html/content page-title)
  [:#two_months]
  (enlive-html/content (map month-selector
                            month-sections)))

(defn get-two-months
  "has db test"
  [my-db-obj yyyy-mm]

  (let [get-all (:get-all my-db-obj)
        this-y-m (current-yyyy-mm yyyy-mm)
        last-y-m (prev-yyyy-mm yyyy-mm)
        this-months (get-all this-y-m)
        last-months (get-all last-y-m)
        current-months (vec this-months)
        previous-months (vec last-months)]
    [previous-months current-months]))

(defn get-index
  "has db test"
  ([my-db-obj] (get-index my-db-obj (date-to-yyyy-mm (j-time/local-date))))
  ([my-db-obj yyyy-mm]
   (let [[previous-months current-months] (get-two-months my-db-obj yyyy-mm)
         prev-name (prev-month yyyy-mm)
         cur-name (current-month yyyy-mm)
         db-data {:page-title "SFFaudio page checks",
                  :month-sections
                  [{:month-type prev-name, :month-data previous-months}
                   {:month-type cur-name, :month-data current-months}]}
         page-html (render-parts (index-page db-data))]
     page-html)))

(defn show-data
  "has db test"
  ([my-db-obj] (show-data my-db-obj (date-to-yyyy-mm (j-time/local-date))))
  ([my-db-obj yyyy-mm]
   (ring-response/content-type (ring-response/response (get-index my-db-obj
                                                                  yyyy-mm))
                               "text/html")))

(defn make-request-fn
  "has db test"
  [temporize-func my-db-obj cron-url sms-data]
  (fn request-handler
    [request]
    (let [the-uri (:uri request)
          send-test-sms-url (:send-test-sms-url sms-data)]
;(println " request:" request)
;(println " uri:" the-uri)
;(println " show-data:" show-data)
;(println " my-db-obj:" my-db-obj)
      (if (= the-uri cron-url) (temporize-func))
      (condp = the-uri
        "/" (show-data my-db-obj)
        cron-url (show-data my-db-obj)
        send-test-sms-url (sms-to-phones sms-data)
        "/base-styles.css" (ring-response/resource-response "base-styles.css" {:root ""})
        (ring-response/not-found "404")))))

;; https://stackoverflow.com/questions/54056579/how-to-avoid-global-state-in-clojure-when-using-wrap-reload
;     https://github.com/panta82/clojure-webdev/blob/master/src/webdev/core.clj
(def jetty-reloader #'ring-reload/reloader)

(defn web-reload
  []
  (let [reload-jetty! (jetty-reloader ["src"] true)] (reload-jetty!)))

(defn web-init
  [server-port request-handler]
  (remove-service "web-init")
  (web-reload)
  (let [web-server (ring-jetty/run-jetty request-handler
                                         {:port server-port, :join? false})
        my-kill-web (fn kill-web
                      []
                      (if-not (boolean (resolve 'DB-TEST-NAME)) (println "Killing web-service"))
                      (.stop web-server))]
    (add-service "web-init" my-kill-web)
    my-kill-web))
