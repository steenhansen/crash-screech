

(ns tests-html-render

  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as spec-alpha]
            [clojure.spec.gen.alpha :as spec-gen]
            [clojure.spec.test.alpha :as spec-test])

  (:require [global-consts-vars  :refer :all])

  (:require [crash-sms.choose-db :refer [build-db]])
  (:require [crash-sms.scrape-html :refer [read-html]])
  (:require [crash-sms.html-render :refer :all])

  (:require [java-time :refer [local-date?]])

  (:require [prepare-tests :refer :all])

  (:require [lambdaisland.deep-diff :as ddiff])

 (:require [text-diff :refer :all])

  )



(alias 's 'clojure.spec.alpha)
(alias 'h-r 'crash-sms.html-render)


(s/fdef h-r/day-hour-min
  :args (s/cat :check-date  :year-mon-day-hour-min?/test-specs ) )



(s/fdef h-r/get-two-months
  :args (s/cat :my-db-obj  coll?
               :yyyy-mm   :year-month?/test-specs
                :testing-sms? boolean?  ))


(s/fdef h-r/get-index
  :args (s/alt :unary (s/cat :my-db-obj  coll?   )
               :binary (s/cat :my-db-obj  coll?
                      :yyyy-mm   :year-month?/test-specs   )

               :ternary (s/cat :my-db-obj    coll?
                               :yyyy-mm      :year-month?/test-specs
                               :testing-sms? boolean?   )

 ))


(defn html-render-specs []
  (print-line "Speccing html-render")
      (spec-test/instrument)

      (spec-test/instrument 'day-hour-min))

(deftest unit-day-hour-min
    (console-test  "unit-day-hour-min"  "html-render")
    (let [expected-day-hour-min "05-06:07"
          actual-day-hour-min (day-hour-min "2019-04-05-06-07-46.173Z")]
      (is (= expected-day-hour-min actual-day-hour-min))))



(deftest unit-get-index
    (console-test  "unit-get-index"  "html-render")
    (let [[my-db-obj _ _ _] (build-db T-TEST-COLLECTION
                                      []
                                      USE_MONGER_DB
                                      TEST-CONFIG-FILE
                                      IGNORE-ENV-VARS)
          purge-table (:purge-table my-db-obj)
          put-items (:put-items my-db-obj)
          NOV-2000-DATE "2000-11-11-11:11:11.011Z"
          DEC-2000-DATE "2000-12-12-12:12:12.012Z"
          NOV-2000-MONTH "2000-11"    ; 0 and 1 record
          DEC-2000-MONTH "2000-12"    ; 1 and 2 records
          test-many [{:the-url "november-1" :the-date NOV-2000-DATE :the-html "blah 1" :the-accurate true :the-time 1111}
                     {:the-url "december-2" :the-date DEC-2000-DATE :the-html "blah 22" :the-accurate true :the-time 2222}
                     {:the-url "december-2" :the-date DEC-2000-DATE :the-html "blah 333" :the-accurate true :the-time 3333}]
          read-from-web false
          expected-get-index (conform-whitespace (read-html "tests_html_render_1.html" read-from-web))]
      (purge-table)
      (put-items test-many)
      (let [actual-get-index (conform-whitespace (get-index my-db-obj "2000-12"))]
      (let [ [text-diff-1 text-diff-2] (is-html-eq expected-get-index actual-get-index)]
          (is (= text-diff-1 text-diff-2)))
)))


(defn do-tests []
  (reset! *run-all-tests* true)
  (reset! *testing-namespace* "fast-all-tests-running")
 (html-render-specs)
  (run-tests 'tests-html-render)
  (reset! *testing-namespace* "no-tests-running"))



(defn fast-tests []
  (reset! *run-all-tests* false)
  (reset! *testing-namespace* "fast-all-tests-running")
 (html-render-specs)
  (run-tests 'tests-html-render)
  (reset! *testing-namespace* "no-tests-running"))