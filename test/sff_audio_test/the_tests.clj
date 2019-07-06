
(def ^:const TEST-CONFIG-FILE "./local-config.edn")
(def ^:const SMS-NO-ERROR [])
(def ^:const DB-TEST-NAME "zzz2")
(def ^:const TEST-DIR "../../test/sff_audio_test/")

(load (str TEST-DIR "dbs-turned-on"))
(print-block)

(load (str TEST-DIR "checked-data/-checked-data-tests"))
(load (str TEST-DIR "choose-db/-choose-db-tests"))
(load (str TEST-DIR "html-render/-html-render-tests"))
(load (str TEST-DIR "scrape-html/-scrape-html-tests"))
(load (str TEST-DIR "years-months/-years-months-tests"))

(spec-test/instrument) 
(run-tests)

