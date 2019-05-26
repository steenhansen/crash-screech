 
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

( defn the-project-name [config-suffix]
  ( str "../"
    ( -> (aquire-project-file) 
         (clj-str/split   #"\.")
        (last)
        )
    config-suffix ) )           

 (defn load-config [start-args config-suffix]
   ( let [ config-vars (edn-read/load-file (the-project-name config-suffix))       ]
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


