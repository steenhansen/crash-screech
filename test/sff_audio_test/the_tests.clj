


(def ^:const TEST-CONFIG-FILE "./local-config.edn")
(def ^:const SMS-NO-ERROR [])
(def ^:const DB-TEST-NAME "zzz3")
(def ^:const TEST-DIR "../test/sff_audio_test/")

(load (str TEST-DIR "dbs-turned-on"))



(load (str TEST-DIR "checked-data/tests"))
(load (str TEST-DIR "choose-db/tests"))
(load (str TEST-DIR "html-render/tests"))
(load (str TEST-DIR "scrape-html/tests"))
(load (str TEST-DIR "years-months/tests"))



(io.aviso.repl/install-pretty-exceptions)




(spec-test/instrument)

(defn check-testing []
   (local-dynamodb-on?)
 
  (dampen-mongo-logging)
  (local-mongo-on?) 
  (sms-is-in-test :monger-db)
)

(defn checked-data-tests []
  (test-count-string)
  (test-derive-data)
  (test-ensure-has-date)
  (test-prepare-data)
  (test-sub-string)
  (test-trunc-string)
  (test-uniquely-id)
)

(defn choose-db-tests []
 (test-get-phone-nums)
)

(defn html-render-tests []
  (test-count-scrapes)
  (test-day-hour-min)
  (test-get-index)
  (test-get-two-months)
  (test-make-request-fn1)
  (test-make-request-fn2)
  (test-show-data)
)

(defn scrape-html-tests []
  (test-1000)
  (test-1001)
  (test-1002)
  (test-1003)
  (test-2000)
  (test-2001)
)

(defn years-months-tests []
  (test-adjusted-date)
  (test-current-month)
  (test-current-yyyy-mm)
  (test-ensure-date)
  (test-month-name_0)
  (test-month-name_1)
  (test-month-name_-1)
  (test-prev-month)
  (test-prev-yyyy-mm)
  (test-yyyy-mm-to-ints)
)

(defn test-suite []
  (checked-data-tests)
  (choose-db-tests)
  (html-render-tests)
  (scrape-html-tests)
 (years-months-tests)
)




(defn test-ns-hook [] (test-suite))

(defn -run-tests []
   ; (try           
   ;   (println "Nice stack trace print example, via io.aviso/pretty")
   ;   ("nice_stack_trace_print")
   ;    (catch Exception e (clojure.repl/pst e))   
   ;  )
  (reset! *we-be-testing* true)
  (kill-services)
  (check-testing)
	 (run-tests 'main-sff-audio)
 )


