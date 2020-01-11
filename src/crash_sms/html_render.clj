
(ns crash-sms.html-render
  (:require [ring.util.response :as ring-response])
  (:require [net.cgrand.enlive-html :as enlive-html])
  (:require [clojure.string :as clj-str])
  (:require [global-consts-vars  :refer :all])
  (:require [crash-sms.years-months  :refer [current-yyyy-mm current-month  trunc-yyyy-mm-dd
                                             prev-month prev-yyyy-mm date-to-yyyy-mm]])
;  (:require [java-time.local :as j-time])
  (:require [global-consts-vars :refer :all]))

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
(declare fill-time)
(declare fill-date)

(defn fill-url [check-url] (enlive-html/do-> (enlive-html/content check-url)))

(declare fill-bytes)

(defn get-two-months
  "has db test"
  [my-db-obj yyyy-mm testing-sms?]     ;;; under-test?
  (let [get-all (:get-all my-db-obj)
        this-y-m (current-yyyy-mm yyyy-mm)
        last-y-m (prev-yyyy-mm yyyy-mm)
        inject-under-test (fn [a-record] (assoc-in a-record [:under-test?] testing-sms?))
        this-months-no-test (get-all this-y-m)
;_ (println "11111111111111111" this-months)
;_ (println "2222222222222222" this-tests)
        last-months-no-test (get-all last-y-m)

        this-months (map inject-under-test this-months-no-test)
        last-months (map inject-under-test last-months-no-test)]
    (if testing-sms?
      (let [this-sorted (sort-by :check-url this-months)
            last-sorted (sort-by :check-url last-months)
            current-months (vec this-sorted)
            previous-months (vec last-sorted)]
        [previous-months current-months])
      (let [this-sorted (sort-by :check-date this-months)
            last-sorted (sort-by :check-date last-months)
            current-months (vec this-sorted)
            previous-months (vec last-sorted)]
        [previous-months current-months]))))

(defn fill-html
  [under-test? check-accurate check-html]
  (if (and under-test? (not check-accurate))
    (enlive-html/do-> (enlive-html/content FAKE-FAIL-CONTENT))
    (enlive-html/do-> (enlive-html/content (if check-accurate "" check-html)))))

(defn fill-time
  [under-test? check-time]
  (if  under-test?
    (enlive-html/do-> (enlive-html/content (str FAKE-SCRAPE-SPEED))
                      (enlive-html/add-class "speed_test"))

    (enlive-html/do-> (enlive-html/content (str check-time))
                      (if (> check-time 2000)
                        (enlive-html/add-class "speed_bad")
                        (if (> check-time 1000)
                          (enlive-html/add-class "speed_average")
                          (enlive-html/add-class "speed_good"))))))

(defn fill-date  [under-test? check-date]
  (if  under-test?
    (enlive-html/do-> (enlive-html/content (trunc-yyyy-mm-dd  check-date)))
    (enlive-html/do-> (enlive-html/content (day-hour-min check-date)))))

(defn fill-bytes [testing-sms? check-bytes]
  (if  testing-sms?
    (enlive-html/do-> (enlive-html/content (str FAKE-SCRAPE-BYTES)))
    (enlive-html/do-> (enlive-html/content (str check-bytes)))))

(enlive-html/defsnippet row-selector
  BASE-HTML-TEMPLATE
  [[:.month_content (enlive-html/nth-of-type 1)] :>
   enlive-html/first-child]
  [{:keys [check-url check-date check-bytes check-html
           check-accurate check-time                    under-test?]}]
  [:div.scrape_accurate]
  (fill-accurate check-accurate)
  [:div.scrape_time]
  (fill-time  under-test? check-time)
  [:div.scrape_date]
  (fill-date  under-test? check-date)
  [:div.scrape_url]
  (fill-url check-url)
  [:div.scrape_bytes]
  (fill-bytes  under-test? check-bytes)
  [:div.scrape_html]
  (fill-html under-test? check-accurate check-html))

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

(defn get-index
  "has db test"
  ([my-db-obj] (get-index my-db-obj (date-to-yyyy-mm (java-time.temporal/instant))))
  ([my-db-obj yyyy-mm] (get-index my-db-obj yyyy-mm false))
  ([my-db-obj yyyy-mm testing-sms?]
   (let [[previous-months current-months] (get-two-months my-db-obj yyyy-mm testing-sms?)       ;;; under-test? sut
         prev-name (prev-month yyyy-mm)
         cur-name (current-month yyyy-mm)
         db-data {:page-title "SFFaudio page checks",
                  :month-sections
                  [{:month-type prev-name, :month-data previous-months}
                   {:month-type cur-name, :month-data current-months}]}]
     (render-parts (index-page db-data)))))
