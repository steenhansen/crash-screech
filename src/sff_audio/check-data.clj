
(def ^:const ERROR-LENGTH 55)


;(defn year-this-month [])   ; "2019-01"
;(defn year-last-month [])   ; "2018 12"  
; home-page/last-2-month

(defn instant-time-fn []
  (str (java-time/instant)))

(defn adjusted-date [the-date]
  (clj-str/replace the-date #"T|:" "-"))

(defn ensure-date [check-record]
	 (if (check-record :the-date)
    check-record
    (let [now-date (instant-time-fn)
          dated-check (assoc-in check-record [:the-date] now-date)]
      dated-check)))

(defn trunc-string [the-string num-chars]
  (subs the-string 0 (min (count the-string) num-chars)))

(defn derive-data [check-record]                                   
  (let [{the-url :the-url       
         the-date :the-date
         the-html :the-html
         the-status :the-status               
							  the-time :the-time} check-record    
         total-bytes (count the-html)
								 trunc-html (trunc-string the-html ERROR-LENGTH)          
							  dashed-date (adjusted-date the-date)
					    new-record {:check-url the-url        
					             	  :check-date dashed-date   
                     :check-bytes total-bytes     
                     :check-html  trunc-html     
                     :check-status the-status        
																	 	  :check-time the-time}]		
    new-record))			

(defn uniquely-id [many-index many-item]
  (let [the-date (get many-item :check-date)
  					 extended-date (str the-date "+" many-index)
  					 unique-item (assoc-in many-item [:_id] extended-date)]
    unique-item))

(defn prepare-data [check-records]
  (let [records-dated (for [check-record check-records] 
										      						  (ensure-date check-record))
        derived-data (for [dated-record records-dated] 
															      		(derive-data dated-record))
    				unique-data (map-indexed uniquely-id derived-data)]    
    unique-data)) 


