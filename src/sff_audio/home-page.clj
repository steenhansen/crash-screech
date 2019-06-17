



;;; this stuff should be in check-sites.clj

(defn left-pad
  "Left pad string with spaces, making it at least len long."
  [mystr len]
  (format "%02d" mystr))





(comment  "to check" (last-2-months 2000 01)         )
(defn last-2-months [the-year the-month ]
	(let [last-year (- the-year 1)
						 last-month (- the-month 1) 

       the-month-pad (left-pad the-month 2)
       last-month-pad (left-pad last-month 2)
						 ] 
     ( if (= 0 last-month)
          [(str last-year "-12") (str the-year "-01")] 
          [(str the-year "-" last-month-pad) ( str the-year "-" the-month-pad)] )))




(comment  "all pages 2019-04 and 2019-05" (data-2-months my-db-obj 2019 5)         )
(defn data-2-months [my-db-obj the-year the-month ]
 ( let [ [start-ym end-ym] (last-2-months the-year the-month)


          start-data ((:begins-all my-db-obj)  start-ym)                   ;;;   ({map})
          end-data ((:begins-all my-db-obj)  end-ym) 
          months-2-data (merge start-data end-data)
              
;              sorted-matches (sort-by :_id    months-2-data)              ;;;; 	qbert amazonica does not use :_id
              sorted-matches (sort-by :check-date    months-2-data)              ;;;;  both have check-date



;;;;; WE SHOULD NEVER HAVE TO SORT BY :check-date as we will show 2 different tables, this-month & last-month
;;;;; this function should return [this-month last-month]



          ]
       
      (println "asdfasdf" start-ym "xxx" end-ym "iii")
          sorted-matches
    )



)

 
