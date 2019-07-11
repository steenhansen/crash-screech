

(def ^:const DB-TABLE-NAME "fff2")

(def ^:const SCRAPED-TEST-DATA "./test/scraped-data/")

(def ^:const EMPTY-HTML "-")
(def ^:const DATE-MAX-LENGTH 19)

(def ^:const ERROR-KEEP-LENGTH 200)
(def ^:const IGNORE-ENV-VARS "ignore-environment"); don't look at Windows environment variables, just in case
(def ^:const CRON-MILL-SECS 600000); 1000 =sec      10000 =10sec         60000 =min   600000 = 10 min


(def ^:const MAX-R-W-RECORDS 50)
(def ^:const BASE-HTML-TEMPLATE "base-template.html")

(def ^:const SMS-FOUND-ERROR "Found an error")
;(def ^:const SMS-NO-ERROR [])
(def ^:const SMS-NEW-MONTH "Start of a new SFFaudio month!")

(def ^:const THE-CHECK-PAGES
  [{:check-page "www.sffaudio.com",
    :enlive-keys [:article :div.blog-item-wrap],
    :at-least 6} ;6
   ;{:check-page "www.jerkersearcher.com",
   ; :enlive-keys [:article.post :h2.entry-title],
   ; :at-least 10}
   ;  {:check-page
   ;  "sffaudio-graph-ql.herokuapp.com/media-radio-lists"
   ;  :enlive-keys[:input]  :at-least 5}
   {:check-page "sffaudio.herokuapp.com_rsd_rss",
    :enlive-keys [:item],
    :at-least 1} ;  {:check-page "sffaudio.herokuapp.com_podcast_rss"
                 ;  :enlive-keys[:item]
   ;                     :at-least 525}
   ; {:check-page "sffaudio.herokuapp.com_pdf_rss"     :enlive-keys[:item]
   ;                   :at-least 6100}
   ;   {:check-page "sffaudio-search.herokuapp.com"
   ;   :enlive-keys[:div.book__choice]             :at-least 6400}
   ])
