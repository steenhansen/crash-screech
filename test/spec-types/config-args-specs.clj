
(alias 's 'clojure.spec.alpha)
(alias 'c-a 'crash-screech.config-args)

(s/fdef c-a/read-config-file
  :args (s/cat :config-file ::edn-filename??))

(s/fdef c-a/merge-environment
  :args (s/cat :accum-environment ::map-entry-or-emtpy??
               :env-object vector?))

(s/fdef c-a/make-config
  :args (s/cat :db-type string?
               :config-file ::edn-filename??
               :environment-utilize string? ))


