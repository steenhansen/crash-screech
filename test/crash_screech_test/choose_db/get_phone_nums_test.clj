
(ns crash-screech-test.choose-db.get-phone-nums-test
   (:require [crash-screech.choose-db :refer :all])
   
    (:require [clojure.test :refer :all])
   ; (:require [clojure.spec.alpha :as spec-alpha]
   ;          [clojure.spec.gen.alpha :as spec-gen]
   ;          [clojure.spec.test.alpha :as spec-test])
   
  )

 
 
(deftest test-get-phone-nums
  (testing "test-get-phone-nums : cccccccccccccccccccccc "
  

;    (try
     ; (println "dddddddddddddddddddddddddkdkdkdkdk")
      (let [expected-phone-nums ["12345678901" "01234567890"]
            actual-phone-nums (get-phone-nums "12345678901,01234567890")]
        (is (= expected-phone-nums actual-phone-nums)))
      
      
  ;    (catch Exception e (str "asdf " (.getMessage e)))
 ;     )
    
    
    ))

