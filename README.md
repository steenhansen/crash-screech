

tests-web-server> (do-tests)
   works
      tests_web_server_1.html 
is what we get with
    test-date (date-with-now-time-fn "2019-10-01") 
in tests_web_server.clj

























problem is that the months test is brittle
if we scrape in december it does not match the september name in 

septmeberX octoberY in tests_web_server_1.html

in sms_event.clj     build-web-scrap    where we do (instant-time-fn)   most likely makes the month
look at years_months/ instant-time-fn

           (defn build-web-scrap [scrape-pages-fn my-db-obj pages-to-check sms-data testing-sms? date-time-fn]

in src/start_heroku
 temporize-func (build-web-scrap scrape-pages-fn my-db-obj THE-CHECK-PAGES sms-data testing-sms? instant-time-fn)

in test/tests_web_server
   temporize-func (build-web-scrap scrape-pages-fn my-db-obj THE-CHECK-PAGES sms-data testing-sms?  (adjusted-date "2019-07-04T04:18:46.173Z"))







 (defn vector-to-st
r [a-vector]
; (println "VECTOR" a-vector)
   (let [vector-members (reduce thing-to-str "" a-vector)
          vector-str (str " [" vector-members " ]")]
     vector-str))
  
  (defn map-to-str [a-map]
  ;(println "MAP" a-map)
    (let [map-members (reduce thing-to-str "" a-map)
	      map-str (str " { " map-members "}")]
	  map-str ))
  
 (defn thing-to-str [accum the-thing]
  ; (println "ACCUM" accum )
   ;(println "THING" the-thing)
  
    (if (map-entry? the-thing)
	  (do  
	  ;(println "a entry" the-thing)
             (str accum (key the-thing) " " (val the-thing) " "))
			 
   
     (if (vector? the-thing)
	     (do 
		   ;  (println "a vector" the-thing)
             (str accum (vector-to-str the-thing))
			 )
		(if (map? the-thing)
		  (do 
		      ;(println "a map" the-thing)
		      (str accum (map-to-str the-thing)))
		   (do
		    ; (println "a some" the-thing)
		    (str  accum " " the-thing))
		 
		 
		 ))))
  		
; (let [a-vec [ [1 2] [3 4] ]
; (let [a-vec  {:a 1 :b 2} 
 (let [a-vec [ {:a 1 :b 2} {:c 3 :d 4} ]

       res-str (thing-to-str "" a-vec)]
  (println res-str (type  res-str)))
  










in html_render.fill-date 
   if testing then put 987654321


(defn fill-date
  [check-date]
  (enlive-html/do-> (enlive-html/content "bog's your uncle"))
;;  (enlive-html/do-> (enlive-html/content (day-hour-min check-date)))
)
(def ^:const FAKE-TEST-TIME 98765432)
(defn test-time 
  ""
  [the-time]
  (if (nil? (resolve 'T-DB-TEST-NAME))
     the-time
     (if @*test-use-test-time*
         FAKE-TEST-TIME
         the-time)))








we are having issues with tests-mongo-db

the :yyyy-mm-or-yyyy-mm-dd??/test-specs

is 2000-12-31-23     2004-04-04-04
there is not supposed to be an hour there, but works

so we need 
:yyyy-mm-dd-hh also !!!!









we have to move all the specs to where they are used in clj files, and we should be ok .....












[Live site on Heroku](https://fathomless-woodland-85635.herokuapp.com).


