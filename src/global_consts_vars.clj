

; global testing vars      
;  *service-kill-list*
;  *we-be-testing*
;; (defonce ^:dynamic *sms-was-executed* (atom {}))





(ns global-consts-vars
  (:gen-class))

(defonce ^:dynamic *we-be-testing* (atom {}))
(defonce ^:dynamic *sms-was-executed* (atom {}))          ;;; need some testing flavor, *test-sms-was-executed
(defonce ^:dynamic *pages-were-scraped* (atom {}))
(defonce ^:dynamic *test-use-test-time* (atom {}))  ;;; ALSO SET TO FALSE 




(def ^:const FAKE-TEST-TIME    98765432)
(def ^:const FAKE-SCRAPE-BYTES 23456789)






(def ^:const MONGER_DB "monger-db")
(def ^:const AMAZONICA_DB "amazonica-db")

(def ^:const START_CRON "start-cron")

(def ^:const DB-TABLE-NAME "iii8")
(def ^:const SCRAPED-TEST-DATA "./test/scraped-data/")

(def ^:const EMPTY-HTML "-")
(def ^:const DATE-MAX-LENGTH 19)
(def ^:const ERROR-KEEP-LENGTH 200)

(def ^:const USE_ENVIRONMENT "use-environment")
(def ^:const IGNORE-ENV-VARS "ignore-environment"); don't look at Windows environment variables, just in case
(def ^:const CRON-MILL-SECS 1000); 1000 =sec      10000 =10sec         60000 =min   600000 = 10 min


(def ^:const LARGE-RECORD-COUNT 1000)
(def ^:const START-REGEX-LITERAL "\\Q")
(def ^:const END-REGEX-LITERAL "\\E")

(def ^:const MAX-R-W-RECORDS 50)
(def ^:const BASE-HTML-TEMPLATE "base-template.html")

(def ^:const SMS-FOUND-ERROR "Found an error")
(def ^:const SMS-NEW-MONTH "Start of a new SFFaudio month!")

(def ^:const SFF-AUDIO
  {:check-page "www.sffaudio.com",
   :enlive-keys [:article :div.blog-item-wrap],
   :at-least 6})

(def ^:const SFF-RSD
  {:check-page "sffaudio.herokuapp.com_rsd_rss"  
 :enlive-keys [:item],
   :at-least 1})

(def ^:const SFF-PODCAST
  {:check-page "sffaudio.herokuapp.com_podcast_rss"
   :enlive-keys [:item]
   :at-least 525})

(def ^:const SFF-PDF
  {:check-page "sffaudio.herokuapp.com_pdf_rss"
     :enlive-keys [:item]
   :at-least 6100})

(def ^:const SFF-MEDIA
  {:check-page
   "sffaudio-graph-ql.herokuapp.com/media-radio-lists"
   :enlive-keys [:input] 
   :at-least 5})

(def ^:const SFF-SEARCH
  {:check-page "sffaudio-search.herokuapp.com"
   :enlive-keys [:div]           
  :at-least 6400})

(def ^:const CANADIAN-QUOTATIONS
  {:check-page "www.canadianquotations.ca",
   :enlive-keys [:article.post],  
 :at-least 21})

(def ^:const JERKER-SEARCHER
  {:check-page "www.jerkersearcher.com",
   :enlive-keys [:article.post :h2.entry-title],
   :at-least 10})

(def ^:const THE-CHECK-PAGES
  [SFF-AUDIO
   SFF-RSD
   SFF-PODCAST
   SFF-PDF
   SFF-SEARCH
   CANADIAN-QUOTATIONS
   JERKER-SEARCHER
   SFF-MEDIA])

(def ^:const HEROKU_CONFIG "../heroku-config.edn")

(def ^:const TEST-CONFIG-FILE "./local-config.edn")
(def ^:const SMS-NO-ERROR [])





