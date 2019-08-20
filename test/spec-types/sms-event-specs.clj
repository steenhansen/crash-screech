
(alias 's 'clojure.spec.alpha)



(s/fdef crash-screech.sms-event/make-api-call
  :args (s/cat :sms-data ::sms-data?? :sms-message string? )) 


(s/fdef crash-screech.sms-event/build-sms-send
  :args (s/cat :sms-data ::sms-data?? :testing-sms? boolean? )) 


(s/fdef crash-screech.sms-event/sms-to-phones
  :args (s/cat :sms-data ::sms-data?? :testing-sms? boolean? ))




(s/fdef crash-screech.sms-event/single-cron-fn
  :args (s/cat :scrape-pages-fn  clojure.test/function?
               :my-db-obj coll?                        ;; persistentArrayMap
               :pages-to-check vector?
 :sms-data ::sms-data??
 :testing-sms? boolean? ))



