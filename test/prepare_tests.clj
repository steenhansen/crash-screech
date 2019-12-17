

;   get-two-months

; https://github.com/clojure/tools.logging   => log4j.properties
; http://www.paullegato.com/blog/logging-clojure-clj-logging-config/

(ns prepare-tests
  (:require [clj-logging-config.log4j :as log-config]
            [clojure.tools.logging :as log])
  (:require [clj-http.client :as http-client])

  (:require [clojure.string :as clj-str])

  (:require [global-consts-vars  :refer :all])

  (:require [crash-screech.choose-db :refer :all])

)





(defn dynamo-on []

)





  (alias 's 'clojure.spec.alpha)


(def yyyy #"^(20(0|1|2)[\d])$")
(s/def :year-only?/test-specs  (s/and string? #(re-matches yyyy %)))


(def yyyy-mm #"^(20(0|1|2)[\d])-(0[1-9]|1[0-2])$")
(s/def :year-month?/test-specs  (s/and string? #(re-matches yyyy-mm %)))

(def yyyy-mm-dd #"^(20(0|1|2)[\d])-(0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-9]|3[0-1])$")
(s/def :year-mon-day?/test-specs  (s/and string? #(re-matches yyyy-mm-dd %)))

(def yyyy-mm-dd-hh #"^(20(0|1|2)[\d])-(0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-9]|3[0-1])-(2[0-3]|[01]?[0-9]).*$")
(s/def :year-mon-day-hour?/test-specs  (s/and string? #(re-matches yyyy-mm-dd-hh %)))

(def yyyy-mm-dd-hh-ii #"^(20(0|1|2)[\d])-(0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-9]|3[0-1])-(2[0-3]|[01]?[0-9])-([0-5][0-9]).*$")
(s/def :year-mon-day-hour-min?/test-specs  (s/and string? #(re-matches yyyy-mm-dd-hh-ii %)))

(def yyyy-mm-dd-hh-ii-ss #"^(20(0|1|2)[\d])-(0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-9]|3[0-1])-(2[0-3]|[01]?[0-9])-([0-5][0-9])-([0-5][0-9]).*$")
(s/def :year-mon-day-hour-min-sec?/test-specs  (s/and string? #(re-matches yyyy-mm-dd-hh-ii-ss %)))

(s/def :yyyy-mm?-dd?-hh?-mm?-ss/test-specs (s/or
;;;;;;;;;;;;         year-mon
          :yyyy-mm  :year-month?/test-specs
          :yyyy-mm  :year-mon-day?/test-specs
          :yyyy-mm  :year-mon-day-hour?/test-specs
          :yyyy-mm  :year-mon-day-hour-min?/test-specs
          :yyyy-mm  :year-mon-day-hour-min-sec?/test-specs

))

(s/def :yyyy?-mm?-dd?-hh?-mm?-ss/test-specs (s/or
          :yyyy-mm  :year-only?/test-specs
          :yyyy-mm  :year-month?/test-specs
          :yyyy-mm  :year-mon-day?/test-specs
          :yyyy-mm  :year-mon-day-hour?/test-specs
          :yyyy-mm  :year-mon-day-hour-min?/test-specs
          :yyyy-mm  :year-mon-day-hour-min-sec?/test-specs

))


;;; replace this with above, and fix year-mon
(s/def :yyyy-mm-or-yyyy-mm-dd?/test-specs (s/or :yyyy-mm  :year-mon-day?/test-specs
                                       :yyyy-mm  :year-month?/test-specs))


















(def phones-regex #"^\s*([0-9]{11})\s*(,\s*[0-9]{11})*\s*$")
(s/def :phones-text?/test-specs  (s/and string? #(re-matches phones-regex %)))





(def EDN-FILE-REGEX #"^\.(\.)?/[-a-zA-Z0-9]+\.edn$")


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;;changed
(s/def :url-date-tuple?/test-specs  (s/keys :req-un []
                       :opt-un [ ::the-date      ::the-url ::the-html ::the-accurate ::the-time]))




;;;;changed
(s/def :data-map?/test-specs  (s/keys :req-un []
    :opt-un [ ::the-url ::the-date ::the-html  ::the-accurate ::the-time]))







(s/def :config-map?/test-specs  (s/keys :req-un
 [ ::CRON_URL_DIR ::SEND_TEST_SMS_URL  ::HEROKU_APP_NAME  ::PHONE_NUMBERS ::PORT  ::TILL_API_KEY
   ::TILL_URL  ::TILL_USERNAME  ::TESTING_SMS]))















(s/def :check-map?/test-specs  (s/keys :req-un
 [ ::check-url ::check-date ::check-html  ::check-accurate ::check-time ::check-bytes ]))




(s/def :edn-filename?/test-specs  (s/and string? #(re-matches EDN-FILE-REGEX %)))

(s/def :env-object?/test-specs  (s/and map? #(re-matches EDN-FILE-REGEX %)))

(s/def :sms-data?/test-specs  (s/keys :req-un
 [ ::till-username ::till-url ::till-api-key ::phone-numbers ::heroku-app-name ::testing-sms?]))

(s/def :mongo-config?/test-specs (s/keys :req-un
[ ::SEND_TEST_SMS_URL
   ::PHONE_NUMBERS
  ::HEROKU_APP_NAME
   ::MONGODB_URI
   ::TILL_USERNAME
   ::TILL_API_KEY
   ::TILL_URL
   ::CRON_URL_DIR
   ::TESTING_SMS
   ::PORT  ]))

(s/def :dynamo-config?/test-specs (s/keys :req-un
[ ::access-key
   ::endpoint
   ::secret-key  ]))


;; {} or (first ({a: 17}))
(s/def :map-entry-or-emtpy?/test-specs (s/or :accum-environment  map?
                                     :accum-environment  map-entry?
                        ))

 (s/def :mongo-db-map?/test-specs  (s/keys :req-un
  [ ::MONGODB_URI]))





















(def ^:const RUN-SPEC-TESTS true)


;(def ^:const T-TIME-TO-RUN-SCRAPE false)



;(def ^:const T-DB-TEST-NAME "zzz3")
(def ^:const T-CONSOLE-TESTS true)
(def ^:const T-DO-DYNAMODB-TESTS false)
(def ^:const T-TIME-STAMP #"^(20(0|1|2)[\d])-(0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-9]|3[0-1])T\d\d:\d\d:\d\d\.\d\d\dZ$")


(defn console-test
  ([test-name] (console-test test-name "-"))
  ([test-name test-area]
   (if T-CONSOLE-TESTS
     (println "..." test-area test-name))))

;  (ddiff/pretty-print (ddiff/diff expected-get-index actual-get-index) )
(defn conform-whitespace
  [rn-text]
  (let [
       n-text (clj-str/replace rn-text  #"\r\n" "\n")
       no-eoln (clj-str/replace n-text  #"\n" "")
        one-spaces  (clj-str/replace no-eoln  #"\s\s+" " ")

        bracket-space  (clj-str/replace one-spaces  #">\s<" "><")

        trimmed-unix (clj-str/trim bracket-space)]
   trimmed-unix))


(defn dampen-mongo-logging
  []
  (log-config/set-logger!
   :level :debug
   :out (org.apache.log4j.FileAppender.
         (org.apache.log4j.EnhancedPatternLayout.
          org.apache.log4j.EnhancedPatternLayout/TTCC_CONVERSION_PATTERN)
         "logs/foo.log"
         true)))

(defn local-dynamodb-on?
  []
  (if T-DO-DYNAMODB-TESTS
    (try
      (http-client/get "http://localhost:8000/shell/")
      (catch Exception e
        (throw  (Exception. " **** ERROR DynamoDB is not running on - http://localhost:8000"))))))

(defn local-mongo-on?
  []
  (try (http-client/get "http://localhost:27017")
       (catch Exception e
         (throw
          (Exception.
           " **** ERROR MongoDB is not running on - http://localhost:27017")))))

(defn strip-white-space [my-text] (clj-str/replace my-text #"\s" ""))

;; reset-expected-to-actual-data
(defn reset-test-to-actual-data
  [test-file actual-data]
  (let  [test-path (str SCRAPED-TEST-DATA test-file)]
    (println test-file test-path)
 ; (spit test-path actual-data)
    ))

(defn print-block
  []
  (println
   "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"))

(defn sms-is-in-test [db-type]
  (let [[_ _ _ sms-data] (build-db T-DB-TEST-NAME
                                   []
                                   db-type
                                   TEST-CONFIG-FILE
                                   IGNORE-ENV-VARS)
        testing-sms? (:testing-sms? sms-data)]
    (if (not testing-sms?)  (throw
                             (Exception.
                              " **** ERROR :TESTING_SMS is NOT false")))))

(defn is-string-number
  [num-as-str]
  (try
    (let [the-number (Integer/parseInt num-as-str)
          is-number (number? the-number)
          bigger-0 (< 0 the-number)]
      (and is-number bigger-0))
    (catch Exception e
      false)))

(defn is-url-dir
  [url-dir-leading-slash]
  (try
    (let [the-slash (first url-dir-leading-slash)
          the-dir (subs url-dir-leading-slash 1)]
      (and (= the-slash "/") (string? the-dir)))
    (catch Exception e
      false)))
