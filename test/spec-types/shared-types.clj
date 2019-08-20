

(alias 's 'clojure.spec.alpha)


(def phones-regex #"^\s*([0-9]{11})\s*(,\s*[0-9]{11})*\s*$")
(s/def ::phones-text  (s/and string? #(re-matches phones-regex %)))


(def yyyy-mm-dd-hh-ii #"^([\d]{4})-(0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-9]|3[0-1])-\d\d-\d\d.*$")
(s/def ::year-mon-day-hour-min  (s/and string? #(re-matches yyyy-mm-dd-hh-ii %)))




(s/def ::config-map??  (s/keys :req-un
 [ ::CRON_URL_DIR ::SEND_TEST_SMS_URL  ::HEROKU_APP_NAME  ::PHONE_NUMBERS ::PORT  ::TILL_API_KEY
   ::TILL_URL  ::TILL_USERNAME  ::TESTING_SMS]))


(s/def ::data-map??  (s/keys :req-un
 [ ::the-url ::the-date ::the-html  ::the-accurate ::the-time]))


(s/def ::check-map??  (s/keys :req-un
 [ ::check-url ::check-date ::check-html  ::check-accurate ::check-time ::check-bytes ]))


(s/def ::no-date-data-map??  (s/keys :req-un
 [ ::the-url ::the-html  ::the-accurate ::the-time]))

(def EDN-FILE-REGEX #"^\.(\.)?/[-a-zA-Z0-9]+\.edn$")

(s/def ::edn-filename  (s/and string? #(re-matches EDN-FILE-REGEX %)))

(s/def ::env-object  (s/and map? #(re-matches EDN-FILE-REGEX %)))

(s/def ::sms-data??  (s/keys :req-un
 [ ::till-username ::till-url ::till-api-key ::phone-numbers ::heroku-app-name ::testing-sms?]))

