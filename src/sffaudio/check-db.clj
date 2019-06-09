

(ns sffaudio.web-stat
	 (:require [amazonica.aws.dynamodbv2 :as aws-dyn])  
  (:require [clojure.string :as clj-str])         
  (:require [edn-config.core :as edn-read])       
  (:require [java-time :as jav-time])                  
   (:require [monger.core :as mong-core])           
   (:require [monger.collection :as mong-coll])   
   (:require [monger.operators :refer :all])
)

(load "config-args") 
(load "amazonica-db")  
(load "monger-db") 

(defn get-db-conn [db-conn-params]
  (let [{db-type :db-type db-location :db-location the-config :the-config 
 	       table-name :table-name pages-to-check :pages-to-check } db-conn-params] 
    (try 
 					(case db-type :amazonica (dynamo-build the-config  table-name pages-to-check) 
                       :monger (mongolabs-build the-config  table-name pages-to-check))
      (catch Exception e (println db-location " -- caught exception: " (.getMessage e))))))

(defn build-db [build-db-params config-file]
    (let [{start-args :start-args table-name :table-name pages-to-check :pages-to-check } build-db-params
           the-config (load-config start-args config-file)
           db-type (db-1-arg-config start-args)
           db-location (db-2-args-config start-args)
           db-conn-params { :db-type db-type :db-location db-location :the-config the-config 
 	                          :table-name table-name :pages-to-check pages-to-check }
           my-db-funcs (get-db-conn db-conn-params)]
      { :delete-table (get my-db-funcs :delete-table) 
             :get-all (get my-db-funcs :get-all)     
             :get-url (get my-db-funcs :get-url)
            :put-item (get my-db-funcs :put-item)          
           :put-items (get my-db-funcs :put-items)}))

(defn db-handle [table-name pages-to-check start-args config-file]
  (let [ build-db-params { :start-args start-args :table-name table-name :pages-to-check pages-to-check}
         my-db-obj (build-db build-db-params config-file)]
    my-db-obj ))
