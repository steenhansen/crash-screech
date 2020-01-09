
(ns crash-sms.scrape-html
  (:require [net.cgrand.enlive-html :as enlive-html])
  (:require [clojure.string :as clj-str])
  (:require [clj-http.client :as http-client])
  (:require [crash-sms.years-months  :refer [instant-time-fn]])
  (:require [global-consts-vars  :refer :all])
  (:require [crash-sms.config-args :refer [compact-hash]])
  (:require [crash-sms.check-data :refer [count-string]]))

(defn count-scrapes
  [some-html]
  (let [number-scrapes (count-string some-html #"a_countable_scrape")]
    number-scrapes))


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

(comment
  (read-html  WWW-SFFAUDIO-COM true)
;
)
(defn read-html
  [check-page read-from-web?]
  (if read-from-web?
    (try
      (let [time-stamp (instant-time-fn)
            no-semi-time (clj-str/replace time-stamp #":" "-")
            no-dot-time (clj-str/replace no-semi-time #"\." "-")
            no-cache-url (str "https://" check-page no-dot-time)]
        (:body (http-client/get no-cache-url)))
      (catch Exception e (str "404 found for " check-page)))
    (let [no-slash-fn  (clj-str/replace check-page #"\/" "~")
          no-quest-fn  (clj-str/replace no-slash-fn #"\?" "+")
          safe-filename  (clj-str/replace no-quest-fn #"\=" "_")] ;; valid-filename-chars   -+_
      (slurp (str SCRAPED-TEST-DATA safe-filename)))))

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
    (if prev-errors-today? false now-error?)))

(defn send-sms-message [prev-errors-today? my-db-obj send-hello-sms? sms-send-fn]
  (let [send-err-sms? (first-error-today? prev-errors-today? my-db-obj)
        no-sms-sent ""]
    (if send-err-sms?
      (sms-send-fn SMS-FOUND-ERROR)
      (if send-hello-sms?
        (sms-send-fn SMS-NEW-MONTH)
        no-sms-sent))))

(comment
  (let [pages-OK-check [{:check-page WWW-SFFAUDIO-COM   :enlive-keys SFFAUDIO-CHECK-KEYS :at-least HTML-OK-COUNT}]
        pages-FAIL-check [{:check-page WWW-SFFAUDIO-COM :enlive-keys SFFAUDIO-CHECK-KEYS :at-least HTML-FAIL-COUNT}]
        [my-db-obj _ _ sms-data] (build-db T-TEST-COLLECTION pages-OK-check USE_MONGER_DB TEST-CONFIG-FILE IGNORE-ENV-VARS)
        purge-table (:purge-table my-db-obj)
        testing-sms? true
        sms-send-fn (build-sms-send sms-data testing-sms?)
        read-from-web? true
        _ (purge-table)
        start-month-sms (scrape-pages-fn my-db-obj pages-OK-check   instant-time-fn sms-send-fn read-from-web?)
        no-error-sms    (scrape-pages-fn my-db-obj pages-OK-check   instant-time-fn sms-send-fn read-from-web?)
        error-sms       (scrape-pages-fn my-db-obj pages-FAIL-check instant-time-fn sms-send-fn read-from-web?)]
    [start-month-sms no-error-sms error-sms])

  (let [pages-OK-check [{:check-page  WWW-SFF-SEARCH   :enlive-keys SFF-SEARCH-CHECK-KEYS :at-least HTML-OK-COUNT}]
        pages-FAIL-check [{:check-page WWW-SFF-SEARCH :enlive-keys SFF-SEARCH-CHECK-KEYS :at-least HTML-FAIL-COUNT}]
        [my-db-obj _ _ sms-data] (build-db T-TEST-COLLECTION pages-OK-check USE_MONGER_DB TEST-CONFIG-FILE IGNORE-ENV-VARS)
        purge-table (:purge-table my-db-obj)
        testing-sms? true
        sms-send-fn (build-sms-send sms-data testing-sms?)
        read-from-web? true
        _ (purge-table)
        start-month-sms (scrape-pages-fn my-db-obj pages-OK-check   instant-time-fn sms-send-fn read-from-web?)
        no-error-sms    (scrape-pages-fn my-db-obj pages-OK-check   instant-time-fn sms-send-fn read-from-web?)
        error-sms       (scrape-pages-fn my-db-obj pages-FAIL-check instant-time-fn sms-send-fn read-from-web?)]
    [start-month-sms no-error-sms error-sms])

  (let [the-check-pages (make-check-pages 0)
        [my-db-obj _ _ sms-data] (build-db T-TEST-COLLECTION the-check-pages USE_MONGER_DB TEST-CONFIG-FILE IGNORE-ENV-VARS)
        purge-table (:purge-table my-db-obj)
        testing-sms? true
        sms-send-fn (build-sms-send sms-data testing-sms?)
        read-from-web? true
        _ (purge-table)
        all-sms (scrape-pages-fn my-db-obj the-check-pages instant-time-fn sms-send-fn read-from-web?)]
    [all-sms])


  (let [pages-OK-check [{:check-page "www.sffaudio.com/not-exist-404"   :enlive-keys SFFAUDIO-CHECK-KEYS :at-least HTML-OK-COUNT}]
        [my-db-obj _ _ sms-data] (build-db T-TEST-COLLECTION pages-OK-check USE_MONGER_DB TEST-CONFIG-FILE IGNORE-ENV-VARS)
        purge-table (:purge-table my-db-obj)
        testing-sms? true
        sms-send-fn (build-sms-send sms-data testing-sms?)
        read-from-web? true
        _ (purge-table)
        start-month-sms (scrape-pages-fn my-db-obj pages-OK-check   instant-time-fn sms-send-fn read-from-web?)]
    [start-month-sms])
  ["Found and error"])

(defn scrape-pages-fn
  [my-db-obj pages-to-check time-fn sms-send-fn read-from-web?]
  (let [{:keys [_today-error? send-hello-sms? prev-errors-today? put-item]} (get-db-objs my-db-obj)]
    (doseq [check-page-obj pages-to-check
            :let [{:keys [check-page enlive-keys at-least]} check-page-obj
                  start-timer (System/currentTimeMillis)
                  web-html (read-html check-page read-from-web?)
                  the-time (figure-interval start-timer)
                  start-process (System/currentTimeMillis)
                  {:keys [actual-matches the-accurate]} (enough-sections? web-html enlive-keys at-least)
                  the-url (clj-str/replace check-page #"_" "/")
                  the-date (time-fn)
                  the-html (remove-tags web-html)
                  process-time (figure-interval start-process)
                  check-record (compact-hash the-url
                                             the-date
                                             the-html
                                             the-accurate
                                             the-time)]]
      (put-item check-record)
      (Thread/sleep LOCAL-DATA-MOCKED-DELAY)          ; when doing a mocked test of data in file system, timestamps can be the same
      )
    (let [sms-mess-result (send-sms-message prev-errors-today? my-db-obj send-hello-sms? sms-send-fn)]
      sms-mess-result)))   ;; testing looks at array returned
