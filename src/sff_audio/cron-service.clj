
(defn get-the-name [accum item]
  (let [ [head-name head-value] item]
    ( if (= head-name "Content-Length")
     head-value 
     accum )))

(defn matching-css-sections[pages-html css-match]    
  (map enlive-html/text
       (enlive-html/select
        (-> pages-html 
            java.io.StringReader. 
            enlive-html/html-resource)
        css-match)))

(defn enough-sections? [the-html css-match wanted-matches]   
  (let [actual-sections (matching-css-sections the-html css-match)
        actual-matches (count actual-sections)]
    (if (>= actual-matches wanted-matches)
      { :actual-matches actual-matches :the-accurate true}    
      { :actual-matches actual-matches :the-accurate false})))

(defn remove-tags [the-html]
  (let [ no-head (clj-str/replace the-html #"<head[\w\W]+?</head>" "")
        no-styles (clj-str/replace no-head #"<style[\w\W]+?</style>" "")
        no-js (clj-str/replace no-styles #"<script[\w\W]+?</script>" "")
        no-end-tags (clj-str/replace no-js #"</[\w\W]+?>" "")
        no-start-tags (clj-str/replace no-end-tags #"<[\w\W]+?>" "")
        no-multi-spaces (clj-str/replace no-start-tags #"\s\s+" ".") ]
    no-multi-spaces))

(defn real-slash-url [check-page]
  (clj-str/replace check-page #"_" "/"))

(defn read-html [check-page]
  (if CRON-DO-SCRAPE
    (let [ under-to-slashes (clj-str/replace check-page #"_" "/") ]
      (:body (http-client/get (str "https://" under-to-slashes))))
    (slurp (str "./test/" check-page))))


(defn first-error-todayXX [prev-today-error? my-db-obj]
  (let [ now-error? (:today-error? my-db-obj) ]
    (if-not prev-today-error?
      (if now-error?
        true
        false)
      false)))

(defn first-error-today [prev-today-error? my-db-obj]
  (let [ now-error? ((:today-error? my-db-obj)) ]
    (if prev-today-error?
        false
        now-error?)))

(defn scrape-pages-fn [my-db-obj pages-to-check time-fn sms-fn] 
  (println "scrape-pages-fn executing ...")
  (let [prev-today-error? ((:today-error? my-db-obj))]
    (doseq [check-page-obj pages-to-check
					       :let [{:keys [check-page enlive-keys at-least]} check-page-obj                              
									                 start-timer (System/currentTimeMillis)
									                 web-html (read-html check-page)
									                 end-timer (System/currentTimeMillis)
									                 the-time (- end-timer start-timer)
									                 {:keys [actual-matches the-accurate]}	(enough-sections? web-html enlive-keys at-least)
									                 the-url (real-slash-url check-page)
									                 the-date (time-fn)
									                 the-html (remove-tags web-html)
									                 check-record (compact-hash the-url the-date the-html the-accurate the-time)]]
				 	((:put-item my-db-obj) check-record)
      (if (first-error-today prev-today-error? my-db-obj)
        (sms-fn)))))       
  
(defn cron-type [cron-func my-pool]   
  (println "crontype" CRON-RUN-CONTINUOUS)
  (if CRON-RUN-CONTINUOUS
    (at-at/every CRON-MILL-SECS cron-func my-pool)
    (at-at/after CRON-MILL-SECS cron-func my-pool)))  

(defn kill-cron [cron-ref] 
  (println "Killing cron-service:" cron-ref)
  (at-at/stop cron-ref))

(defn start-cron [my-db-obj cron-job pages-to-check] 

  (defn cron-func [] 
    (cron-job my-db-obj pages-to-check instant-time-fn))	

  (let [thread-pool (at-at/mk-pool) 
        scheduled-task (cron-type cron-func thread-pool)]
    
    (defn remove-crons []
      (println "Removing all cron-services")
      (try (at-at/stop-and-reset-pool! thread-pool)
           (catch Exception e (println " -- caught exception " (.getMessage e)))))

    scheduled-task))

(defn cron-init[cron-job my-db-obj pages-to-check ]
  (let [scheduled-task (start-cron my-db-obj cron-job pages-to-check)]
    (remove-service cron-job kill-cron)    
    (add-service cron-job kill-cron scheduled-task)))
