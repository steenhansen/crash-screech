
(alias 's 'clojure.spec.alpha)




(s/fdef crash-screech.singular-service/add-service
  :args (s/cat :spawning-name string?
                :kill-service clojure.test/function? )) 



(s/fdef crash-screech.singular-service/remove-service
  :args (s/cat :spawning-name string?)) 


(s/fdef crash-screech.singular-service/kill-services
  :args (s/cat)) 


(in-ns 'spec-calls)

