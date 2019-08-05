
; /src/core.clj   ..a........a..aa.....a.....aAAaaass

(ns core
  (:gen-class)

; (:require [heroku-start :refer :all])     ; Never used in Cider, called by Heroku Procfile and "lein run "

  (:require [crash-screech.singular-service :refer  [kill-services]]) ; (kill-services) - kills cron and ring 


  (:require [send-sms-test :refer :all])      ; (-sms-test "../heroku-config.edn")
  (:require [core-test :refer :all])      ; (-do-tests)
  (:require [local-start :refer :all])         ; (-local-main "monger-db" "./local-config.edn") 
                                              ; (-local-main "amazonica-db" "./local-config.edn")  
  )


