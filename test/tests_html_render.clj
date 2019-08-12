

(ns tests-html-render


  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as spec-alpha]
            [clojure.spec.gen.alpha :as spec-gen]
            [clojure.spec.test.alpha :as spec-test])

  (:require [global-consts  :refer :all])
  (:require [global-vars  :refer :all])

  (:require [crash-screech.html-render :refer :all])


(:require [java-time :refer [local-date?]])





  (:require [prepare-tests :refer :all])
  (:require [spec-calls :refer :all]))



(defn html-render-units []
   (spec-test/instrument 'day-hour-min)
 ; (day-hour-min-test/test-day-hour-min)
)

 (deftest uni-day-hour-min
   (testing "test-day-hour-min : cccccccccccccccccccccc "
     (let [expected-day-hour-min "05-06:07"
           actual-day-hour-min (day-hour-min "2019-04-05-06-07-46.173Z")]
       (is (= expected-day-hour-min actual-day-hour-min)))))



