
(in-ns 'crash-screech.years-months)
(alias 's 'clojure.spec.alpha)

(s/fdef adjusted-date
  :args (s/cat :date-str string?))

(in-ns 'spec-calls)
