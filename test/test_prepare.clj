

;   a

; https://github.com/clojure/tools.logging   => log4j.properties
; http://www.paullegato.com/blog/logging-clojure-clj-logging-config/

(ns test-prepare
  (:require [clj-logging-config.log4j :as log-config]
            [clojure.tools.logging :as log])
  (:require [clj-http.client :as http-client])

  (:require [clojure.string :as clj-str])

  (:use [sff-global-consts])

  (:use [crash-screech.choose-db]))

(defn dampen-mongo-logging
  []
  (log-config/set-logger!
   :level :debug
   :out (org.apache.log4j.FileAppender.
         (org.apache.log4j.EnhancedPatternLayout.
          org.apache.log4j.EnhancedPatternLayout/TTCC_CONVERSION_PATTERN)
         "logs/foo.log"
         true)))

(defn local-dynamodb-on?
  []
  (try
    (http-client/get "http://localhost:8000/shell/")
    (catch Exception e
      (throw  (Exception. " **** ERROR DynamoDB is not running on - http://localhost:8000")))))

(defn local-mongo-on?
  []
  (try (http-client/get "http://localhost:27017")
       (catch Exception e
         (throw
          (Exception.
           " **** ERROR MongoDB is not running on - http://localhost:27017")))))

(defn strip-white-space [my-text] (clj-str/replace my-text #"\s" ""))

(defn reset-test-to-actual-data
  [test-data actual-data]
  (spit test-data actual-data))

(defn print-block
  []
  (println
   "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"))

(defn sms-is-in-test [db-type]
  (let [[_ _ _ sms-data] (build-db DB-TEST-NAME
                                   {}
                                   db-type
                                   TEST-CONFIG-FILE
                                   IGNORE-ENV-VARS)
        testing-sms? (:testing-sms? sms-data)]
    (if (not testing-sms?)  (throw
                             (Exception.
                              " **** ERROR :TESTING_SMS is NOT false")))))

(defn is-string-number
  [num-as-str]
  (try
    (let [the-number (Integer/parseInt num-as-str)
          is-number (number? the-number)
          bigger-0 (< 0 the-number)]
      (and is-number bigger-0))
    (catch Exception e
      false)))

(defn is-url-dir
  [])
