

(def ^:const DB-TABLE-NAME "eee")


(def ^:const ERROR-KEEP-LENGTH 200)

(def ^:const IGNORE-ENV-VARS "ignore-environment") ; don't look at Windows environment variables, just in case

(def ^:const CRON-MILL-SECS 600000)          ; 1000 =sec      10000 =10sec         60000 =min   600000 = 10 min
(def ^:const CRON-RUN-CONTINUOUS true)       
(def ^:const CRON-SAVE-TO-DB true)      
(def ^:const CRON-DO-SCRAPE true)   


(def ^:const 	TEMPORIZE-CRON-CALL "/temporize-call")  

(def ^:const MAX-R-W-RECORDS 50)



(def ^:const BASE-HTML-TEMPLATE "base-html-template.html")   

(def ^:const THE-CHECK-PAGES   [
    {:check-page "www.sffaudio.com"               :enlive-keys[:article :div.blog-item-wrap]  :at-least 5}    
  	 {:check-page "sffaudio.herokuapp.com_rsd_rss" :enlive-keys[:item]                         :at-least 5}	
  ])