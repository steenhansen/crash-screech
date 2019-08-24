

(alias 's 'clojure.spec.alpha)


(def phones-regex #"^\s*([0-9]{11})\s*(,\s*[0-9]{11})*\s*$")
(s/def ::phones-text??  (s/and string? #(re-matches phones-regex %)))


(def yyyy-mm-dd-hh-ii #"^([\d]{4})-(0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-9]|3[0-1])-\d\d-\d\d.*$")
(s/def ::year-mon-day-hour-min??  (s/and string? #(re-matches yyyy-mm-dd-hh-ii %)))



(def yyyy-mm-dd #"^([\d]{4})-(0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-9]|3[0-1])$")
(s/def ::year-mon-day??  (s/and string? #(re-matches yyyy-mm-dd %)))



(def yyyy-mm #"^([\d]{4})-(0[1-9]|1[0-2])$")
(s/def ::year-month??  (s/and string? #(re-matches yyyy-mm %)))







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

(s/def ::edn-filename??  (s/and string? #(re-matches EDN-FILE-REGEX %)))

(s/def ::env-object??  (s/and map? #(re-matches EDN-FILE-REGEX %)))

(s/def ::sms-data??  (s/keys :req-un
 [ ::till-username ::till-url ::till-api-key ::phone-numbers ::heroku-app-name ::testing-sms?]))




(s/def ::mongo-config?? (s/keys :req-un
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


;; {} or (first ({a: 17}))
(s/def ::map-entry-or-emtpy?? (s/or :accum-environment  map?
                                     :accum-environment  map-entry?
                        ))






 (s/def ::mongo-db-map??  (s/keys :req-un
  [ ::MONGODB_URI]))
