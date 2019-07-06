




(def ^:const TEST-CONFIG-FILE "./local-config.edn")
(def ^:const SMS-NO-ERROR [])
(def ^:const DB-TEST-NAME "zzz2")



(def ^:const TEST-DIR "../../test/sff_audio_test/")



 (load (str TEST-DIR "dbs-turned-on"))
 
 
 
 
 
 (print-block)
 
 
 
 
 (load (str TEST-DIR "scrape-html/sms-send-fn-test"))



 (load (str TEST-DIR "years-months/date-to-yyyy-mm-test"))
 (load (str TEST-DIR "years-months/month-name-test"))
 (load (str TEST-DIR "years-months/yyyy-mm-to-ints-test"))
 (load (str TEST-DIR "years-months/current-yyyy-mm-test"))
 (load (str TEST-DIR "years-months/prev-yyyy-mm-test"))

(load (str TEST-DIR "years-months/adjusted-date-test"))


(load (str TEST-DIR "years-months/ensure-date-test"))


(load (str TEST-DIR "years-months/prev-month-test"))

(load (str TEST-DIR "years-months/current-month-test"))


 (load (str TEST-DIR "html-render/get-index-test"))

 


 
 
(load (str TEST-DIR "html-render/show-data-test"))



(load (str TEST-DIR "html-render/day-hour-min-test"))
(load (str TEST-DIR "html-render/day-hour-min-test"))






(spec-test/instrument) 
(run-tests)

 ;(spec-test/check 'fill-bytes)