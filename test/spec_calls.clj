(ns spec-calls

  (:require
   [clojure.spec.test.alpha :as spec-test])

  (:require [crash-screech.check-data :refer :all])
  (:require [crash-screech.choose-db :refer :all])
  (:require [crash-screech.html-render :refer :all ])
  (:require [crash-screech.scrape-html :refer :all ])
)

; https://blog.taylorwood.io/2017/10/15/fspec.html
; http://leeorengel.com/from-schema-to-spec/ 
; https://adambard.com/blog/domain-modeling-with-clojure-spec/



(spec-test/instrument)

(load "spec-types/check-data-specs")
(load "spec-types/choose-db-specs")
(load "spec-types/html-render-specs")
(load "spec-types/scrape-html-specs")
(load "spec-types/years-months-specs")


