
(ns tests-fake-db
  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as t])
  (:require [global-consts-vars  :refer :all])
  (:require [crash-sms.config-args :refer [make-config compact-hash]])
  (:require [crash-sms.fake-db :refer :all])
  (:require [java-time :refer [local-date?]])
  (:require [prepare-tests :refer :all]))

(s/check-asserts true)

(s/fdef fake-build
  :args (s/alt :nullary (s/cat)
               :ternary (s/cat ::mongolabs-config :mongo-config?/test-specs
               ::table-name string?
               ::pages-to-check vector?)  ;; for tests to match mongo's signature
 ))

(defn fake-db-specs []
  (print-line "Speccing fake-db")
  (t/instrument)
  (t/instrument 'fake-build))

;     (clojure.test/test-vars [#'tests-fake-db/t-fake-get-url])
(deftest test-fake-get-url
  (console-test "test-fake-put-items" "fake-db")
  (let [a-fake-db (fake-build)
        purge-table (:my-purge-table a-fake-db)
        put-items (:my-put-items a-fake-db)
        get-url (:my-get-url a-fake-db)
        test-many [{:the-url WWW-SFFAUDIO-COM
                    :the-date "2016-07-08-01:54:03.001Z",
                    :the-html "blah 1111",
                    :the-accurate true,
                    :the-time 1234}
                   {:the-url "sffaudio.herokuapp.com_rsd_rss",
                    :the-date "2016-07-08-01:54:03.002Z",
                    :the-html "bluh 2222",
                    :the-accurate true,
                    :the-time 12346}
                   {:the-url WWW-SFFAUDIO-COM
                    :the-date "2016-07-09-01:54:03.003Z",
                    :the-html "blah 3333",
                    :the-accurate true,
                    :the-time 1234}
                   {:the-url "sffaudio.herokuapp.com_rsd_rss",
                    :the-date "2016-07-09-02:54:03.004Z",
                    :the-html "bluhss 4444",
                    :the-accurate false,
                    :the-time 12346}]
        _ (purge-table)
        _ (put-items test-many)
        all-db (get-url "" "")
        all-2016 (get-url "2016" "")
        all-2016-07 (get-url "2016-07" "")
        all-2016-07-08 (get-url "2016-07-08" "")
        all-2016-07-09-02 (get-url "2016-07-09-02" "")
        sff (get-url "" WWW-SFFAUDIO-COM)
        sff-2016 (get-url "2016" WWW-SFFAUDIO-COM)
        sff-2016-07 (get-url "2016-07" WWW-SFFAUDIO-COM)
        sff-2016-07-08 (get-url "2016-07-08" WWW-SFFAUDIO-COM)

]
    (is (= (count all-db) 4))
    (is (= (count all-2016) 4))
    (is (= (count all-2016-07) 4))
    (is (= (count all-2016-07-08) 2))
    (is (= (count all-2016-07-09-02) 1))
    (is (= (count sff) 2))
    (is (= (count sff-2016) 2))
    (is (= (count sff-2016-07) 2))
    (is (= (count sff-2016-07-08) 1))
;
    ))

(defn all-tests []
  (reset! *T-REAL-DB-ASSERTIONS* true)
  (reset! *T-ASSERTIONS-VIA-REPL* false)
  (fake-db-specs)
  (run-tests 'tests-fake-db)
  (reset! *T-ASSERTIONS-VIA-REPL* true))

(defn fast-tests []
  (reset! *T-REAL-DB-ASSERTIONS* false)
  (reset! *T-ASSERTIONS-VIA-REPL* false)
  (fake-db-specs)
  (run-tests 'tests-fake-db)
  (reset! *T-ASSERTIONS-VIA-REPL* true))
