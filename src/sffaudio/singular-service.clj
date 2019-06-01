

(defn- pretty-demunge
  [fn-object]
  (let [dem-fn (clojure.repl/demunge (str fn-object))
        pretty (second (re-find #"(.*?\/.*?)[@].*" dem-fn))]
    (if pretty pretty dem-fn)))

(defonce Xcron--job--queue (atom {}))

(defn add-cron-job-to-queue2 [new-cron-ref cron-job kill-service-fn]
   ( let [ old-job-queue @Xcron--job--queue
           cron-name (pretty-demunge cron-job)
           cron-key (keyword cron-name)
           cron-array-inited (contains? old-job-queue cron-key)
           ]
             (let [new-used  (assoc-in old-job-queue [cron-key] new-cron-ref)]
																					(reset! Xcron--job--queue new-used)   )  ))

(defn off-service [service-name kill-service-fn]
  ( let [ old-job-queue @Xcron--job--queue
           the-name (pretty-demunge service-name)
           service-key (keyword the-name)
           service-array-inited (contains? old-job-queue service-key)
           ]
             (if service-array-inited
                     (let [ old-service (service-key old-job-queue)]
                        (kill-service-fn old-service )
                       )   )
             (let [new-used  (dissoc  old-job-queue service-key)]
																					(reset! Xcron--job--queue new-used)  )   ))
