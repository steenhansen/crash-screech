

(def ^:const DB-TABLE-NAME "eee7")

(def ^:const ERROR-KEEP-LENGTH 200)
(def ^:const IGNORE-ENV-VARS "ignore-environment") ; don't look at Windows environment variables, just in case
(def ^:const CRON-MILL-SECS 600000)          ; 1000 =sec      10000 =10sec         60000 =min   600000 = 10 min
(def ^:const CRON-RUN-CONTINUOUS true)       
(def ^:const CRON-SAVE-TO-DB true)      
(def ^:const CRON-DO-SCRAPE true)   
(def ^:const 	TEMPORIZE-CRON-CALL "/temporize-call")  
(def ^:const MAX-R-W-RECORDS 50)
(def ^:const BASE-HTML-TEMPLATE "base-html-template.html")   
(def ^:const BETWEEN-URL-WAIT 5000)
(def ^:const GRAPHQL-CLARKE-URL "sffaudio-graph-ql.herokuapp.com/graphql?operationName=serch_ql&query=%0Aquery%20serch_ql(%24search_parameter%3A%20String!)%20%7B%0A%20search_site_content(search_text%3A%20%24search_parameter)%20%7B%0A%20...%20on%20ArticlePage%7B%20ID%20headline%20article_post%20%7D%2C%0A%20...%20on%20MentionPage%7B%20ID%20headline%20mention_post%20%7D%2C%0A%20...%20on%20RsdMedia%20%7B%20ID%20rsd_post%20resource%0A%20book%20%7B%20author%20title%20%7D%0A%20podcast%20%7B%20description%20mp3%20length%20episode%20%7D%20%7D%2C%0A%20...%20on%20SffAudioMedia%20%7B%20ID%20sffaudio_post%20narrator%20about%0A%20possiblebook%7B%20author%20title%20%7D%0A%20podcast%20%7B%20description%20mp3%20length%20episode%20%7D%20%7D%2C%0A%20...%20on%20PdfMedia%20%7B%20ID%0A%20book%20%7B%20author%20title%20%7D%0A%20issues%20%7B%20url%20publisher%20pages%20%7D%20%7D%0A%20%7D%0A%7D%20&variables=%7B%20%22search_parameter%22%3A%20%22clarke%22%7D")

(def ^:const THE-CHECK-PAGES   [
      {:check-page "www.sffaudio.com"                   :enlive-keys[:article :div.blog-item-wrap]  :at-least 5}    
      {:check-page "www.jerkersearcher.com"             :enlive-keys[:article.post :h2.entry-title] :at-least 9}    
      {:check-page "sffaudio.herokuapp.com_rsd_rss"     :enlive-keys[:item]                         :at-least 175}	
      {:check-page "sffaudio.herokuapp.com_podcast_rss" :enlive-keys[:item]                         :at-least 525}	
      {:check-page "sffaudio.herokuapp.com_pdf_rss"     :enlive-keys[:item]                         :at-least 6100}	
      {:check-page "sffaudio-search.herokuapp.com"      :enlive-keys[:div.book__choice]             :at-least 6400}    
    
;        {:check-page GRAPHQL-CLARKE-URL  :enlive-keys[:id]              :at-least 90}    
    
      ])
