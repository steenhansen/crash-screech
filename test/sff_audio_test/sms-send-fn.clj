

(defn sms-send-init [pages-to-check db-type]
  (let [[my-db-obj _ _ sms-data] (build-db DB-TEST-NAME
                                           pages-to-check
                                           db-type
                                           TEST-CONFIG-FILE
                                           IGNORE-ENV-VARS)
         purge-table (:purge-table my-db-obj)
         an-sms-test? true
         sms-send-fn (build-sms-send sms-data an-sms-test?)]
        
   (defn get-actual-sms [read-from-web?]
     (let [actual-sms (scrape-pages-fn my-db-obj
                                       pages-to-check
                                       instant-time-fn
                                       sms-send-fn
                                       read-from-web?)]
       actual-sms))
  
   (purge-table)
   (compact-hash my-db-obj sms-data sms-send-fn)))


(defn sms-send-fn_error [db-type]
  (let [fail-to-large 12345678
        pages-to-check [{:check-page "www.sffaudio.com",
                         :enlive-keys [:article :div.blog-item-wrap],
                         :at-least fail-to-large}]
        {:keys [my-db-obj sms-data sms-send-fn]} (sms-send-init pages-to-check db-type)
        read-from-web? false
        send-hello-sms? true
        expected-sms (make-api-call sms-data SMS-FOUND-ERROR send-hello-sms?)]
    (let [actual-sms (get-actual-sms read-from-web?)]
      (is (= expected-sms actual-sms)))))

(defn sms-send-fn_ok [db-type]
  (let [pass-small 1
        pages-to-check [{:check-page "www.sffaudio.com",
                         :enlive-keys [:article :div.blog-item-wrap],
                         :at-least pass-small}]
        {:keys [my-db-obj sms-data sms-send-fn]}  (sms-send-init pages-to-check db-type)
        read-from-web? false
        expected-sms SMS-NO-ERROR]
    (let [actual-sms (get-actual-sms read-from-web?)]
      (is (= expected-sms actual-sms)))))


; (deftest test-1000
;   (testing "test-1000 :amazonica-db should send an sms message in sms-send-fn"
;            (sms-send-fn_error :amazonica-db)))

;  (deftest test-1001
;    (testing "test-1001 :monger-db should send an sms message in sms-send-fn"
;            (sms-send-fn_error :monger-db)))




; (deftest test-1002
;   (testing "test-1002 :amazonica-db should send an sms message in sms-send-fn"
;            (sms-send-fn_ok :amazonica-db)))

; (deftest test-1003
;   (testing "test-1003 :monger-db should send an sms message in sms-send-fn"
;            (sms-send-fn_ok :monger-db)))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn init_1_2_3_4_months [db-type]
  (let [ [my-db-obj _ _ _] (build-db DB-TEST-NAME
                                                         {}
                                                         db-type
                                                         TEST-CONFIG-FILE
                                                         IGNORE-ENV-VARS)
        purge-table (:purge-table my-db-obj)
        put-items (:put-items my-db-obj)
         get-all (:get-all my-db-obj)
         NOV-2000-DATE "2000-11-11-11:11:11.011Z"
         DEC-2000-DATE "2000-12-12-12:12:12.012Z"
         JAN-2001-DATE "2001-01-01-01:01:01.001Z"
         FEB-2001-DATE "2001-02-02-02:02:02.002Z"

                  NOV-2000-MONTH "2000-11"
                  DEC-2000-MONTH "2000-12"
                  JAN-2000-MONTH "2001-01"
                  FEB-2000-MONTH "2001-02"
        test-many [
                   {:the-url "november-1", :the-date NOV-2000-DATE}

                   {:the-url "december-2", :the-date DEC-2000-DATE}
                   {:the-url "december-2", :the-date DEC-2000-DATE}
                   
                   {:the-url "january-3",  :the-date JAN-2001-DATE}
                   {:the-url "january-3",  :the-date JAN-2001-DATE}
                   {:the-url "january-3",  :the-date JAN-2001-DATE}
                            
                   {:the-url "february-4", :the-date FEB-2001-DATE}
                   {:the-url "february-4", :the-date FEB-2001-DATE}
                   {:the-url "february-4", :the-date FEB-2001-DATE}
                   {:the-url "february-4", :the-date FEB-2001-DATE}
                   
                   
                                      ]] 
     (purge-table)
   (put-items test-many) 
   
    ( println "****" (get-two-months my-db-obj DEC-2000-MONTH)    )
 
   
   
   
   
   ))

(deftest test-2000
  (testing "test-2000 :monger-db should send an sms message in sms-send-fn"
           (init_1_2_3_4_months :monger-db)))
