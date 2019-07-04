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
    
    
 




