
(alias 's 'clojure.spec.alpha)

(s/fdef crash-screech.choose-db/get-phone-nums
  :args (s/cat :phone-comma-string ::phones-text))

(s/fdef crash-screech.choose-db/get-db-conn
  :args (s/cat :table-name string?
               :pages-to-check vector?
               :db-type string?
               :config-file ::config-map??))


 (s/fdef crash-screech.choose-db/build-empty-month?
   :args (s/cat :get-all-fn clojure.test/function?))

(s/fdef crash-screech.choose-db/build-today-error?
  :args (s/cat :get-all-fn clojure.test/function?))

(s/fdef crash-screech.choose-db/build-db
  :args (s/cat :table-name string?
               :pages-to-check vector?
               :db-type string?
               :config-file string?
               :environment-utilize string?))


