
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

(defn enough-sections?
  [the-html css-match wanted-matches]
  (let [actual-sections (matching-css-sections the-html css-match)
        actual-matches (count actual-sections)]
    (if (>= actual-matches wanted-matches)
      {:actual-matches actual-matches, :the-accurate true}
      {:actual-matches actual-matches, :the-accurate false})))

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

(defn first-error-today?
  [prev-errors-today? my-db-obj]
  (let [today-error? (:today-error? my-db-obj)
        now-error? (today-error?)]
    (if prev-errors-today? false now-error?)))

(defn send-first-day-sms?
  [my-db-obj]
  (let [empty-month? (:empty-month? my-db-obj)
        first-check-of-month? (empty-month?)]
    first-check-of-month?))

(defn scrape-pages-fn
  [my-db-obj pages-to-check time-fn sms-send-fn read-from-web]
  (let [today-error? (:today-error? my-db-obj)
        send-hello-sms? (send-first-day-sms? my-db-obj)
        prev-errors-today? (today-error?)
        put-item (:put-item my-db-obj)]
    (doseq [check-page-obj pages-to-check
            :let [{:keys [check-page enlive-keys at-least]} check-page-obj
                  start-timer (System/currentTimeMillis)
                  web-html (read-html check-page read-from-web)
                  end-timer (System/currentTimeMillis)
                  the-time (- end-timer start-timer)



                  {:keys [actual-matches the-accurate]}
                  (enough-sections? web-html enlive-keys at-least)
                  the-url (real-slash-url check-page)


(println "the-time " the-time the-url)

                  the-date (time-fn)
                  the-html (remove-tags web-html)
                  check-record (compact-hash the-url
                                             the-date
                                             the-html
                                             the-accurate
                                             the-time)]]
      (put-item check-record))
    (let [send-err-sms? (first-error-today? prev-errors-today? my-db-obj)
          no-sms-sent []]
      (if send-hello-sms? (sms-send-fn SMS-NEW-MONTH))
      (if send-err-sms?
        (sms-send-fn SMS-FOUND-ERROR)
        no-sms-sent)))); NB return values used





