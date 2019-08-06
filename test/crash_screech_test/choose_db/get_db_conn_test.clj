
(ns crash-screech-test.choose-db.get-db-conn-test
   (:require [crash-screech.choose-db :refer :all])
       (:require [clojure.test :refer :all])
      (:require [sff-global-consts  :refer :all])
         (:require [test-prepare  :refer :all])
  (:require [crash-screech.config-args :refer [make-config]])
  )

(defn test-get-db-conn [db-type]
  (let [the-config (make-config db-type TEST-CONFIG-FILE IGNORE-ENV-VARS)
        my-db-funcs (get-db-conn DB-TEST-NAME [] db-type the-config)]


    (is (function? (:my-delete-table my-db-funcs)))
    (is (function? (:my-purge-table my-db-funcs)))
    (is (function? (:my-get-all my-db-funcs)))
    (is (function? (:my-get-url my-db-funcs)))
    (is (function? (:my-put-item my-db-funcs)))
    (is (function? (:my-put-items my-db-funcs)))
           ))

(deftest test-get-db-conn-dynoDb
  (testing "test-build-db :"
    (test-get-db-conn :monger-db)))

(deftest test-get-db-conn-mongoDb
  (testing "test-build-db :"
    (test-get-db-conn     :amazonica-db)))



