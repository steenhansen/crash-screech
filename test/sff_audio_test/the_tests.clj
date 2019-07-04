
(ns sff-audio-test.the-tests
  (:require [clojure.test :refer :all]
            [sff-audio.web-server :refer :all]
            :reload-all)
     (:require [clj-http.client :as http-client])
       (:require [clojure.string :as clj-str])
 )

(def ^:const TEST-CONFIG-FILE "./local-config.edn")
(def ^:const SMS-NO-ERROR [])
(def ^:const DB-TEST-NAME "zzz2")

(load "dbs-turned-on")
(print-block)
(load "scrape-html/sms-send-fn-test")



(load "years-months/date-to-yyyy-mm-test")
(load "years-months/month-name-test")
(load "years-months/yyyy-mm-to-ints-test")
(load "years-months/current-yyyy-mm-test")
(load "years-months/prev-yyyy-mm-test")

(load "years-months/adjusted-date-test")
(load "years-months/ensure-date-test")


(load "years-months/prev-month-test")

(load "years-months/current-month-test")


(load "html-render/get-index-test")



(run-tests)

