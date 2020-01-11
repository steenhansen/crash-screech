
(ns tests-check-data
  (:require [clojure.test :refer :all])
  (:require [clojure.spec.alpha :as s]

            [clojure.spec.test.alpha :as t])
  (:require [global-consts-vars  :refer :all])
  (:require [crash-sms.check-data :refer :all])
  (:require [java-time :refer [local-date?]])
  (:require [text-diff :refer [is-html-eq]])
  (:require [prepare-tests :refer :all]))

(s/fdef prepare-data
  :args (s/cat :check-records (s/coll-of :url-date-tuple?/test-specs)
               :table-name string?))

(s/fdef count-string
  :args (s/cat :hay-stack string?
               :needle-regex #(instance? java.util.regex.Pattern %)))

(s/fdef trunc-string
  :args (s/cat :the-string (s/nilable string?) :num-chars integer?))

(s/fdef ms-load-time
  :args (s/cat :the-time (s/nilable integer?)
               :table-name string?))

(s/fdef derive-data
  :args (s/cat :check-record :data-map?/test-specs
               ::table-name string?))

(s/fdef uniquely-id
  :args (s/cat :many-index integer? :many-item :check-map?/test-specs))

(s/fdef ensure-has-date
  :args (s/cat :check-record :url-date-tuple?/test-specs))

(defn check-data-specs []
  (print-line "Speccing check-data")
  (t/instrument)
  (t/instrument 'count-string)
  (t/instrument 'trunc-string)
  (t/instrument 'derive-data)
  (t/instrument 'uniquely-id)
  (t/instrument 'ensure-has-date)
  (t/instrument 'prepare-data))

(deftest test-count-string
  (console-test "test-count-string" "check-data")
  (let [occurance-count (count-string "001001001000" #"1")]
    (is (= occurance-count 3))))

(deftest test-trunc-string
  (console-test "test-trunc-string" "check-data")
  (let [trunced-str (trunc-string "123456789" 3)]
    (is (= trunced-str "123"))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def ^:const T-BEFORE-THE-DATA {:the-url WWW-SFFAUDIO-COM
                                :the-date "2019-06-19-01:54:03.800Z",
                                :the-html "123456789",
                                :the-accurate true,
                                :the-time FAKE-SCRAPE-SPEED})
;                                :the-time 9876543})

(def ^:const T-AFTER-CHECK-DATA {:check-url WWW-SFFAUDIO-COM
                                 :check-date "2019-06-19-01-54-03.800Z",
                                 :check-html "123456789",
                                 :check-bytes 9
                                 :check-accurate true,
                                 :check-time FAKE-SCRAPE-SPEED})

(deftest test-derive-data
  (console-test "test-derive-data" "check-data")
  (let [derived-data (derive-data T-BEFORE-THE-DATA T-TEST-COLLECTION)
        [diff-1 diff-2] (is-html-eq derived-data T-AFTER-CHECK-DATA)]
    (is (= diff-1 diff-2))))

(def ^:const T-BEFORE-UNIQUE-ID {:check-url WWW-SFFAUDIO-COM
                                 :check-date "2019-06-19-01:54:03.800Z",
                                 :check-html "123456789",
                                 :check-bytes 9
                                 :check-accurate true,
                                 :check-time 1234})

(def ^:const T-EXPECTED-UNIQUE-ID {:check-url WWW-SFFAUDIO-COM
                                   :check-date "2019-06-19-01:54:03.800Z",
                                   :_id "2019-06-19-01:54:03.800Z+1",
                                   :check-html "123456789",
                                   :check-bytes 9
                                   :check-accurate true,
                                   :check-time 1234})

(deftest test-uniquely-id
  (console-test  "test-uniquely-id"  "check-data")
  (let [unique-data (uniquely-id 1 T-BEFORE-UNIQUE-ID)]
    (is (= unique-data T-EXPECTED-UNIQUE-ID))))


;;;;;;;;;;;;;;


(def ^:const T-ENSURE-DATA {:the-url WWW-SFFAUDIO-COM
                            :the-html "123456789",
                            :the-accurate true,
                            :the-time 1234})

(deftest test-ensure-has-date
  (console-test "test-ensure-has-date" "check-data")
  (let [has-date (ensure-has-date T-ENSURE-DATA)]
    (is (contains? has-date :the-date))))


;;;;;;;;;;;;;;;;;;;;


(def ^:const T-BEFORE-ENSURE-DATA

  [{:the-url WWW-SFFAUDIO-COM
    :the-date "2019-06-19-01:54:03.800Z",
    :the-html "blah 1111",
    :the-accurate true,
    :the-time 1234}
   {:the-url "sffaudio.herokuapp.com_rsd_rss",
    :the-date "2019-06-19-01:54:03.800Z",
    :the-html "bluh 2222",
    :the-accurate true,
    :the-time 12346}])

(def ^:const T-EXPECTED-PREPARE-DATA
  [{:check-url WWW-SFFAUDIO-COM
    :check-date "2019-06-19-01-54-03.800Z",
    :_id "2019-06-19-01-54-03.800Z+0",
    :check-html "blah 1111",
    :check-bytes 9
    :check-accurate true,
    :check-time FAKE-SCRAPE-SPEED}
   {:check-url "sffaudio.herokuapp.com_rsd_rss",
    :_id "2019-06-19-01-54-03.800Z+1",
    :check-date "2019-06-19-01-54-03.800Z",
    :check-html "bluh 2222",
    :check-bytes 9
    :check-accurate true,
    :check-time FAKE-SCRAPE-SPEED}])

(deftest test-prepare-data
  (console-test "test-prepare-data" "check-data")
  (let [prepared-data (prepare-data T-BEFORE-ENSURE-DATA T-TEST-COLLECTION)
        [diff-1 diff-2] (is-html-eq prepared-data T-EXPECTED-PREPARE-DATA)]
    (is (= diff-1 diff-2))   ;; so here we are comparaing strings, so much easier  ;; abbacab
    (is (= prepared-data T-EXPECTED-PREPARE-DATA))))

(defn all-tests []
  (reset! *T-REAL-DB-ASSERTIONS* true)
  (reset! *T-ASSERTIONS-VIA-REPL* false)
  (check-data-specs)
  (run-tests 'tests-check-data)
  (reset! *T-ASSERTIONS-VIA-REPL* true))

(defn fast-tests []
  (reset! *T-REAL-DB-ASSERTIONS* false)
  (reset! *T-ASSERTIONS-VIA-REPL* false)
  (check-data-specs)
  (run-tests 'tests-check-data)
  (reset! *T-ASSERTIONS-VIA-REPL* true))
