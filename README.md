



(-main    " uses temporize")


(-dev    "uses cron-init")








https://devcenter.heroku.com/articles/clojure-support


Procfile

web: lein with-profile production trampoline run -m sff-audio.web-stat monger-db ../heroku-config.edn use-environment





we need a command for heroku to run a cron job on temporize



ignore-environment


choose-db.clj is new name for check-db.clj







if exception on bind already in use, then show message 'End Java Processes'















