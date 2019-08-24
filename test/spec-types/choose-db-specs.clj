
(alias 's 'clojure.spec.alpha)
(alias 'c-db 'crash-screech.choose-db)

(s/fdef c-db/get-phone-nums
  :args (s/cat :phone-comma-string ::phones-text??))

(s/fdef c-db/get-db-conn
  :args (s/cat :table-name string?
               :pages-to-check vector?
               :db-type string?
               :config-file ::config-map??))

(s/fdef c-db/build-empty-month?
  :args (s/cat :get-all-fn function?))

(s/fdef c-db/build-today-error?
  :args (s/cat :get-all-fn function?))

(s/fdef c-db/build-db
  :args (s/cat :table-name string?
               :pages-to-check vector?
               :db-type string?
               :config-file string?
               :environment-utilize string?))


