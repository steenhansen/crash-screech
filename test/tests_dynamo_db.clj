

(ns tests-dynamo-db
  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as t])
  (:require [crash-sms.data-store  :refer :all])
  (:require [global-consts-vars  :refer :all])
  (:require [crash-sms.config-args :refer [make-config compact-hash]])
  (:require [crash-sms.dynamo-db :refer :all])
  (:require [java-time :refer [local-date?]])
  (:require [prepare-tests :refer :all])
)

(s/check-asserts true)

(s/fdef dynamo-build
  :args (s/cat ::amazonica-config :dynamo-config?/test-specs
               ::my-table-name string?
               ::pages-to-check vector?))

(defn dynamo-db-specs []
    (print-line "Speccing dynamo-db")
      (t/instrument)
      (t/instrument 'dynamo-build))


(s/fdef build-empty-month?
  :args (s/cat :get-all-fn function?))

(s/fdef build-today-error?
  :args (s/cat :get-all-fn function?))

(s/fdef build-db
  :args (s/cat :table-name string?
               :pages-to-check vector?
               :db-type string?
               :config-file string?
               :environment-utilize string?))

(defn data-store-specs []
  (print-line "Speccing data-store")
      (t/instrument)
      (t/instrument 'dynamo-build))

(defn all-tests []
  (reset! *T-REAL-DB-ASSERTIONS* true)
  (reset! *T-ASSERTIONS-VIA-REPL* false)
(dynamo-db-specs)
  (run-tests 'tests-dynamo-db)
  (reset! *T-ASSERTIONS-VIA-REPL* true))

(defn fast-tests []
  (reset! *T-REAL-DB-ASSERTIONS* false)
  (reset! *T-ASSERTIONS-VIA-REPL* false)
(dynamo-db-specs)
  (run-tests 'tests-dynamo-db)
  (reset! *T-ASSERTIONS-VIA-REPL* true))
