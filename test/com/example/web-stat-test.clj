(ns sff-audio.web-stat-test
  (:require [clojure.test :refer :all]
            [sff-audio.web-stat :refer :all]))

 (deftest a-test
   (testing "FIXME, I fail.asdasd"
     (is (= 1 1))))

(def ^:const TEST-DB-TYPE "monger-db")                          ;;;; what about dynamo-db
(def ^:const TEST-CONFIG-FILE "./local-config.edn")

(deftest test-1000 
  (testing "test-1000 should send an sms message in sms-send-fn" 
    (let [ sms-err-mess "TEST 1000"
           db-test-name DB-TEST-NAME
           fail-to-large 12345678
           pages-to-check [ {:check-page "www.sffaudio.com" :enlive-keys[:article :div.blog-item-wrap]  :at-least fail-to-large} ]
           db-type TEST-DB-TYPE           
           config-file TEST-CONFIG-FILE   
           [my-db-obj _ _ sms-data] (build-db db-test-name pages-to-check db-type config-file IGNORE-ENV-VARS) 
           sms-send-fn (build-sms-send sms-data sms-err-mess)
           expected-data (make-api-call sms-data sms-err-mess) ]
    (swap! *test-results*  assoc :sms-send-fn {})
    ((:purge-table my-db-obj)) 
    (scrape-pages-fn my-db-obj pages-to-check instant-time-fn sms-send-fn)
    (let [actual-data (:sms-send-fn @*test-results*)]
      (is (= expected-data actual-data))))))



(deftest test-1001 
  (testing "test-1001 should not send an sms message in sms-send-fn" 
    (let [ sms-err-mess "TEST 1001"
           db-test-name DB-TEST-NAME
           pass-small 1
           pages-to-check [ {:check-page "www.sffaudio.com" :enlive-keys[:article :div.blog-item-wrap]  :at-least pass-small} ]
           db-type TEST-DB-TYPE          
           config-file TEST-CONFIG-FILE
           [my-db-obj _ _ sms-data] (build-db db-test-name pages-to-check db-type config-file IGNORE-ENV-VARS) 
           sms-send-fn (build-sms-send sms-data sms-err-mess)
           expected-data {} ]
    (swap! *test-results*  assoc :sms-send-fn {})
    ((:purge-table my-db-obj)) 
    (scrape-pages-fn my-db-obj pages-to-check instant-time-fn sms-send-fn)
    (let [actual-data (:sms-send-fn @*test-results*)]
      (is (=  expected-data actual-data))))))

(run-tests)
