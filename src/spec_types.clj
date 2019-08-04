(ns  spec-types

  (:require
   [clojure.spec.alpha :as s]
   [clojure.spec.gen.alpha :as spec-gen]
   [clojure.spec.test.alpha :as spec-test])

  (:require [crash-screech.choose-db :refer [get-phone-nums]])
)


(in-ns 'crash-screech.choose-db)
(spec-test/instrument)



; https://blog.taylorwood.io/2017/10/15/fspec.html
; http://leeorengel.com/from-schema-to-spec/ 
; https://adambard.com/blog/domain-modeling-with-clojure-spec/

(def phones-regex #"^\s*([0-9]{10})\s*(,\s*[0-9]{10})*\s*$")    
(s/def ::phones-text  (s/and string? #(re-matches phones-regex %) ))

;(s/fdef crash-screech.choose-db.get-phone-nums 
(s/fdef get-phone-nums 
      :args (s/cat :phone-comma-string ::phones-text  ) 
  )




;(s/fdef build-db
 ; :args (s/cat :table-name string?
  ;              :pages-to-check vector?
   ;             :db-type string? 
    ;           :config-file string?
     ;          :environment-utilize string?
      ;         )
 ; )
(in-ns 'spec-types)
