


(def ^:const IGNORE-ENV-VARS "no-environment")

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


	(defn merge-environment-better [accum-environment env-object]
				  (let [  env-key (key env-object)
				          plain-key (name env-key)
				          the-value (val env-object)
              system-environment (assoc-in accum-environment [env-key] (System/getenv plain-key))
              program-environment (assoc-in accum-environment [env-key] the-value)]
   (if (System/getenv plain-key)
          system-environment
          program-environment)))

(defn make-config [db-type config-file environment-utilize]
				(let [config-vars (read-project-file config-file)
            db-keyword (keyword db-type)
						      db-config (get config-vars db-keyword)
						      ignore-environmentals (= environment-utilize IGNORE-ENV-VARS)          
            has-environmentals (reduce merge-environment {} db-config)]
              (if ignore-environmentals
                  db-config
                  has-environmentals)))




						
 



