

; how can we manage the date?????? 

(def ^:const MAKE-REQUEST-FN-FILE "./test/sff_audio_test/html-render/make-request-fn-test.html")       

; (defn make-request-fn-test1 [db-type]
;   (let [[my-db-obj web-port cron-url _] (build-db DB-TEST-NAME
;                                            {}
;                                            db-type
;                                            TEST-CONFIG-FILE
;                                            IGNORE-ENV-VARS)
;         purge-table (:purge-table my-db-obj)
;            int-port (Integer/parseInt web-port)
;            temporize-func #("placeholder-func")
;           request-handler (make-request-fn temporize-func my-db-obj cron-url)
;           local-url (str "http://localhost:" int-port)
;           kill-web  (web-init int-port request-handler)
;            ]
;     (purge-table)
;     (let [expected-html  (slurp MAKE-REQUEST-FN-FILE)
;           expected-compressed (strip-white-space expected-html )
;           actual-html (slurp local-url)
;           actual-compressed (strip-white-space actual-html ) ]
;        (is (= expected-compressed actual-compressed))
;        (kill-web))))

;  (deftest test-make-request-fn
;    (testing "test-make-request-fn :"
;            (make-request-fn-test1 :monger-db)))

 ;;;;;;;;;;;;;;
 (def ^:const MAKE-REQUEST-FN-FILE-22 "./test/sff_audio_test/html-render/make-request-fn-test.html")
 
 (defn make-request-fn-test2 [db-type]
  (let [[my-db-obj web-port cron-url sms-data] (build-db DB-TEST-NAME
                                           THE-CHECK-PAGES
                                           db-type
                                           TEST-CONFIG-FILE
                                           IGNORE-ENV-VARS)
        purge-table (:purge-table my-db-obj)
           int-port (Integer/parseInt web-port)
  temporize-func (single-cron-fn scrape-pages-fn my-db-obj THE-CHECK-PAGES sms-data)
          request-handler (make-request-fn temporize-func my-db-obj cron-url)
          local-url (str "http://localhost:" int-port cron-url)
          kill-web  (web-init int-port request-handler)
           ]
    (purge-table)
    (let [expected-html  (slurp MAKE-REQUEST-FN-FILE)
          expected-compressed (strip-white-space expected-html )
          actual-html (slurp local-url)
          actual-compressed (strip-white-space actual-html ) ]
       (is (= expected-compressed actual-compressed))
       (kill-web))))

 (deftest test-make-request-fn
   (testing "test-make-request-fn :"
           (make-request-fn-test2 :monger-db)))