
; https://fathomless-woodland-85635.herokuapp.com/

; A. in core.clj hit [F5] for cider-jack-in   (once in session)
; B. in core.clj hit [F4] for cider-ns-reload-all
; C. in core.clj hit [F3] for cider-repl-set-ns

; core> (-local-main USE_MONGER_DB LOCAL_CONFIG)
; core> (kill-services)

; core> (-local-main USE_AMAZONICA_DB LOCAL_CONFIG)
; core> (kill-services)

; core> (-local-heroku-main USE_MONGER_DB HEROKU_CONFIG)
; Will use Heroku MongoDb

(ns core
  (:gen-class)

  ; Never used in Cider, called by Heroku Procfile and "lein run "
  ; (:require [start-heroku :refer :all])

  ; (kill-services) - kills cron and ring
 ; (:require [crash-screech.singular-service :refer [kill-services]])

  ; (-local-heroku-main USE_MONGER_DB LOCAL_CONFIG)
  (:require [start-local-heroku-db :refer [-local-heroku-main]])

  ; (-local-main USE_MONGER_DB LOCAL_CONFIG)
  ; (-local-main USE_AMAZONICA_DB LOCAL_CONFIG)
  (:require [start-local :refer [-local-main]])
                                 ;(:require [start-local :refer :all])

  (:require [global-consts-vars  :refer :all])

  (:require [crash-screech.check-data :refer :all])
  (:require [crash-screech.choose-db :refer :all])
  (:require [crash-screech.config-args :refer :all])
  (:require [crash-screech.cron-service :refer :all])
  (:require [crash-screech.dynamo-db :refer :all])

 (:require [crash-screech.html-render :refer :all])
  (:require [crash-screech.mongo-db :refer :all])
  (:require [crash-screech.scrape-html :refer :all])
  (:require [crash-screech.singular-service :refer :all])

   (:require [crash-screech.sms-event :refer :all])
   (:require [crash-screech.web-server :refer :all])
   (:require [crash-screech.years-months :refer :all])

  )
