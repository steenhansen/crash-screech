


(ns crash-screech-test.check-data.trunc-string-test
  (:require [crash-screech.check-data :refer :all])
      (:require [clojure.test :refer :all])
)



 (deftest test-trunc-string
   (testing "test-trunc-string : cccccccccccccccccccccc "
     (let [trunced-str (trunc-string "123456789" 3)]
       (is (= trunced-str "123")))))



