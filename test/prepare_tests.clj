


;;  general-specs for all the specs


;   get-two-months

; https://github.com/clojure/tools.logging   => log4j.properties
; http://www.paullegato.com/blog/logging-clojure-clj-logging-config/

(ns prepare-tests
  (:require [clj-logging-config.log4j :as log-config]
            [clojure.tools.logging :as log])
  (:require [clj-http.client :as http-client])

  (:require [clojure.string :as clj-str])

  (:require [global-consts-vars  :refer :all])

  (:require [crash-sms.choose-db :refer :all])
  (:require [general-specs :refer :all])

)



(defn console-test
  ([test-name]
      (console-test test-name "" "" ""))
  ([test-name test-1]
     (console-test test-name test-1 "" ""))
  ([test-name test-1 test-2]
     (println "..." test-name test-1 test-2 ""))
  ([test-name test-1 test-2 test-3]
     (println "..." test-name test-1 test-2 test-3))
)

;  (ddiff/pretty-print (ddiff/diff expected-get-index actual-get-index) )
(defn conform-whitespace
  [rn-text]
  (let [
       n-text (clj-str/replace rn-text  #"\r\n" "\n")
       no-eoln (clj-str/replace n-text  #"\n" "")
        one-spaces  (clj-str/replace no-eoln  #"\s\s+" " ")
        bracket-space  (clj-str/replace one-spaces  #">\s<" "><")
        trimmed-unix (clj-str/trim bracket-space)]
   trimmed-unix))


(defn dampen-mongo-logging
  []
  (log-config/set-logger!
   :level :debug
   :out (org.apache.log4j.FileAppender.
         (org.apache.log4j.EnhancedPatternLayout.
          org.apache.log4j.EnhancedPatternLayout/TTCC_CONVERSION_PATTERN)
         "logs/foo.log"
         true)))


;; reset-expected-to-actual-data
(defn reset-test-to-actual-data
  [test-file actual-data]
  (let  [test-path (str SCRAPED-TEST-DATA test-file)]
    (println test-file test-path)
 ; (spit test-path actual-data)
    ))

(defn print-block
  []
  (println
   "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"))

(defn sms-is-in-test [db-type]
  (let [[_ _ _ sms-data] (build-db T-TEST-COLLECTION
                                   []
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
  [url-dir-leading-slash]
  (try
    (let [the-slash (first url-dir-leading-slash)
          the-dir (subs url-dir-leading-slash 1)]
      (and (= the-slash "/") (string? the-dir)))
    (catch Exception e
      false)))
