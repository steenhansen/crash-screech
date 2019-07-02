
(ns sff-audio-test.the-tests
  (:require [clojure.test :refer :all]
            [sff-audio.web-server :refer :all]
            :reload-all)
     (:require [clj-http.client :as http-client])
 )

(def ^:const TEST-CONFIG-FILE "./local-config.edn")
(def ^:const SMS-NO-ERROR [])
(def ^:const DB-TEST-NAME "zzz2")

(load "dbs-turned-on")
(print-block)
(load "sms-send-fn")

(run-tests)

