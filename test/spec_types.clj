(ns spec-types

  (:require
 ; [clojure.spec.alpha :as s]
   [clojure.spec.gen.alpha :as spec-gen]
   [clojure.spec.test.alpha :as spec-test])

  (:require [crash-screech.choose-db :refer :all])
  (:require [crash-screech.html-render :refer :all ])
)

; https://blog.taylorwood.io/2017/10/15/fspec.html
; http://leeorengel.com/from-schema-to-spec/ 
; https://adambard.com/blog/domain-modeling-with-clojure-spec/



(spec-test/instrument)



(in-ns 'crash-screech.choose-db)
(alias 's 'clojure.spec.alpha)

(def phones-regex #"^\s*([0-9]{11})\s*(,\s*[0-9]{11})*\s*$")
(s/def ::phones-text  (s/and string? #(re-matches phones-regex %)))

(s/fdef get-phone-nums
  :args (s/cat :phone-comma-string ::phones-text))

(s/fdef build-db
  :args (s/cat :table-name string?
                :pages-to-check vector?
                :db-type keyword? 
               :config-file string?
               :environment-utilize string?
               )
  )

(in-ns 'crash-screech.html-render)
(alias 's 'clojure.spec.alpha)

(s/fdef fill-bytes
  :args (s/cat :check-bytes
                        number?)
  :ret (s/fspec :args any? :ret any?))


(in-ns 'spec-types)
