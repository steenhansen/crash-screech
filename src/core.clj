
; https://fathomless-woodland-85635.herokuapp.com/
 
; A. in core.clj hit [F5] for cider-jack-in   (once in session)
; B. in core.clj hit [F4] for cider-ns-reload-all
; C. in core.clj hit [F3] for cider-repl-set-ns

; core> (-local-main "monger-db" "./local-config.edn")
; core> (kill-services)                 

; core> (-local-main "amazonica-db" "./local-config.edn")
; core> (kill-services)                 

; core> (-local-heroku-main "monger-db" "../heroku-config.edn")
; Will use Heroku MongoDb

(ns core
  (:gen-class)

  ; Never used in Cider, called by Heroku Procfile and "lein run "
  ; (:require [start-heroku :refer :all])  

  ; (kill-services) - kills cron and ring 
  (:require [crash-screech.singular-service :refer [kill-services]])


  ; (-local-heroku-main "monger-db" "./local-config.edn") 
  (:require [start-local-heroku-db :refer [-local-heroku-main]])


  ; (-local-main "monger-db" "./local-config.edn") 
  ; (-local-main "amazonica-db" "./local-config.edn")  
  (:require [start-local :refer [-local-main]])



                                  ;(:require [start-local :refer :all])


  )





