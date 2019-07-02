

(defn year-month-str
  [ymd-date]
  (let [ymd-str (str ymd-date)
        date-vector (clj-str/split ymd-str #"-")
        ym-vector (pop date-vector)
        ym-str (clj-str/join "-" ym-vector)]
    ym-str))

(defn fig-month
  ([month-offset] (fig-month month-offset (java-time/local-date)))
  ([month-offset this-month]
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
         ymd-str (str this-month)
         date-vector (clj-str/split ymd-str #"-")
         month-str (nth date-vector 1)
         month-int (Integer/parseInt month-str)
         adjusted-month (+ month-int month-offset)
         adjsted-str (str adjusted-month)
         int-key (keyword adjsted-str)
         month-name (int-key month-names)]
     month-name)))

(defn yyyy-mm-date [yyyy-mm]
  (let [ date-vector (clj-str/split yyyy-mm #"-")
         yyyy-int (Integer/parseInt (first date-vector))
         mm-int (Integer/parseInt (second date-vector))
         
         ]
   ; (println "ddddddddddddddddddddd" yyyy-int mm-int)
    [yyyy-int mm-int])
  
  )


(defn last-y-m
  ([] (last-y-m (java-time/local-date)))
  ([yyyy-mm]
   (let [ ; my-var yyyy-mm-date(yyyy-mm) 
         [yyyy-int mm-int] (yyyy-mm-date yyyy-mm) 
         local-date (java-time/local-date yyyy-int mm-int)
         
         last-month (java-time/minus local-date (java-time/months 1))        ; local date was string
         ym-str (year-month-str last-month)
         ]
     
   ;   (println "ddddddddddddddddddddd" yyyy-int mm-int)
     
     ym-str)))

(defn this-y-m
  ([] (this-y-m (java-time/local-date)))
  ([yyyy-mm]
   yyyy-mm))
  
  
  
  
  
  
 


(defn prev-month [] (fig-month -1))

(defn current-month [] (fig-month 0))



(defn instant-time-fn [] (str (java-time/instant)))

(defn adjusted-date [the-date] (clj-str/replace the-date #"T|:" "-"))

(defn ensure-date
  [check-record]
  (if (check-record :the-date)
    check-record
    (let [now-date (instant-time-fn)
          dated-check (assoc-in check-record [:the-date] now-date)]
      dated-check)))

; (defn trunc-string
;   [the-string num-chars]
;   (subs the-string 0 (min (count the-string) num-chars)))

(defn trunc-string
  [the-string num-chars]
  (if the-string 
      (subs the-string 0 (min (count the-string) num-chars))
      EMPTY-HTML
      ))


(defn sub-string
  [the-string start-pos num-chars]
  (subs the-string start-pos (min (count the-string) num-chars)))




(defn derive-data
  [check-record]
  (let [{:keys [the-url the-date the-html the-accurate the-time]} check-record
        check-url the-url
        check-accurate the-accurate
        check-bytes (count the-html)
        check-html (trunc-string the-html ERROR-KEEP-LENGTH)
        check-date (adjusted-date the-date)
        check-time the-time
        new-record (compact-hash check-url
                                 check-date
                                 check-bytes
                                 check-html
                                 check-accurate
                                 check-time)]
    new-record))

(defn uniquely-id
  [many-index many-item]
  (let [the-date (:check-date many-item)
        extended-date (str the-date "+" many-index)
        unique-item (assoc-in many-item [:_id] extended-date)]
    unique-item))

(defn prepare-data
  [check-records]
  (let [records-dated (for [check-record check-records]
                        (ensure-date check-record))
        derived-data (for [dated-record records-dated]
                       (derive-data dated-record))
        unique-data (map-indexed uniquely-id derived-data)]
    unique-data))
