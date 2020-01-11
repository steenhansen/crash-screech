
(ns general-specs
  (:require [clojure.spec.alpha :as s]))

(def ^:const T-TIME-STAMP #"^((1|2)\d\d\d)-(0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-9]|3[0-1])T\d\d:\d\d:\d\d\.\d\d\dZ$")

(def yyyy #"^((1|2)\d\d\d)$")
(s/def :year-only?/test-specs  (s/and string? #(re-matches yyyy %)))

(def yyyy-mm #"^((1|2)\d\d\d)-(0[1-9]|1[0-2])$")
(s/def :year-month?/test-specs  (s/and string? #(re-matches yyyy-mm %)))

(def yyyy-mm-dd #"^((1|2)\d\d\d)-(0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-9]|3[0-1])$")
(s/def :year-mon-day?/test-specs  (s/and string? #(re-matches yyyy-mm-dd %)))

(def yyyy-mm-dd-hh #"^((1|2)\d\d\d)-(0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-9]|3[0-1])-(2[0-3]|[01]?[0-9]).*$")
(s/def :year-mon-day-hour?/test-specs  (s/and string? #(re-matches yyyy-mm-dd-hh %)))

(def yyyy-mm-dd-hh-ii #"^((1|2)\d\d\d)-(0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-9]|3[0-1])-(2[0-3]|[01]?[0-9])-([0-5][0-9]).*$")
(s/def :year-mon-day-hour-min?/test-specs  (s/and string? #(re-matches yyyy-mm-dd-hh-ii %)))

(def yyyy-mm-dd-hh-ii-ss #"^((1|2)\d\d\d)-(0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-9]|3[0-1])-(2[0-3]|[01]?[0-9])-([0-5][0-9])-([0-5][0-9]).*$")
(s/def :year-mon-day-hour-min-sec?/test-specs  (s/and string? #(re-matches yyyy-mm-dd-hh-ii-ss %)))

(s/def :yyyy-mm?-dd?-hh?-mm?-ss/test-specs (s/or
                                            :yyyy-mm  :year-month?/test-specs
                                            :yyyy-mm  :year-mon-day?/test-specs
                                            :yyyy-mm  :year-mon-day-hour?/test-specs
                                            :yyyy-mm  :year-mon-day-hour-min?/test-specs
                                            :yyyy-mm  :year-mon-day-hour-min-sec?/test-specs))

(s/def :yyyy?-mm?-dd?-hh?-mm?-ss/test-specs (s/or
                                             :yyyy-mm  :year-only?/test-specs
                                             :yyyy-mm  :year-month?/test-specs
                                             :yyyy-mm  :year-mon-day?/test-specs
                                             :yyyy-mm  :year-mon-day-hour?/test-specs
                                             :yyyy-mm  :year-mon-day-hour-min?/test-specs
                                             :yyyy-mm  :year-mon-day-hour-min-sec?/test-specs))

(def phones-regex #"^\s*([0-9]{11})\s*(,\s*[0-9]{11})*\s*$")
(s/def :phones-text?/test-specs  (s/and string? #(re-matches phones-regex %)))

(def EDN-FILE-REGEX #"^\.(\.)?/[-a-zA-Z0-9]+\.edn$")


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;;changed


(s/def :url-date-tuple?/test-specs  (s/keys :req-un []
                                            :opt-un [::the-date      ::the-url ::the-html ::the-accurate ::the-time]))




;;;;changed


(s/def :data-map?/test-specs  (s/keys :req-un []
                                      :opt-un [::the-url ::the-date ::the-html  ::the-accurate ::the-time]))

(s/def :config-map?/test-specs  (s/keys :req-un
                                        [::CRON_URL_DIR ::SEND_TEST_SMS_URL  ::HEROKU_APP_NAME  ::PHONE_NUMBERS ::PORT  ::TILL_API_KEY
                                         ::TILL_URL  ::TILL_USERNAME  ::TESTING_SMS]))

(s/def :check-map?/test-specs  (s/keys :req-un
                                       [::check-url ::check-date ::check-html  ::check-accurate ::check-time ::check-bytes]))

(s/def :edn-filename?/test-specs  (s/and string? #(re-matches EDN-FILE-REGEX %)))

(s/def :env-object?/test-specs  (s/and map? #(re-matches EDN-FILE-REGEX %)))

(s/def :sms-data?/test-specs  (s/keys :req-un
                                      [::till-username ::till-url ::till-api-key ::phone-numbers ::heroku-app-name ::testing-sms?]))

(s/def :mongo-config?/test-specs (s/keys :req-un
                                         [::SEND_TEST_SMS_URL
                                          ::PHONE_NUMBERS
                                          ::HEROKU_APP_NAME
                                          ::MONGODB_URI
                                          ::TILL_USERNAME
                                          ::TILL_API_KEY
                                          ::TILL_URL
                                          ::CRON_URL_DIR
                                          ::TESTING_SMS
                                          ::PORT]))

(s/def :dynamo-config?/test-specs (s/keys :req-un
                                          [::access-key
                                           ::endpoint
                                           ::secret-key]))


;; {} or (first ({a: 17}))


(s/def :map-entry-or-emtpy?/test-specs (s/or :accum-environment  map?
                                             :accum-environment  map-entry?))

(s/def :mongo-db-map?/test-specs  (s/keys :req-un
                                          [::MONGODB_URI]))
