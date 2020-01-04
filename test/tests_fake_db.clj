
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




; bring back old
(s/fdef f-d/fake-build
  :args (s/alt :nullary (s/cat)
               :ternary (s/cat ::mongolabs-config :mongo-config?/test-specs
               ::table-name string?
               ::pages-to-check vector?)  ;; for tests to match mongo's signature

 ))



(defn fake-db-specs []
  (print-line "Speccing fake-db")
  (spec-test/instrument)
  (spec-test/instrument 'fake-build))

;     (clojure.test/test-vars [#'tests-fake-db/t-fake-get-url])
(deftest t-fake-get-url
  (console-test "t-fake-put-items")
  (let [a-fake-db (fake-build)
        purge-table (:my-purge-table a-fake-db)
        put-items (:my-put-items a-fake-db)
        get-url (:my-get-url a-fake-db)
        test-many [{:the-url "www.sffaudio.com",
                    :the-date "2016-07-08-01:54:03.001Z",
                    :the-html "blah 1111",
                    :the-accurate true,
                    :the-time 1234}
                   {:the-url "sffaudio.herokuapp.com_rsd_rss",
                    :the-date "2016-07-08-01:54:03.002Z",
                    :the-html "bluh 2222",
                    :the-accurate true,
                    :the-time 12346}
                   {:the-url "www.sffaudio.com",
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
        sff (get-url "" "www.sffaudio.com")
        sff-2016 (get-url "2016" "www.sffaudio.com")
        sff-2016-07 (get-url "2016-07" "www.sffaudio.com")
        sff-2016-07-08 (get-url "2016-07-08" "www.sffaudio.com")

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

(defn do-tests []
  (reset! *run-all-tests* true)
  (reset! *testing-namespace* "fast-all-tests-running")
  (fake-db-specs)
  (run-tests 'tests-fake-db)
  (reset! *testing-namespace* "no-tests-running"))

(defn fast-tests []
  (reset! *run-all-tests* false)
  (reset! *testing-namespace* "fast-all-tests-running")
  (fake-db-specs)
  (run-tests 'tests-fake-db)
  (reset! *testing-namespace* "no-tests-running"))
