 
(defn aquire-project-file []
   ( ->> "project.clj"
     (slurp)
     (read-string)
      (drop 3)
     (apply hash-map)    
     (:main) 
     (name)))

(defn get-env-value [env-key]
  (if ( System/getenv env-key)
     ( System/getenv env-key)
     ( str "no env value for " env-key)))

 (defn db-2-args-config [start-args]
  (keyword ( str (first start-args) "-" (second start-args))))

 (defn db-1-arg-config [start-args]
  (keyword ( first start-args )))

(defn get-real-envs [db-config]
   (for [[env-key _] db-config
      :let [  str-name        (name env-key)
              environment-val ( System/getenv str-name)]]
      {env-key environment-val}))


( defn project-file-name [config-suffix]
   (str 
    ( -> (aquire-project-file) 
         (clj-str/split   #"\.")
        (last)
        )
    config-suffix )  )

( defn read-project-file [config-suffix]
   (let [config-file-name (project-file-name config-suffix)
         outside-passwords ( str "../" config-file-name)  
         inside-passwords ( str "" config-file-name)]
    (try 
 					(edn-read/load-file outside-passwords)
 					(catch Exception e 
 					     (println "Warning missing " outside-passwords " file, now trying " inside-passwords)
  					    (try 
 					         (edn-read/load-file inside-passwords)
               (catch Exception e (str " Missing cofig file: " inside-passwords (.getMessage e))))))))           

 (defn load-config [start-args config-suffix]
   ( let [ config-vars (read-project-file config-suffix)       ]
														(if (= 0 (count start-args))
																			(throw (Exception. " NO DB-CLIENT first run arg"))

														     ( if (= 1 (count start-args))
														         ( let [config-single-vars (db-1-arg-config start-args) 
														                db-keys    (get config-vars config-single-vars)
                              db-single-config    (get-real-envs db-keys  ) ]
																							 db-single-config )
														         
                        ( let [config-double-vars (db-2-args-config start-args) 
																															db-double-config (get config-vars config-double-vars) ]
																												db-double-config )))))
 
(defn load-constants [file-name]
 (let [ config-consts (edn-read/load-file file-name)]
   config-consts))


