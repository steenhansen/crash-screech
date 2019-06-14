

(load "check-data")
(load "choose-db")



;;;   NB we have to save a copy of my-db-obj as the passed in my-db-obj is garbage collected!!

(def ^:dynamic  *my-db-obj* (atom {}))


(def ^:const 	TEMPORIZE-CALL "/temporize-call")   


; below is for temporize calls, that are url cron jobs

(defn cron-via-url [my-db-obj cron-job-fn pages-to-check] 
  	   (defn url-func-call [] 
	       (cron-job-fn my-db-obj pages-to-check instant-time-fn))											 					
  url-func-call)


(defn single-cron-fn[cron-job-fn table-name pages-to-check db-type config-file environment-utilize]
  (let [ [my-db-obj _web-port] (build-db table-name pages-to-check db-type config-file environment-utilize)
        
         

        callable-cron (cron-via-url my-db-obj cron-job-fn pages-to-check)]

 (reset! *my-db-obj* my-db-obj)

    callable-cron))


