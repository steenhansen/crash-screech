


(def ^:const CRON-MILL-SECS 600000)          ; 1000 =sec               60000 =min   600000 = 10 min
(def ^:const CRON-CONTINUOUS true)       
(def ^:const CRON-SAVE true)      
(def ^:const CRON-READ true)   

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
     (if (> actual-matches wanted-matches)
         { :actual-matches actual-matches :the-status true}    
         { :actual-matches actual-matches :the-status false})))

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
  (if CRON-READ
      (let [ under-to-slashes (clj-str/replace check-page #"_" "/") ]
        (:body (http-client/get (str "https://" under-to-slashes))))
      (slurp (str "./test/" check-page))))

(defn scrape-pages-fn [my-db-obj pages-to-check time-fn] 
(println "i be scraping")
  (doseq [ check-page-obj pages-to-check
           :let [ 
                 
                   ;  {:keys [check-page enlive-keys at-least]} check-page-obj]                              
                 {check-page :check-page enlive-keys :enlive-keys at-least :at-least} check-page-obj
				               start-timer (System/currentTimeMillis)
				               web-html (read-html check-page)
                   
                   
;				              {:keys [actual-matches the-status}	(enough-sections? web-html enlive-keys at-least)
				               {actual-matches :actual-matches the-status :the-status}	(enough-sections? web-html enlive-keys at-least)
				               
                   tag-free (remove-tags web-html)
				               end-timer (System/currentTimeMillis)
				               time-spent (- end-timer start-timer)
				               url-with-slash (real-slash-url check-page)
				               check-record { :the-url url-with-slash
				                            	:the-date (time-fn)
					                            :the-html tag-free
					                            :the-status the-status
					                            :the-time time-spent } ] ]
			 (if CRON-SAVE
				  	 ((:put-item my-db-obj) check-record ))))

(defn scrape-pages-extra [my-db-obj pages-to-check time-fn] 
  (println "i be scraping EXTRA")
)

(defn cron-type [cron-func my-pool]   
   (if CRON-CONTINUOUS
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

(defn cron-init[cron-job table-name pages-to-check db-type config-file environment-utilize]
  (let [ my-db-obj (build-db table-name pages-to-check db-type config-file environment-utilize)
        scheduled-task (start-cron my-db-obj cron-job pages-to-check)]
    (remove-service cron-job kill-cron)    
    (add-service cron-job kill-cron scheduled-task)))
