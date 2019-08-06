
(in-ns 'crash-screech.choose-db)
(alias 's 'clojure.spec.alpha)
;(alias 'test 'clojure.spec.alpha)

(def phones-regex #"^\s*([0-9]{11})\s*(,\s*[0-9]{11})*\s*$")
(s/def ::phones-text  (s/and string? #(re-matches phones-regex %)))


(s/def ::config-map?  (s/keys :req-un
 [ ::CRON_URL_DIR ::SEND_TEST_SMS_URL  ::HEROKU_APP_NAME  ::PHONE_NUMBERS ::PORT  ::TILL_API_KEY
   ::TILL_URL  ::TILL_USERNAME  ::TESTING_SMS]))

(s/fdef get-db-conn
  :args (s/cat :table-name string?
               :pages-to-check vector?
               :db-type keyword?
               :config-file ::config-map?))


(s/fdef build-empty-month?
  :args (s/cat :get-all-fn clojure.test/function?))

(s/fdef  build-today-error?
  :args (s/cat :get-all-fn clojure.test/function?))


(s/fdef get-phone-nums
  :args (s/cat :phone-comma-string ::phones-text))

(s/fdef build-db
  :args (s/cat :table-name string?
               :pages-to-check vector?
               :db-type keyword?
               :config-file string?
               :environment-utilize string?))

(in-ns 'spec-calls)
