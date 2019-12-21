


(ns crash-screech.years-months

  (:require [clojure.string :as clj-str])
  (:require [java-time.core :as jt-core])
  (:require [java-time.amount :as jt-amount])
  (:require [java-time.temporal :as jt-temporal]))

(comment
  (trunc-yyyy-mm "2001-12-31")
  ;"2001-12"

  (trunc-yyyy-mm "2001-12-31T123")
  ;"2001-12"
  )

(defn trunc-yyyy-mm
  [ym-extra]
  (let [time-vector (clj-str/split ym-extra #"T")
        date-part (first time-vector)
        date-vector (clj-str/split date-part #"-")
        ym-vector (take 2 date-vector)
        ym-str (clj-str/join "-" ym-vector)]
    ym-str))

(comment
  (trunc-yyyy-mm-dd "2001-12-31")
  ;"2001-12-31"
  (trunc-yyyy-mm-dd "2001-12-31T123")
  ;"2001-12-31"
  )

(defn trunc-yyyy-mm-dd
  [ymd-extra]
  (let [time-vector (clj-str/split ymd-extra #"T")
        date-part (first time-vector)
        date-vector (clj-str/split date-part #"-")
        ymd-vector (take 3 date-vector)
        ymd-str (clj-str/join "-" ymd-vector)]
    ymd-str))

(comment
  (date-to-yyyy-mm (java-time.temporal/instant 1999 12))
  ;"1999-12"
  )
(defn date-to-yyyy-mm
  [ymd-date]
  (let [ymd-str (str ymd-date)
        ym-str (trunc-yyyy-mm ymd-str)]
    ym-str))

(comment
  (date-to-yyyy-mm-dd (java-time.temporal/instant))
  ;"2019-12-19"
  )
(defn date-to-yyyy-mm-dd
  "spec"
  [ymd-date]
  (let [ymd-str (str ymd-date)
        ym-str (trunc-yyyy-mm-dd ymd-str)]
    ym-str))

(comment
  (month-name 1)
  ;"January"
  (month-name -1 "2001-01")
; "December"
  )
(defn month-name
  "spec"
  ([month-offset]
   (let [now-yyyy-mm (date-to-yyyy-mm (java-time.temporal/instant))]
     (month-name month-offset now-yyyy-mm)))
  ([month-offset yyyy-mm]
   (let [month-names {:0 "December",
                      :1 "January",
                      :2 "February",
                      :3 "March",
                      :4 "April",
                      :5 "May",
                      :6 "June",
                      :7 "July",
                      :8 "August",
                      :9 "September",
                      :10 "October",
                      :11 "November",
                      :12 "December",
                      :13 "January"}
         date-vector (clj-str/split yyyy-mm #"-")
         month-str (nth date-vector 1)
         month-int (Integer/parseInt month-str)
         adjusted-month (+ month-int month-offset)
         adjsted-str (str adjusted-month)
         int-key (keyword adjsted-str)
         month-name (int-key month-names)]
     month-name)))

(comment
  (prev-month "2001-01")
; "December"
  )

(defn prev-month
  "spec"
  ([] (month-name -1))
  ([yyyy-mm] (month-name -1 yyyy-mm)))

(comment
  (current-month "2001-01")
; "January"
  )
(defn current-month
  "spec"
  ([] (month-name 0))
  ([yyyy-mm] (month-name 0 yyyy-mm)))

(comment
  (yyyy-mm-to-ints "2001-01")
; [2001 1]
  )
(defn yyyy-mm-to-ints
  "spec"
  [yyyy-mm]
  (let [date-vector (clj-str/split yyyy-mm #"-")
        yyyy-int (Integer/parseInt (first date-vector))
        mm-int (Integer/parseInt (second date-vector))]
    [yyyy-int mm-int]))

(comment
  (current-yyyy-mm)
; [2019 19]
  (current-yyyy-mm "2002-02")
; [2002 2]
  )
(defn current-yyyy-mm
  "spec"
  ([] (current-yyyy-mm (date-to-yyyy-mm (java-time.temporal/instant))))
  ([yyyy-mm] (trunc-yyyy-mm yyyy-mm)))

(comment
  (current-yyyy-mm-dd)
; "2019-12-19"
  (current-yyyy-mm-dd "2002-02-03")
; "2002-02-03"
  )
(defn current-yyyy-mm-dd
  "spec"
  ([] (current-yyyy-mm-dd (date-to-yyyy-mm-dd (java-time.temporal/instant))))
  ([yyyy-mm-dd] (trunc-yyyy-mm-dd yyyy-mm-dd)))

(comment
  (prev-yyyy-mm "2019-12")
  ; "2019-11"
  (prev-yyyy-mm "2019-01")
  ; "2018-12"
  )
(defn prev-yyyy-mm
  ([] (prev-yyyy-mm (date-to-yyyy-mm (java-time.temporal/instant))))
  ([yyyy-mm]
   (let [[yyyy-int mm-int] (yyyy-mm-to-ints yyyy-mm)]
     (if (= 1 mm-int)
       (str (- yyyy-int 1) "-" 12)
       (if (> mm-int 10)
         (str yyyy-int "-" (- mm-int 1))
         (str yyyy-int "-0" (- mm-int 1)))))))

(comment
  (instant-time-fn)
  ; "2019-12-19T07:55:17.829Z"
  )
(defn instant-time-fn []
  "spec"
  (str (java-time.temporal/instant)))

(comment
  (adjusted-date "2019-12-19T07:55:17.829Z")
  ; "2019-12-19-07-55-17.829Z"
  )
(defn adjusted-date [date-str]
  "spec"
  (clj-str/replace date-str #"T|:" "-"))


(comment
  ((date-with-now-time-fn "2019-11-10"))
  ; "2019-11-10-07-55-17.829Z"
  )
(defn date-with-now-time-fn [the-date]
  (fn []
    (let [utc-time (str (jt-temporal/instant))
          utc-list (clj-str/split utc-time #"T")
          utc-time (second utc-list)
          any-time  (str the-date "T" utc-time)]
      any-time)))
