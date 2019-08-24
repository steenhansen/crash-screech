
(alias 's 'clojure.spec.alpha)
(alias 's-s 'crash-screech.singular-service)



(s/fdef s-s/add-service
  :args (s/cat :spawning-name string?
                :kill-service function? )) 



(s/fdef s-s/remove-service
  :args (s/cat :spawning-name string?)) 


(s/fdef s-s/kill-services
  :args (s/cat)) 



