; /src/all_main.clj   

(ns all-main
  (:gen-class)
  ; (:require [heroku-start :refer :all])     ; Never used in Cider, called by Heroku Procfile

  (:require [send-sms-test :refer :all])      ; (-sms-test "../heroku-config.edn" "use-environment")
  (:require [execute-tests :refer :all])      ; (-do-tests)
 
  (:require [local-heroku-start :refer :all])
  (:require [local-start :refer :all])        ; (-local-main "monger-db" "./local-config.edn" "ignore-environment") 
                                              ; (-local-main "amazonica-db" "./local-config.edn" "ignore-environment")  
)

