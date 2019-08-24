
(alias 's 'clojure.spec.alpha)
(alias 's-h 'crash-screech.scrape-html)


(s/fdef s-h/count-scrapes
  :args (s/cat :some-html string?)) 

