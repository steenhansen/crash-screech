
(ns crash-screech.scrape-html

  (:require [net.cgrand.enlive-html :as enlive-html])
  (:require [clojure.string :as clj-str])
  (:require [clj-http.client :as http-client])

  (:require [global-consts-vars  :refer :all])

  (:require [crash-screech.config-args :refer [compact-hash]])
  (:require [crash-screech.check-data :refer [count-string]]))

(defn count-scrapes
  [some-html]
  (let [number-scrapes (count-string some-html #"a_countable_scrape")]
    number-scrapes))

(defn get-the-name
  [accum item]
  (let [[head-name head-value] item]
    (if (= head-name "Content-Length") head-value accum)))

(defn matching-css-sections
  [pages-html css-match]
  (map enlive-html/text
       (enlive-html/select (-> pages-html
                               java.io.StringReader.
                               enlive-html/html-resource)
                           css-match)))

(defn matches-accurate
  [actual-matches wanted-matches]
  (if (>= actual-matches wanted-matches)
    {:actual-matches actual-matches, :the-accurate true}
    {:actual-matches actual-matches, :the-accurate false}))

(defn enough-sections?
  [the-html css-match wanted-matches]
  (if (< LARGE-RECORD-COUNT wanted-matches)
    (let [name-key (name (first css-match))
          my-regex (re-pattern (str START-REGEX-LITERAL  name-key  END-REGEX-LITERAL))
          split-vector  (clj-str/split the-html my-regex)
          actual-matches (count split-vector)]
      (matches-accurate actual-matches wanted-matches))
    (let [actual-sections (matching-css-sections the-html css-match)
          actual-matches (count actual-sections)]
      (matches-accurate actual-matches wanted-matches))))

(defn remove-tags
  [the-html]
  (let [no-head (clj-str/replace the-html #"<head[\w\W]+?</head>" "")
        no-styles (clj-str/replace no-head #"<style[\w\W]+?</style>" "")
        no-js (clj-str/replace no-styles #"<script[\w\W]+?</script>" "")
        no-end-tags (clj-str/replace no-js #"</[\w\W]+?>" "")
        no-start-tags (clj-str/replace no-end-tags #"<[\w\W]+?>" "")
        no-multi-spaces (clj-str/replace no-start-tags #"\s\s+" ".")]
    no-multi-spaces))

(defn real-slash-url [check-page] (clj-str/replace check-page #"_" "/"))

(defn read-html
  [check-page read-from-web]
  (if read-from-web
    (let [under-to-slashes (clj-str/replace check-page #"_" "/")]
      (:body (http-client/get (str "https://" under-to-slashes))))
    (slurp (str SCRAPED-TEST-DATA check-page))))

(defn send-first-day-sms?
  [my-db-obj]
  (let [empty-month? (:empty-month? my-db-obj)
        first-check-of-month? (empty-month?)]
    first-check-of-month?))

(defn figure-interval
  [start-time]
  (let [end-time (System/currentTimeMillis)
        total-time (- end-time start-time)]
    total-time))

(defn get-db-objs [my-db-obj]
  (let [today-error? (:today-error? my-db-obj)
        send-hello-sms? (send-first-day-sms? my-db-obj)
        prev-errors-today? (today-error?)
        put-item (:put-item my-db-obj)]
    (compact-hash today-error? send-hello-sms? prev-errors-today? put-item)))

(defn first-error-today?
  [prev-errors-today? my-db-obj]
  (let [today-error? (:today-error? my-db-obj)
        now-error? (today-error?)]
 ;   (println "scrape-html.first-error-today? 7 prev-errors-today?" prev-errors-today?)
  ;  (println "scrape-html.first-error-today? 7 now-error?" now-error?)
    (if prev-errors-today? false now-error?)))

(defn send-sms-message [prev-errors-today? my-db-obj send-hello-sms? sms-send-fn]
  (let [send-err-sms? (first-error-today? prev-errors-today? my-db-obj)
        no-sms-sent []]

   ; (println "scrape-html.send-sms-message 0 prev-errors-today?" prev-errors-today?)

   ; (println "scrape-html.send-sms-message 0 send-err-sms?" send-err-sms?)
  ;  (println "scrape-html.send-sms-message 1 send-hello-sms?" send-hello-sms?)
  ;  (println "scrape-html.send-sms-message 2 SMS-NEW-MONTH"  SMS-NEW-MONTH)
  ;  (println "scrape-html.send-sms-message 3 (sms-send-fn SMS-NEW-MONTH)"  (sms-send-fn SMS-NEW-MONTH))


      (if send-err-sms?
        (sms-send-fn SMS-FOUND-ERROR)
        (if send-hello-sms?
          (sms-send-fn SMS-NEW-MONTH)
           no-sms-sent
      ))


    ;; (if send-hello-sms?
    ;;   (sms-send-fn SMS-NEW-MONTH)
    ;;   (if send-err-sms?
    ;;     (sms-send-fn SMS-FOUND-ERROR)
    ;;     no-sms-sent))


)
)

(defn scrape-pages-fn
  [my-db-obj pages-to-check time-fn sms-send-fn read-from-web]


(println "&&&&&&&&&&&&" (type time-fn) time-fn (time-fn) )

  (let [{:keys [today-error? send-hello-sms? prev-errors-today? put-item]} (get-db-objs my-db-obj)]
    (doseq [check-page-obj pages-to-check
            :let [{:keys [check-page enlive-keys at-least]} check-page-obj
                  start-timer (System/currentTimeMillis)
                  web-html (read-html check-page read-from-web)
                  the-time (figure-interval start-timer)
                  start-process (System/currentTimeMillis)
                  {:keys [actual-matches the-accurate]}
                  (enough-sections? web-html enlive-keys at-least)
                  the-url (real-slash-url check-page)
                  the-date (time-fn)
                  the-html (remove-tags web-html)
                  process-time (figure-interval start-process)
                  check-record (compact-hash the-url
                                             the-date
                                             the-html
                                             the-accurate
                                             the-time)]]
  ;    (if (not @*we-be-testing*)
   ;     (println "the-time the-url process-time" the-time the-url process-time))
(println "*************************** the-record" the-date the-time the-url)     
 (put-item check-record))
    (reset! global-consts-vars/*pages-were-scraped* true)
    (let [sms-mess-result (send-sms-message prev-errors-today? my-db-obj send-hello-sms? sms-send-fn)]
    ;(println "scrape-html.scrape-pages-fn 230u0us ****** " sms-mess-result)
      sms-mess-result)
 ;(println "scrape-html.scrape-pages-fn 230u0us ****** " (send-sms-message prev-errors-today? my-db-obj send-hello-sms? sms-send-fn))
  ;  (send-sms-message prev-errors-today? my-db-obj send-hello-sms? sms-send-fn)
))  ; NB return values used





