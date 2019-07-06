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
    
    
    
    (defn print-block []
      
              (println "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
                     (println "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
                     (println "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
                     (println "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
                     (println "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
                     (println "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
                     (println "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
                     (println "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
                     (println "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
                     (println "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
                     (println "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
                     (println "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
                     (println "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
      )
    
    (defn  strip-white-space [my-text] 
          (clj-str/replace my-text #"\s" ""))
 
    
    
    
    (defn reset-test-to-actual-data [test-data actual-data]
    (spit test-data actual-data))
    
    


