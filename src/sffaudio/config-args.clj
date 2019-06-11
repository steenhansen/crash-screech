
(ns sffaudio.web-stat
	 (:require [me.raynes.fs :as fs])
)

(defn db-2-args-config [start-args]
  (keyword (str (first start-args) "-" (second start-args))))   ;monger-heroku

(defn db-1-arg-config [start-args]
 (keyword (first start-args )))

(defn read-project-file [config-file]
  (let [config-path (str (fs/absolute config-file))]
    (println "Trying to load" config-path )
    (try 
 					(edn-read/load-file config-file)
        (catch Exception e (println "Failed to load" (.getMessage e))))))


	(defn merge-environment [env-vector  env-tuple]
				  (let [  the-key (key env-tuple)
				          no-colon-key (name the-key)
				          the-value (val env-tuple)
              sys-vector (assoc-in env-vector [the-key] (System/getenv no-colon-key))
              new-vector (assoc-in env-vector [the-key] the-value)]
   (if (System/getenv no-colon-key)
          sys-vector
          new-vector)))

(defn make-config [start-args config-vars]
				(let [config-double-vars (db-2-args-config start-args)    
						      db-double-config (get config-vars config-double-vars)
						      third-argument (nth start-args 2 "use-environment")
						      ignore-environmentals (= third-argument "no-environment")
            has-environmentals (reduce merge-environment {} db-double-config)]
              (if ignore-environmentals
                  db-double-config
                  has-environmentals)))


;; (-main "monger" "localhost" "use-environment")      defaults
;; (-main "monger" "localhost" )

;; (-main "monger" "localhost" "no-environment")
(defn load-config [start-args config-file]
  (let [config-vars (read-project-file config-file)]
		  (if (= 0 (count start-args))
				  (throw (Exception. " No db-type, try (-main \"monger\") or (-main \"amazonica\") "))
    (if (= 1 (count start-args))
						(let [db-arg (first start-args)               ;(-main "monger") => (-main "monger" "localhost" "use-environment")
							     start-localhost [db-arg "localhost"]]
						   (make-config start-localhost config-vars) )
      (make-config start-args config-vars)))))         ;(-main "monger" "localhost" )



						
 



