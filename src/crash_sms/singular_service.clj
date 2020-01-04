(ns crash-sms.singular-service
  (:gen-class)
(:require [global-consts-vars  :refer :all])
)

(defonce ^:dynamic *windows-java-service-kill-list* (atom {}))

(defn add-service
  [spawning-name kill-service]
  (let [kill-list @*windows-java-service-kill-list*
        spawning-key (keyword spawning-name)
        new-kills (assoc-in kill-list [spawning-key] kill-service)]
    (reset! *windows-java-service-kill-list* new-kills))
  spawning-name)

(defn remove-service
  [spawning-name]
  (let [kill-list @*windows-java-service-kill-list*
        spawning-key (keyword spawning-name)
        service-running? (contains? kill-list spawning-key)]
    (if service-running?
      (let [kill-service (spawning-key kill-list)] (kill-service))
      (let [new-kills (dissoc kill-list spawning-key)]
        (reset! *windows-java-service-kill-list* new-kills))))
  spawning-name)

(comment "to stop all services" (kill-services))
(defn kill-services
  []
  (let [kill-list @*windows-java-service-kill-list*]
    (if (nil? (resolve 'T-TEST-COLLECTION))
      (print-line "Killing all services:"))
    (for [[service-key kill-service] kill-list] (kill-service))))
