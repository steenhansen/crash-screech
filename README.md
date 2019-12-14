
 test-time() does not work anymore as








tests-scrape-html>  (sms-send-fn_error USE_MONGER_DB)

this was failing becuase the computer times were GMT 12:01 am at 2pm PAC ...

So local time ...   j-time/local-time is WRONG ..... becuase "UTC" is always 8 hour ahead

java-time/instant is UTC, thus 8 hours ahead
java-time/local is PAc, 8 hours behind so that previous errors today didn't work .... when
testing as testing used local expectations...


so in years_months.clj there are new date-with-now-time-fn version date-with-now-time-fn2
which will always pass















{:SEND_TEST_SMS_URL "/zxc",
 :PHONE_NUMBERS "12345678901,12345678901,12345678901",
 :HEROKU_APP_NAME "https://fathomless-woodland-85635.herokuapp.com/",
 :MONGODB_URI "mongodb://localhost:27017/local",
 :TILL_USERNAME "abcdefghijklmnopqrstuvwxyz1234",
 :TILL_API_KEY "1234567890abcdefghijklmnopqrstuvwxyz1234",
 :TILL_URL "https://platform.tillmobile.com/api/send",
 :CRON_URL_DIR "/url-for-cron-execution",
 :TESTING_SMS true,
 :PORT "8080"}
@@@@@ scrape-pages-fn today-error? send-hello-sms? #function[crash-screech.choose-db/build-today-error?/today-error?--92957] true
JJJJJJJJ scrape-pages-fn the-accurate false
XXXXXXXXXX scrape-html.first-error-today? 7 prev-errors-today? false
XXXXXXXXXXscrape-html.today-error? ? #function[crash-screech.choose-db/build-today-error?/today-error?--92957]
XXXXXXXXXXscrape-html.first-error-today? 7 now-error? false
scrape-html.send-sms-message 0 prev-errors-today? false
scrape-html.send-sms-message 0 send-err-sms? false
scrape-html.send-sms-message 1 send-hello-sms? true
scrape-html.send-sms-message 2 SMS-NEW-MONTH Start of a new SFFaudio month!
scrape-html.send-sms-message 3 (sms-send-fn SMS-NEW-MONTH) {:till-url https://platform.tillmobile.com/api/send?username=abcdefghijklmnopqrstuvwxyz1234&api_key=1234567890abcdefghijklmnopqrstuvwxyz1234, :sms-params {:form-params {:phone [12345678901 12345678901 12345678901], :text Start of a new SFFaudio month! - https://fathomless-woodland-85635.herokuapp.com/}, :content-type :json}}
scrape-html.scrape-pages-fn 230u0us ******  {:till-url https://platform.tillmobile.com/api/send?username=abcdefghijklmnopqrstuvwxyz1234&api_key=1234567890abcdefghijklmnopqrstuvwxyz1234, :sms-params {:form-params {:phone [12345678901 12345678901 12345678901], :text Start of a new SFFaudio month! - https://fathomless-woodland-85635.herokuapp.com/}, :content-type :json}}
XXXXXXXXXX scrape-html.first-error-today? 7 prev-errors-today? false
XXXXXXXXXXscrape-html.today-error? ? #function[crash-screech.choose-db/build-today-error?/today-error?--92957]
XXXXXXXXXXscrape-html.first-error-today? 7 now-error? false
scrape-html.send-sms-message 0 prev-errors-today? false
scrape-html.send-sms-message 0 send-err-sms? false
scrape-html.send-sms-message 1 send-hello-sms? true
scrape-html.send-sms-message 2 SMS-NEW-MONTH Start of a new SFFaudio month!
scrape-html.send-sms-message 3 (sms-send-fn SMS-NEW-MONTH) {:till-url https://platform.tillmobile.com/api/send?username=abcdefghijklmnopqrstuvwxyz1234&api_key=1234567890abcdefghijklmnopqrstuvwxyz1234, :sms-params {:form-params {:phone [12345678901 12345678901 12345678901], :text Start of a new SFFaudio month! - https://fathomless-woodland-85635.herokuapp.com/}, :content-type :json}}
|START| { :sms-params  { :content-typ ... 34567890112345678901 ] :text "
|DIFF1| "Start of a new SFFaudio month! -
|DIFF2| "Found an error -
|  END| - https://fathomless-woodland ... efghijklmnopqrstuvwxyz1234" }
FAIL in () (tests_scrape_html.clj:78)
expected: (= text-diff-1 text-diff-2)
  actual: (not (= "Start of a new SFFaudio month!" "Found an error"))
false
tests-scrape-html>

   choose_db/build-today-error?   seems to give the wrong answer if check_accurate is false in db






tests-scrape-html>  (sms-send-fn_error USE_MONGER_DB)

current error:

tests-scrape-html>
Testing tests-scrape-html
|START| { :sms-params  { :content-typ ... 34567890112345678901 ] :text "
|DIFF1| "Start of a new SFFaudio month! -
|DIFF2| "Found an error -
|  END| - https://fathomless-woodland ... efghijklmnopqrstuvwxyz1234" }

so in tests_scrape_html get-actual-sms() is not returning an error

















tests-scrape-html> (do-tests) does not pass spec on    check-data/prepare-data()
 url-date-tuple?

 prepare-tests line 09
(s/def :url-date-tuple?/test-specs  (s/keys :req-un [ ::the-url]
                                            :opt-un [ ::the-date  ::the-html ::the-accurate ::the-time]))























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
