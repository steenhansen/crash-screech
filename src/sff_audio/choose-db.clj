
(load "dynamo-db")
(load "mongo-db")
(load "check-data")




(defn get-db-conn
  [table-name pages-to-check db-type the-config]
  (let [db-keyword (keyword db-type)]
    (try (case db-keyword
           :amazonica-db (dynamo-build the-config table-name pages-to-check)                  
           :monger-db (mongolabs-build the-config table-name pages-to-check))
         (catch Exception e
           (println " get-db-conn - " db-keyword
                    " -caught exception: " (.getMessage e))))))

(defn build-empty-month?
  [get-all]

    (defn empty-month?
      []
      (let [yyyy-mm (this-y-m)
            url-checks (get-all yyyy-mm)
            months-checks (count url-checks)]
        (if (= 0 months-checks)
          true
          false
          )))
    empty-month?)


(defn build-today-error?
  [get-all]
  (let [FOUND-FAILED-CHECK true
        ALL-ACCURATE-CHECKS false]
    (defn failed-check
      [found-error? url-check]
      (if (:check-accurate url-check)
        ALL-ACCURATE-CHECKS            ; return true early once a
        (reduced FOUND-FAILED-CHECK))  ; failed check is found
      )
    (defn today-error?
      []
      (let [yyyy-mm (this-y-m)
            url-checks (get-all yyyy-mm)
            error-found (reduce failed-check false url-checks)]
        error-found))
    today-error?))

(defn build-db
  [table-name pages-to-check db-type config-file environment-utilize]
  (let [the-config (make-config db-type config-file environment-utilize)
        my-db-funcs (get-db-conn table-name pages-to-check db-type the-config)
        web-port (:PORT the-config)
        cron-url (:CRON-URL-DIR the-config)
        till-username (:TILL_USERNAME the-config)
        till-api-key (:TILL_API_KEY the-config)
        phone-numbers (:PHONE-NUMBERS the-config)
        heroku-app-name (:HEROKU_APP_NAME the-config)
        sms-data (compact-hash till-username
                               till-api-key
                               phone-numbers
                               heroku-app-name)
        get-all (:get-all my-db-funcs)
        my-db-obj {:delete-table (:delete-table my-db-funcs)
                   :purge-table (:purge-table my-db-funcs)
                   :get-all get-all
                   :get-url (:get-url my-db-funcs),
                   :put-item (:put-item my-db-funcs),
                   :put-items (:put-items my-db-funcs),
                   :empty-month? (build-empty-month? get-all)
                   :today-error? (build-today-error? get-all)}]
    [my-db-obj web-port cron-url sms-data]))
