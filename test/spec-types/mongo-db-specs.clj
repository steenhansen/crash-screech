(alias 's 'clojure.spec.alpha)
(alias 'm-d 'crash-screech.mongo-db)


(s/def ::yyyy-mm-or-yyyy-mm-dd?? (s/or :yyyy-mm  ::year-mon-day??
                                       :yyyy-mm  ::year-month??))

(s/fdef m-d/next-date-time
  :args (s/cat :yyyy-mm  ::yyyy-mm-or-yyyy-mm-dd??))

(s/fdef m-d/mongolabs-build
  :args (s/cat ::mongolabs-config ::mongo-config??
               ::my-collection string?
               ::pages-to-check vector?))

