

(ns tests-dynamo-db
  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as spec-alpha]
            [clojure.spec.gen.alpha :as spec-gen]
            [clojure.spec.test.alpha :as spec-test])

  (:require [crash-sms.choose-db  :refer :all])
  (:require [global-consts-vars  :refer :all])

  (:require [crash-sms.config-args :refer [make-config compact-hash]])
  (:require [crash-sms.dynamo-db :refer :all])

  (:require [java-time :refer [local-date?]])


  (:require [prepare-tests :refer :all])
)
(alias 's 'clojure.spec.alpha)
(alias 'd-d ' crash-sms.dynamo-db)


(s/fdef d-d/dynamo-build
  :args (s/cat ::amazonica-config :dynamo-config?/test-specs
               ::my-table-name string?
               ::pages-to-check vector?))

(defn dynamo-db-specs []
    (print-line "Speccing dynamo-db")
      (spec-test/instrument)
      (spec-test/instrument 'dynamo-build))


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

(defn choose-db-specs []
  (print-line "Speccing choose-db")
      (spec-test/instrument)
      (spec-test/instrument 'dynamo-build))

(defn do-tests []
  (reset! *run-all-tests* true)
  (reset! *testing-namespace* "fast-all-tests-running")
(dynamo-db-specs)
  (run-tests 'tests-dynamo-db)
  (reset! *testing-namespace* "no-tests-running"))




(defn fast-tests []
  (reset! *run-all-tests* false)
  (reset! *testing-namespace* "fast-all-tests-running")
(dynamo-db-specs)
  (run-tests 'tests-dynamo-db)
  (reset! *testing-namespace* "no-tests-running"))
