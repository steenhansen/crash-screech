
; https://fathomless-woodland-85635.herokuapp.com/

; A. in core.clj hit [F5] for cider-jack-in   (once in session)
; B. in core.clj hit [F4] for cider-ns-reload-all
; C. in core.clj hit [F3] for cider-repl-set-ns

; core> (-local-main USE_MONGER_DB LOCAL_CONFIG)
; core> (kill-services)

; core> (-local-main USE_FAKE_DB LOCAL_CONFIG)
; core> (kill-services)

; core> (-local-main USE_AMAZONICA_DB LOCAL_CONFIG)
; core> (kill-services)

; core> (-local-heroku-main USE_MONGER_DB HEROKU_CONFIG)    ;Will use Heroku MongoDb
; core> (kill-services)


(ns core
  (:gen-class)

  ; Never used in Cider, called by Heroku Procfile and "lein run "
  ; (:require [start-heroku :refer :all])

  ; (kill-services) - kills cron and ring
 ; (:require [crash-sms.singular-service :refer [kill-services]])

  ; (-local-heroku-main USE_MONGER_DB LOCAL_CONFIG)
  (:require [start-local-heroku-db :refer [-local-heroku-main]])

  ; (-local-main USE_MONGER_DB LOCAL_CONFIG)
  ; (-local-main USE_AMAZONICA_DB LOCAL_CONFIG)
  (:require [start-local :refer [-local-main]])

  (:require [global-consts-vars  :refer :all])

  (:require [crash-sms.check-data :refer :all])
  (:require [crash-sms.data-store :refer :all])
  (:require [crash-sms.config-args :refer :all])
  (:require [crash-sms.dynamo-db :refer :all])

  (:require [crash-sms.html-render :refer :all])
  (:require [crash-sms.mongo-db :refer :all])
  (:require [crash-sms.scrape-html :refer :all])
  (:require [crash-sms.singular-service :refer :all])

  (:require [crash-sms.sms-event :refer :all])
  (:require [crash-sms.web-server :refer :all])
  (:require [crash-sms.years-months :refer :all]))
