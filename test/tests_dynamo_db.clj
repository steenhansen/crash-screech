

(ns tests-dynamo-db
  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as spec-alpha]
            [clojure.spec.gen.alpha :as spec-gen]
            [clojure.spec.test.alpha :as spec-test])

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
    (println "Speccing dynamo-db")
      (spec-test/instrument)
      (spec-test/instrument 'dynamo-build))


(deftest unit-dynamo-build
    (let [ db-type  USE_AMAZONICA_DB
           the-config (make-config db-type TEST-CONFIG-FILE IGNORE-ENV-VARS)
          pages-to-check [{:check-page "www.sffaudio.com",
                         :enlive-keys [:article :div.blog-item-wrap],
                         :at-least 1}]
                   a-dynamo-db       (dynamo-build the-config  T-TEST-COLLECTION pages-to-check)]
      (console-test "unit-dynamo-build" "dynamo-db")
     (is (function?(:my-delete-table a-dynamo-db)))
     (is (function?(:my-db-alive? a-dynamo-db)))
     (is (function?(:my-purge-table a-dynamo-db)))
     (is (function?(:my-get-all a-dynamo-db)))
     (is (function?(:my-get-url a-dynamo-db)))
     (is (function?(:my-put-item a-dynamo-db)))
     (is (function?(:my-put-items a-dynamo-db)))
(is (= (count a-dynamo-db ) 7))
))





(defn do-tests []
(dynamo-db-specs)
  (run-tests 'tests-dynamo-db))
