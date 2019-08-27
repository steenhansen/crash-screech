
; https://fathomless-woodland-85635.herokuapp.com/
 
; A. cider-jack-in core.clj [==]
; B. cider-repl-set-ns core.clj [--]
; C. cider-ns-reload-all  [F12]

; core> (-local-main "monger-db" "./local-config.edn")
; core> (kill-services)                 

; core> (-local-main "amazonica-db" "./local-config.edn")
; core> (kill-services)                 

; core> (-local-heroku-main "monger-db" "../heroku-config.edn")
; Will use Heroku MongoDb

(ns core
  (:gen-class)

; (:require [start-heroku :refer :all])     ; Never used in Cider, called by Heroku Procfile and "lein run "

  (:require [crash-screech.singular-service :refer  [kill-services]]) ; (kill-services) - kills cron and ring 

  (:require     [start-local :refer :all])  ; (-local-main "monger-db" "./local-config.edn") 
                                            ; (-local-main "amazonica-db" "./local-config.edn")  
  )





