; /src/all_main.clj   

(ns all-main
  (:gen-class)
  
 ; (:require [heroku-start :refer :all])     ; Never used in Cider, called by Heroku Procfile and "lein run "
  
  
  (:require [send-sms-test :refer :all])      ; (-sms-test "../heroku-config.edn")
  (:require [execute-tests :refer :all])      ; (-do-tests)
 
  (:require [local-heroku-start :refer :all]) ; (-local-heroku-main "monger-db" "../heroku-config.edn")       ;Heroku Mlabs 
 
 (:require [local-start :refer :all])         ; (-local-main "monger-db" "./local-config.edn") 
                                              ; (-local-main "amazonica-db" "./local-config.edn")  
)

