
(ns tests-fake-db
  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as spec-alpha]
            [clojure.spec.gen.alpha :as spec-gen]
            [clojure.spec.test.alpha :as spec-test])

  (:require [global-consts-vars  :refer :all])

  (:require [crash-sms.config-args :refer [make-config compact-hash]])
  (:require [crash-sms.fake-db :refer :all])

  (:require [java-time :refer [local-date?]])

  (:require [prepare-tests :refer :all]))

(alias 's 'clojure.spec.alpha)
(alias 'f-d 'crash-sms.fake-db)




(s/fdef f-d/next-date-time
  :args (s/cat :yyyy-mm-d   :yyyy?-mm?-dd?-hh?-mm?-ss/test-specs))

(s/fdef f-d/fake-build
  :args (s/cat ))



(defn fake-db-specs []
(println "Speccing fake-db")
      (spec-test/instrument)
      (spec-test/instrument 'next-date-time)
      (spec-test/instrument 'fake-build))



;   (clojure.test/test-vars [#'tests-fake-db/t-fake-build])
(deftest t-fake-build
    (let [; db-type  USE_FAKE_DB
         ;  the-config (make-config db-type TEST-CONFIG-FILE IGNORE-ENV-VARS)
       ;   pages-to-check [{:check-page "www.sffaudio.com",
        ;                 :enlive-keys [:article :div.blog-item-wrap],
         ;                :at-least 1}]
                   a-fake-db       (fake-build)  ; the-config  T-TEST-COLLECTION pages-to-check)
]
      (console-test "t-fake-build" "fake-db")

     (is (function?(:my-delete-table a-fake-db)))
     (is (function?(:my-db-alive? a-fake-db)))
     (is (function?(:my-purge-table a-fake-db)))
     (is (function?(:my-get-all a-fake-db)))
     (is (function?(:my-get-url a-fake-db)))              ;;; a-fake-db
     (is (function?(:my-put-item a-fake-db)))
     (is (function?(:my-put-items a-fake-db)))
(is (= (count a-fake-db) 7))
))

(deftest t-fake-put
    (let [a-fake-db (fake-build)
          put-item (:my-put-item a-fake-db)
          put-items (:my-put-items a-fake-db)
          get-all (:my-get-all a-fake-db)
          get-url (:my-get-url a-fake-db)
  test-rec  {:the-url "www.sffaudio.com",
                    :the-date "2011-06-19-01:54:03.800Z",
                    :the-html "blah 1111",
                    :the-accurate true,
                    :the-time 1234}
  test-rec2  {:the-url "aww.sffaudio.com",
                    :the-date "2011-06-18-01:54:03.802Z",
                    :the-html "blah 1111",
                    :the-accurate true,
                    :the-time 1234}
  test-many [{:the-url "www.sffaudio.com",
                     :the-date "2019-06-19-01:54:03.800Z",
                     :the-html "blah 1111",
                     :the-accurate true,
                     :the-time 1234}
                    {:the-url "sffaudio.herokuapp.com_rsd_rss",
                     :the-date "2019-06-19-01:54:03.800Z",
                     :the-html "bluh 2222",
                     :the-accurate true,
                     :the-time 12346}
                    {:the-url "www.sffaudio.com",
                     :the-date "2019-05-19-01:54:03.891Z",
                     :the-html "blah 3333",
                     :the-accurate true,
                     :the-time 1234}
                    {:the-url "sffaudio.herokuapp.com_rsd_rss",
                     :the-date "2019-05-19-01:54:03.892Z",
                     :the-html "bluhss 4444",
                     :the-accurate false,
                     :the-time 12346}]

]
     (console-test "t-fake-put" "fake-db" a-fake-db)
    ; (put-item test-rec)
    ; (put-item test-rec2)

;;  all mongo ({:_id 2019-06-19-01-54-03.800Z+0, :check-url www.sffaudio.com, :check-
  ;  (println "all fake 2011" (get-all "2011-06-19" ))

 (put-items test-many )
;    (println "all fake 2019" (get-all "2019-05-19" ))
    (println "all fake 2019" (get-url "2019-05-19" "www.sffaudio.com" ))          ;;; this seems to work, need a test

))









;; (deftest unit-next-date-time-a
;;     (let [future-date (next-date-time "2000-01")]
;;       (console-test "unit-next-date-time" "mongo-db")
;;       (is (= future-date "2000-02"))))

;; (deftest unit-next-date-time-b
;;     (let [future-date (next-date-time "2001-02-03")]
;;       (console-test "unit-next-date-time" "mongo-db")
;;       (is (= future-date "2001-02-04"))))

;; (deftest unit-next-date-time-c
;;     (let [future-date (next-date-time "2002-12")]
;;       (console-test "unit-next-date-time" "mongo-db")
;;       (is (= future-date "2002-13"))))

;; (deftest unit-next-date-time-d
;;     (let [future-date (next-date-time "2003-12-01")]
;;       (console-test "unit-next-date-time" "mongo-db")
;;       (is (= future-date "2003-12-02"))))

;; (deftest unit-next-date-time-e
;;     (let [future-date (next-date-time "2000")]
;;       (console-test "unit-next-date-time" "mongo-db")
;;       (is (= future-date "2001"))))

;; (deftest unit-next-date-time-f
;;     (let [future-date (next-date-time "2004-04-04-04")]
;;       (console-test "unit-next-date-time" "mongo-db")
;;       (is (= future-date "2004-04-04-05"))))



(defn do-tests []
 (fake-db-specs)
  (run-tests 'tests-fake-db))
