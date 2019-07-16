(defn date-to-yyyy-mm
  [ymd-date]
  (let [ymd-str (str ymd-date)
        date-vector (clj-str/split ymd-str #"-")
        ym-vector (pop date-vector)
        ym-str (clj-str/join "-" ym-vector)]
    ym-str))

(defn month-name
  ([month-offset]
   (let [now-yyyy-mm (date-to-yyyy-mm (java-time/local-date))]
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

(defn prev-month ([] (month-name -1)) ([yyyy-mm] (month-name -1 yyyy-mm)))

(defn current-month ([] (month-name 0)) ([yyyy-mm] (month-name 0 yyyy-mm)))

(defn yyyy-mm-to-ints
  [yyyy-mm]
  (let [date-vector (clj-str/split yyyy-mm #"-")
        yyyy-int (Integer/parseInt (first date-vector))
        mm-int (Integer/parseInt (second date-vector))]
    [yyyy-int mm-int]))

(defn current-yyyy-mm
  ([] (current-yyyy-mm (date-to-yyyy-mm (java-time/local-date))))
  ([yyyy-mm] yyyy-mm))

(defn prev-yyyy-mm
  ([] (prev-yyyy-mm (date-to-yyyy-mm (java-time/local-date))))
  ([yyyy-mm]
   (let [[yyyy-int mm-int] (yyyy-mm-to-ints yyyy-mm)
         local-date (java-time/local-date yyyy-int mm-int)
         last-month (java-time/minus local-date (java-time/months 1))
         ym-str (date-to-yyyy-mm last-month)]
     ym-str)))

(defn instant-time-fn [] (str (java-time/instant)))

(defn adjusted-date [date-str] (clj-str/replace date-str #"T|:" "-"))





