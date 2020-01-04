
(ns crash-sms.check-data
  (:require [clojure.string :as clj-str])
  (:require [crash-sms.years-months  :refer [adjusted-date instant-time-fn]])
  (:require [crash-sms.config-args :refer [compact-hash]])
  (:require [global-consts-vars :refer :all]))

(comment
  (count-string "00x00x00" #"x")
  ; 2
  )
(defn count-string
  "has test"
  [hay-stack needle-regex]
  (let [split-vector (clj-str/split hay-stack needle-regex)
        occurance-count (dec (count split-vector))]
    occurance-count))

(comment
  (trunc-string "1234" 2)
  ; "12"
  )
(defn trunc-string
  "has test"
  [the-string num-chars]
  (if the-string
    (subs the-string 0 (min (count the-string) num-chars))
    EMPTY-HTML))

(comment
  (ms-load-time 2222 PRODUCTION-COLLECTION)
  ; 2222

  (ms-load-time 2222 T-TEST-COLLECTION)
  ; 800007
)
(defn ms-load-time
  [millisecond-load table-name]
  (if (= PRODUCTION-COLLECTION table-name)
    millisecond-load
   FAKE-SCRAPE-SPEED))

(comment
  (derive-data  {:the-url "www.sffaudio.com",
                 :the-date "2019-06-19-01:54:03.800Z",
                 :the-html "123456789",
                 :the-accurate true,
                 :the-time 1234}
                T-TEST-COLLECTION)
  ; {:check-url "www.sffaudio.com",
  ; :check-date "2019-06-19-01-54-03.800Z",
  ; :check-bytes 9,
  ; :check-html "123456789",
  ; :check-accurate true,
  ; :check-time 800007}
  )
(defn derive-data
  "has test"
  [check-record table-name]
  (let [{:keys [the-url the-date the-html the-accurate the-time]} check-record
        check-url the-url
        check-accurate the-accurate
        check-bytes (count the-html)
        check-html (trunc-string the-html ERROR-KEEP-LENGTH)
        check-date (adjusted-date the-date)
        check-time (ms-load-time the-time table-name)
        new-record (compact-hash check-url
                                 check-date
                                 check-bytes
                                 check-html
                                 check-accurate
                                 check-time)]
    new-record))

(comment

 (uniquely-id 8 {:check-url "www.sffaudio.com"
  :check-date "2019-06-19-01-54-03.800Z"
  :check-html "blah 1111"
   :check-accurate true
   :check-time 1234
  :check-bytes 9})
  ; {:check-url "www.sffaudio.com",
  ;  :check-date "2019-06-19-01-54-03.800Z",
  ;  :check-html "blah 1111",
  ;  :check-accurate true,
  ;  :check-time 1234,
  ;  :check-bytes 9,
  ;  :_id "2019-06-19-01-54-03.800Z+8"}
)
(defn uniquely-id
  "has test"
  [many-index many-item]
  (let [the-date (:check-date many-item)
        extended-date (str the-date "+" many-index)
        unique-item (assoc-in many-item [:_id] extended-date)]
    unique-item))

(comment
  (ensure-has-date {})
  ; {:the-date "2019-12-19T03:13:20.749Z"}

  (ensure-has-date {:the-date "abc"})
  ; {:the-date "abc"}
)
(defn ensure-has-date
  [check-record]
  (if (check-record :the-date)
    check-record
    (let [now-date (instant-time-fn)
          dated-check (assoc-in check-record [:the-date] now-date)]
      dated-check)))

(comment
  (prepare-data [{:the-url "www.sffaudio.com/?",
                  :the-date "2019-06-19-01:54:03.800Z",
                  :the-html "blah 1111",
                  :the-accurate true,
                  :the-time 1234}
                 {:the-url "sffaudio.herokuapp.com/rsd/rss/?",
                  :the-date "2019-06-19-01:54:03.800Z",
                  :the-html "bluh 2222",
                  :the-accurate true,
                  :the-time 12346}]
                T-TEST-COLLECTION)
 ; ({:check-url "www.sffaudio.com",
 ;  :check-date "2019-06-19-01-54-03.800Z",
 ;  :check-bytes 9,
 ;  :check-html "blah 1111",
 ;  :check-accurate true,
 ;  :check-time 1234,
 ;  :_id "2019-06-19-01-54-03.800Z+0"}
 ; {:check-url "sffaudio.herokuapp.com_rsd_rss",
 ;  :check-date "2019-06-19-01-54-03.800Z",
 ;  :check-bytes 9,
 ;  :check-html "bluh 2222",
 ;  :check-accurate true,
 ;  :check-time 12346,
 ;  :_id "2019-06-19-01-54-03.800Z+1"})
  )
(defn prepare-data
  [check-records table-name]
  (let [records-dated (for [check-record check-records]
                        (ensure-has-date check-record))
        derived-data (for [dated-record records-dated]
                       (derive-data dated-record table-name))
        unique-data (vec (map-indexed uniquely-id derived-data))]
    unique-data))
