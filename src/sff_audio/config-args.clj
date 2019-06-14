
(def ^:const IGNORE-ENV-VARS "ignore-environment")


(defn read-config-file [config-file]
  (let [config-path (str (file-sys/absolute config-file))]
    (println "Trying to load" config-path )
    (try 
 					 (edn-read/load-file config-file)
        (catch Exception e (println "Failed to load" (.getMessage e))))))

	(defn merge-environment [accum-environment env-object]
				  (let [  env-key (key env-object)
				          plain-key (name env-key)
				          the-value (val env-object)
              system-environment (assoc-in accum-environment [env-key] (System/getenv plain-key))
              program-environment (assoc-in accum-environment [env-key] the-value)]
   (if (System/getenv plain-key)
          system-environment
          program-environment)))

(defn make-config [db-type config-file environment-utilize]
				(let [config-vars (read-config-file config-file)
            db-keyword (keyword db-type)
						      db-config (get config-vars db-keyword)
						      ignore-environmentals (= environment-utilize IGNORE-ENV-VARS)          
            has-environmentals (reduce merge-environment {} db-config)]
       (if ignore-environmentals
           db-config
           has-environmentals)))




						
 



