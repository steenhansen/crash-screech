
(alias 's 'clojure.spec.alpha)
(alias 'w-s 'crash-screech.html-render)

(s/fdef w-s/make-request-fn
  :args (s/cat :temporize-func function?
               :my-db-obj coll?
               :cron-url string?
               :sms-data ::sms-data??
               :testing-sms? boolean?))

