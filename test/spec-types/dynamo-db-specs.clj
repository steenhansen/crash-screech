(alias 's 'clojure.spec.alpha)
(alias 'd-d ' crash-screech.dynamo-db)

(s/def ::yyyy-mm-or-yyyy-mm-dd?? (s/or :yyyy-mm  ::year-mon-day??
                                       :yyyy-mm  ::year-month??))

(s/fdef d-d/dynamo-build
  :args (s/cat ::amazonica-config ::dynamo-config??
               ::my-table-name string?
               ::pages-to-check vector?))

