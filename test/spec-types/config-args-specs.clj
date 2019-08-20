
(alias 's 'clojure.spec.alpha)





(s/fdef crash-screech.config-args/read-config-file
  :args (s/cat :config-file ::edn-filename))



;; (s/fdef crash-screech.config-args/merge-environment
;;   :args (s/cat :accum-environment map?
;;                :env-object vector?)
;; )

