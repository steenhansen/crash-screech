
(in-ns 'crash-screech.html-render)
(alias 's 'clojure.spec.alpha)

(def yyyy-mm-dd-hh-ii #"^([\d]{4})-(0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-9]|3[0-1])-\d\d-\d\d.*$")

(s/fdef day-hour-min
  :args (s/cat :check-date (s/and string? #(re-matches yyyy-mm-dd-hh-ii %))) )

(in-ns 'spec-calls)
