


(ns crash-screech.years-months

  (:require [clojure.string :as clj-str])
  (:require [java-time.local :as j-time])
  (:require [java-time.core :as jt-core])
  (:require [java-time.amount :as jt-amount])
  (:require [java-time.temporal :as jt-temporal]))

(defn trunc-yyyy-mm
  "spec"
  [ym-extra]
  (let [date-vector (clj-str/split ym-extra #"-")
        ym-vector (take 2 date-vector)
        ym-str (clj-str/join "-" ym-vector)]
   ; (if-not (= 2 (count date-vector))
    ;  (throw (Exception. "trunc-yyyy-mm didn't have 2 items")))
    ym-str))

(defn trunc-yyyy-mm-dd
  "spec"
  [ymd-extra]
  (let [date-vector (clj-str/split ymd-extra #"-")
        ymd-vector (take 3 date-vector)
        ymd-str (clj-str/join "-" ymd-vector)]
  ;  (if-not (= 3 (count date-vector))
      ;(throw (Exception. "trunc-yyyy-mm-dd didn't have 3 items")))
    ymd-str))

(defn date-to-yyyy-mm
  "spec"
  [ymd-date]
  (let [ymd-str (str ymd-date)
        ym-str (trunc-yyyy-mm ymd-str)]
    ym-str))

(defn date-to-yyyy-mm-dd
  "spec"
  [ymd-date]
  (let [ymd-str (str ymd-date)
        ym-str (trunc-yyyy-mm-dd ymd-str)]
    ym-str))

(defn month-name
  "spec"
  ([month-offset]
   (let [now-yyyy-mm (date-to-yyyy-mm (j-time/local-date))]
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

(defn prev-month
  "spec"
  ([] (month-name -1))
  ([yyyy-mm] (month-name -1 yyyy-mm)))

(defn current-month
  "spec"
  ([] (month-name 0))
  ([yyyy-mm] (month-name 0 yyyy-mm)))

(defn yyyy-mm-to-ints
  "spec"
  [yyyy-mm]
  (let [date-vector (clj-str/split yyyy-mm #"-")
        yyyy-int (Integer/parseInt (first date-vector))
        mm-int (Integer/parseInt (second date-vector))]
    [yyyy-int mm-int]))

(defn current-yyyy-mm
  "spec"
  ([] (current-yyyy-mm (date-to-yyyy-mm (j-time/local-date))))
  ([yyyy-mm] (trunc-yyyy-mm yyyy-mm)))

(defn current-yyyy-mm-dd
  "spec"
  ([] (current-yyyy-mm-dd (date-to-yyyy-mm-dd (j-time/local-date))))
  ([yyyy-mm-dd] (trunc-yyyy-mm-dd yyyy-mm-dd)))

(defn prev-yyyy-mm
  "spec"
  ([] (prev-yyyy-mm (date-to-yyyy-mm (j-time/local-date))))
  ([yyyy-mm]
   (let [[yyyy-int mm-int] (yyyy-mm-to-ints yyyy-mm)
         local-date (j-time/local-date yyyy-int mm-int)
         last-month (jt-core/minus
                     local-date
                     (jt-amount/months 1))
         ym-str (date-to-yyyy-mm last-month)]
     ym-str)))

(defn instant-time-fn []
  "spec"
  (str (jt-temporal/instant)))

(defn adjusted-date [date-str]
  "spec"
  (clj-str/replace date-str #"T|:" "-"))





