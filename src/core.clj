

 
; /src/core.clj  =              

(ns core
  (:gen-class)

; (:require [start-heroku :refer :all])     ; Never used in Cider, called by Heroku Procfile and "lein run "

  (:require [crash-screech.singular-service :refer  [kill-services]]) ; (kill-services) - kills cron and ring 


  (:require   [test-sms :refer :all])      ; (-sms-test "../heroku-config.edn")
  (:require [core-test   :refer   :all])      ; (-do-tests)
  (:require     [start-local :refer :all])         ; (-local-main "monger-db" "./local-config.edn") 
                                              ; (-local-main "amazonica-db" "./local-config.edn")  
  )





