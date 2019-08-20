(alias 's 'clojure.spec.alpha)

(s/fdef crash-screech.check-data/count-string
  :args (s/cat :hay-stack string? 
  :needle-regex #(instance? java.util.regex.Pattern %))
)


(s/fdef crash-screech.check-data/trunc-string
  :args (s/cat :the-string string? :num-chars integer?)
)


(s/fdef  crash-screech.check-data/derive-data 
  :args (s/cat :check-record ::data-map??)
)



(s/fdef crash-screech.check-data/uniquely-id
  :args (s/cat :may-index integer? :many-item ::check-map??)
)


(s/fdef crash-screech.check-data/ensure-has-date
  :args (s/cat :check-record ::no-date-data-map??)
)

(s/fdef crash-screech.check-data/prepare-data
 :args (s/cat :check-records (s/coll-of ::data-map??)))


