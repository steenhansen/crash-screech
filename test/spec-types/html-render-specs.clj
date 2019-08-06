
(in-ns 'crash-screech.html-render)
(alias 's 'clojure.spec.alpha)

(s/fdef fill-bytes
  :args (s/cat :check-bytes number?))


(in-ns 'spec-calls)
