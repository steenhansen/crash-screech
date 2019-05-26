


(ns sffaudio.web-stat
	  (:require [java-time :as jav-time])             
)


(defn leftpad
  "Left pad string with spaces, making it at least len long."
  [mystr len]
  (format (str "%" len "s") mystr))



(comment  "to check" (last-2-months 2000 01)         )
(defn last-2-months [the-year the-month ]
	(let [last-year (- the-year 1)
						 last-month (- the-month 1) 

       the-month-pad (leftpad 2 the-month)
       last-month-pad (leftpad 2 last-month)
						 ] 
     ( if (= 0 last-month)
          [(str last-year "-12") (str the-year "-01")] 
          [(str the-year "-" last-month-pad) ( str the-year "-" the-month-pad)] )))




(comment  "to check" (data-2-months my-db-obj 2019 5)         )
(defn data-2-months [my-db-obj the-year the-month ]
 ( let [ [start-ym end-ym] (last-2-months the-year the-month)


          start-data ((:begins-all my-db-obj)  start-ym) 
          end-data ((:begins-all my-db-obj)  end-ym) 
       ]   
       (println "asdfasdf" end-ym)
          end-data
    )



)



