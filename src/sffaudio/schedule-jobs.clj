

(ns sffaudio.web-stat
  (:require [overtone.at-at :as at-at])       
  (:require [java-time :as jav-time])             
 (:require [net.cgrand.enlive-html :as html])
  (:require [clojure.string :as clj-str])   
)




(def cron--job--queue (atom []))

  (defn add-cron-job-to-queue [new-cron-job]
  (swap! cron--job--queue conj new-cron-job )
)


(defn stop-cron-jobs []
(let [cron-job-queue @cron--job--queue]
(println "-------------------------------")	
(println "cron-job-queue" (type cron-job-queue))	
(println "*******************************")	

						( doseq [ cron-job cron-job-queue ]
											(println "KILLING - " cron-job) 
	          (at-at/stop cron-job) ) )
)

 (defn ensure-one-cron-job []
 (println "@cron--job--queue 1111 " @cron--job--queue)
 		   ( if (= (count @cron--job--queue) 2)
																		      ( let [  old-cron (first @cron--job--queue)
                                 new-cron (second @cron--job--queue)
																		                 ;return-swap (swap! cron--job--queue second )
																		                 ] 
																		                 (reset! cron--job--queue [new-cron])
																		              ;   (println "-------------------------------")	
                          (println "@cron--job--queue 2222 " @cron--job--queue)
;(println "*******************************")	
;(println "-------------------------------")																		                 
;(println "old-cron" old-cron)																		                 
;(println "return-swap" return-swap)																		                 
;(println "@cron--job--queue" @cron--job--queue)																		                 
																		               (at-at/stop old-cron)
																		      )
																		   )
)




;; "2015-09-26T05:25:48.667Z"
(defn instant-time-fn []
  (str (jav-time/instant) )
)

(defn get-the-name [accum item]
     (let [ [head-name head-value] item]
								 ( if (= head-name "Content-Length")
								       head-value 
								       accum )))

(defn test-page-bytes-fn [_]
    (rand-int 30))



(defn matching-css-sections[check-html css-match]    
    (map html/text
       (html/select
             (-> check-html 
             java.io.StringReader. 
             html/html-resource)
              css-match))
   )

(defn enough-sections? [the-html css-match wanted-matches]   
    ( let [actual-sections (matching-css-sections the-html css-match)
           actual-matches (count actual-sections)]
   (if (> actual-matches wanted-matches)
       { :actual-matches actual-matches :page-ok true}    
        { :actual-matches actual-matches :page-ok false}
        )
   )
)

(defn remove-tags [the-html page-ok]
  (if page-ok "ok"
 ; (if false "ok"
   (let [ no-head (clj-str/replace the-html #"<head[\w\W]+?</head>" "")
          no-styles (clj-str/replace no-head #"<style[\w\W]+?</style>" "")
          no-js (clj-str/replace no-styles #"<script[\w\W]+?</script>" "")
 									no-end-tags (clj-str/replace no-js #"</[\w\W]+?>" "")
          no-start-tags (clj-str/replace no-end-tags #"<[\w\W]+?>" "")
          no-multi-spaces (clj-str/replace no-start-tags #"\s\s+" ".")
       ]
      no-multi-spaces
  )))

(defn test-time-fn []
  "2019-05-20T01:54:03.841Z" )

(defn real-slash-url [check-page]
  (clj-str/replace check-page #"_" "/"))

(defn read-html [cron-read-real check-page]
      ( if cron-read-real
                (let [ under-to-slashes (clj-str/replace check-page #"_" "/") ]
                         (:body (clj-http.client/get (str "https://" under-to-slashes))))
              (slurp (str "./test/" check-page ))))

(defn scrape-pages [my-db-obj pages-to-check cron-save-db cron-read-real time-fn] 
     (ensure-one-cron-job)
     (doseq [ check-page-obj pages-to-check
            :let  [ {check-page :check-page enlive-keys :enlive-keys count-at-least :count-at-least} check-page-obj
				    start-timer (System/currentTimeMillis)
				    the-html (read-html cron-read-real check-page)
				    {actual-matches :actual-matches page-ok :page-ok }			 (enough-sections? the-html enlive-keys count-at-least)
				    tag-free (remove-tags  the-html page-ok)
				    end-timer (System/currentTimeMillis)
				    time-spent (- end-timer start-timer)
				    url-with-slash (real-slash-url check-page)
				    check-record11 { :check-url url-with-slash
				                    	:the-date (time-fn)
					                    :the-html tag-free
					                    :page-ok page-ok
					                    :time-took time-spent } ] ] 
				   	( if cron-save-db
				       	 ((:put-item my-db-obj) check-record11 ))))

(defn cron-type [cron-run-always cron-seconds the-cron-func my-pool]     
   (if cron-run-always
        (at-at/every cron-seconds the-cron-func my-pool)
        (at-at/after cron-seconds the-cron-func my-pool)))

(defn start-cron [my-db-obj pages-to-check cron-info] 
  (let [ {cron-seconds :cron-seconds cron-run-always :cron-run-always cron-save-db :cron-save-db
          cron-read-real :cron-read-real} cron-info ] 
	   (defn the-cron-func [] 
	        (scrape-pages my-db-obj pages-to-check cron-save-db cron-read-real instant-time-fn))											 					

  (let [ my-pool (at-at/mk-pool) 
  
     at-at-stop-ref (cron-type cron-run-always cron-seconds the-cron-func my-pool) ]
  
						(defn stop-cron []
								(try (at-at/stop-and-reset-pool! my-pool)
             (catch Exception e (str " -- caught exception: " )))      ;; (stop-cron)
								 "cron job stopped via my-pool"		)

    at-at-stop-ref ))   )   



