





(def ^:const MAKE-REQUEST-FN-FILE
  "./test/sff_audio_test/html-render/make-request-fn-test.html")

(def ^:const NO-RECORDS-0 0)

(defn make-request-fn-test1 [db-type]
  (let [[my-db-obj web-port cron-url _] (build-db DB-TEST-NAME
                                                  {}
                                                  db-type
                                                  TEST-CONFIG-FILE
                                                  IGNORE-ENV-VARS)
        purge-table (:purge-table my-db-obj)
        int-port (Integer/parseInt web-port)
        temporize-func #("placeholder-func")
        request-handler (make-request-fn temporize-func my-db-obj cron-url)
        local-url (str "http://localhost:" int-port)
        kill-web  (web-init int-port request-handler)]
    (purge-table)
    (let [actual-html (slurp local-url)
          occurance-count (count-scrapes actual-html)]
      (is (= occurance-count NO-RECORDS-0))
      (kill-web))))

(deftest test-make-request-fn1
  (testing "test-make-request-fn1 :"
    (make-request-fn-test1 :monger-db)))

 ;;;;;;;;;;;;;;
(def ^:const INSERTED-RECORDS-2 2)

(def ^:const THE-CHECK-PAGES-99
  [{:check-page "www.sffaudio.com",
    :enlive-keys [:article :div.blog-item-wrap]
    :at-least 66} ;6


   {:check-page "sffaudio.herokuapp.com_rsd_rss"     :enlive-keys [:item]
    :at-least 1}])

(defn make-request-fn-test2 [db-type]
  (let [[my-db-obj web-port cron-url sms-data] (build-db DB-TEST-NAME
                                                         THE-CHECK-PAGES-99
                                                         db-type
                                                         TEST-CONFIG-FILE
                                                         IGNORE-ENV-VARS)
        purge-table (:purge-table my-db-obj)
        int-port (Integer/parseInt web-port)
        temporize-func (single-cron-fn scrape-pages-fn my-db-obj THE-CHECK-PAGES-99 sms-data)
        request-handler (make-request-fn temporize-func my-db-obj cron-url)
        local-url (str "http://localhost:" int-port cron-url)
        kill-web  (web-init int-port request-handler)]
    (purge-table)
    (let [actual-html (slurp local-url)
          occurance-count (count-scrapes actual-html)]
      (is (= occurance-count INSERTED-RECORDS-2))
      (kill-web))))

(deftest test-make-request-fn2
  (testing "test-make-request-fn2 :"
    (make-request-fn-test2 :monger-db)))
