(alias 's 'clojure.spec.alpha)
(alias 'c-d 'crash-screech.check-data)

(s/fdef c-d/count-string
  :args (s/cat :hay-stack string? 
  :needle-regex #(instance? java.util.regex.Pattern %)))

(s/fdef c-d/trunc-string
  :args (s/cat :the-string string? :num-chars integer?))

(s/fdef c-d/derive-data 
  :args (s/cat :check-record ::data-map??))

(s/fdef c-d/uniquely-id
  :args (s/cat :may-index integer? :many-item ::check-map??))

(s/fdef c-d/ensure-has-date
  :args (s/cat :check-record ::no-date-data-map??))

(s/fdef c-d/prepare-data
 :args (s/cat :check-records (s/coll-of ::data-map??)))

