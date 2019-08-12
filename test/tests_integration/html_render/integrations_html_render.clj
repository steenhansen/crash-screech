; /test/core_test.clj  
; (-do-tests)



(ns tests-integration.html-render.integrations-html-render


  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as spec-alpha]
            [clojure.spec.gen.alpha :as spec-gen]
            [clojure.spec.test.alpha :as spec-test])

  (:require [global-consts  :refer :all])
  (:require [global-vars  :refer :all])

  (:require [crash-screech.singular-service :refer  [kill-services]])


(:require [java-time :refer [local-date?]])




  (:require [tests-integration.html-render.get-index-test :as get-index-test])





  (:require [prepare-tests :refer :all])
  (:require [spec-calls :refer :all]))



(defn html-render-integrations []
   (spec-test/instrument 'get-index)
  (get-index-test/test-get-index)
)




