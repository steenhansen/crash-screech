
; /src/core.clj   ..a........a..aa.....a.....aAAaaass

(ns core
  (:gen-class)

  (:require [spec-types  :refer :all])
  (:require [crash-screech.singular-service :refer  [kill-services]])




 ; (:require [heroku-start :refer :all])     ; Never used in Cider, called by Heroku Procfile and "lein run "


  (:require [send-sms-test :refer :all])      ; (-sms-test "../heroku-config.edn")
  (:require [core-test :refer :all])      ; (-do-tests)
;(:require [qwe-456 :refer :all] )



  (:require [local-heroku-start :refer :all]) ; (-local-heroku-main "monger-db" "../heroku-config.edn")       ;Heroku Mlabs 

  (:require [local-start :refer :all])         ; (-local-main "monger-db" "./local-config.edn") 
                                              ; (-local-main "amazonica-db" "./local-config.edn")  
  )


