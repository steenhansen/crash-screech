     
; https://github.com/clojure/tools.logging   => log4j.properties
; http://www.paullegato.com/blog/logging-clojure-clj-logging-config/
(log-config/set-logger! :level :debug
                        :out (org.apache.log4j.FileAppender.
                              (org.apache.log4j.EnhancedPatternLayout. org.apache.log4j.EnhancedPatternLayout/TTCC_CONVERSION_PATTERN)
                              "logs/foo.log"
                              true))

     
     
     ;    (try 
     ;      (http-client/get "http://localhost:8000/shell/")
     ;     (catch Exception e
     ;       (throw  (Exception. " **** ERROR DynamoDB is not running on - http://localhost:8000")
           
     ;       )))
    

     (try 
          (http-client/get "http://localhost:27017")
         (catch Exception e
           (throw  (Exception. " **** ERROR MongoDB is not running on - http://localhost:27017")
           
           )))
     
(defn strip-white-space [my-text] 
          (clj-str/replace my-text #"\s" ""))
 
    
    
    
    (defn reset-test-to-actual-data [test-data actual-data]
    (spit test-data actual-data))
    
    
 
    
    
    (defn print-block []
      (println "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"))
    


