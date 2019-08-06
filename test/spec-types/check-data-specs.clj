(in-ns 'crash-screech.check-data)
(alias 's 'clojure.spec.alpha)

(s/fdef count-string
  :args (s/cat :hay-stack string? 
  :needle-regex #(instance? java.util.regex.Pattern %))
)

(s/fdef trunc-string
  :args (s/cat :the-string string? :num-chars integer?)
)

(s/def ::data-map?  (s/keys :req-un
 [ ::the-url ::the-date ::the-html  ::the-accurate ::the-time]))

(s/fdef  derive-data 
  :args (s/cat :check-record ::data-map?)
)


(s/def ::check-map?  (s/keys :req-un
 [ ::check-url ::check-date ::check-html  ::check-accurate ::check-time ::check-bytes ]))

(s/fdef uniquely-id
  :args (s/cat :may-index integer? :many-item ::check-map?)
)

(s/def ::no-date-data-map?  (s/keys :req-un
 [ ::the-url ::the-html  ::the-accurate ::the-time]))

(s/fdef ensure-has-date
  :args (s/cat :check-record ::no-date-data-map?)
)

(s/fdef prepare-data
 :args (s/cat :check-records (s/coll-of ::data-map?)))


(in-ns 'spec-calls)
