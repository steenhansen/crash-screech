

(alias 's 'clojure.spec.alpha)
(alias 'h-r 'crash-screech.html-render)


(s/fdef h-r/day-hour-min
  :args (s/cat :check-date  ::year-mon-day-hour-min?? ) )
