
(ns all-main
  (:gen-class)
  (:require [heroku-start :refer :all])
  (:require [local-heroku-start :refer :all])
  (:require [local-start :refer :all])
  (:require [send-sms-test :refer :all])
)



(load "../test/sff_audio_test/the_tests")                                        ; (-do-tests) 


(comment "local monger db"                             (-local-main "monger-db" "./local-config.edn" "ignore-environment"))

(comment "local amazonica db"                          (-local-main "amazonica-db" "./local-config.edn" "ignore-environment"))
(comment "real monger db, config file outside project" (-local-main "monger-db" "../heroku-config.edn" "ignore-environment"))
(comment "to send sms message to phone"                (-sms-test "monger-db" "../heroku-config.edn" "use-environment"))
