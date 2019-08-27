

(alias 's 'clojure.spec.alpha)
(alias 'h-r 'crash-screech.html-render)


(s/fdef h-r/day-hour-min
  :args (s/cat :check-date  ::year-mon-day-hour-min?? ) )



(s/fdef h-r/get-two-months
  :args (s/cat :my-db-obj  coll?
               :yyyy-mm   ::year-month??   ) )



(s/fdef h-r/get-index
  :args (s/alt :unary (s/cat :my-db-obj  coll?   )
               :binary (s/cat :my-db-obj  coll?
                      :yyyy-mm   ::year-month??   ) ))

