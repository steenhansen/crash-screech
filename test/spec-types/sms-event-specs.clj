
(alias 's 'clojure.spec.alpha)
(alias 's-e 'crash-screech.sms-event)

(s/fdef s-e/make-api-call
  :args (s/cat :sms-data ::sms-data?? :sms-message string?))

(s/fdef s-e/build-sms-send
  :args (s/cat :sms-data ::sms-data?? :testing-sms? boolean?))

(s/fdef s-e/sms-to-phones
  :args (s/cat :sms-data ::sms-data?? :testing-sms? boolean?))

(s/fdef s-e/single-cron-fn
  :args (s/cat :scrape-pages-fn  function?
               :my-db-obj coll?              
               :pages-to-check vector?
               :sms-data ::sms-data??
               :testing-sms? boolean?))



