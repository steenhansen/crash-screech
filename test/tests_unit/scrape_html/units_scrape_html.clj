; /test/core_test.clj  
; (-do-tests)



(ns tests-unit.scrape-html.units-scrape-html


  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as spec-alpha]
            [clojure.spec.gen.alpha :as spec-gen]
            [clojure.spec.test.alpha :as spec-test])

  (:require [global-consts  :refer :all])
  (:require [global-vars  :refer :all])

  (:require [crash-screech.singular-service :refer  [kill-services]])


(:require [java-time :refer [local-date?]])




  (:require [tests-unit.scrape-html.count-scrapes-test :as count-scrapes-test])






  (:require [prepare-tests :refer :all])
  (:require [spec-calls :refer :all]))




(defn scrape-html-units []
  (spec-test/instrument 'count-scrapes)
  (count-scrapes-test/test-count-scrapes)


) 


