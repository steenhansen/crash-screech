(alias 's 'clojure.spec.alpha)
(alias 'c-s 'crash-screech.cron-service)

(s/fdef c-s/build-cron-func
  :args (s/cat :cron-job function?
               :my-db-obj coll?
               :pages-to-check vector?
               :sms-data ::sms-data??))

(s/fdef c-s/start-cron
  :args (s/cat :cron-job function?
               :my-db-obj coll?
               :pages-to-check vector?
               :sms-data ::sms-data??))


(s/fdef c-s/cron-init
  :args (s/cat :cron-job function?
               :my-db-obj coll?
               :pages-to-check vector?
               :sms-data ::sms-data??))

