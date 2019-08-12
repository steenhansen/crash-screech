
(in-ns 'crash-screech.scrape-html)
(alias 's 'clojure.spec.alpha)



(s/fdef count-scrapes
  :args (s/cat :some-html string?)) 

(in-ns 'spec-calls)
