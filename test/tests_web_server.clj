
(ns tests-web-server

  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as spec-alpha]
            [clojure.spec.gen.alpha :as spec-gen]
            [clojure.spec.test.alpha :as spec-test])

  (:require [global-consts  :refer :all])
  (:require [global-vars  :refer :all])

  (:require [crash-screech.html-render :refer :all])

  (:require [java-time :refer [local-date?]])

  (:require [prepare-tests :refer :all]))

(defn web-server-specs []
  (if RUN-SPEC-TESTS
    (do

      (spec-test/instrument)

  ; (spec-test/instrument 'day-hour-min)
      )))

;; (deftest uni-day-hour-min
;;   (testing "test-day-hour-min : cccccccccccccccccccccc "
;;     (console-test  "uni-day-hour-min"  "html-render")
;;     (let [expected-day-hour-min "05-06:07"
;;           actual-day-hour-min (day-hour-min "2019-04-05-06-07-46.173Z")]
;;       (is (= expected-day-hour-min actual-day-hour-min)))))


;make-request-fn
;web-reload
;web-init
