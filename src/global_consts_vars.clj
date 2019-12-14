

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



;(def ^:const T-DB-TEST-NAME "zzz3")
(def ^:const HTML-FAIL-COUNT 123456789)
(def ^:const HTML-OK-COUNT 1)


(def ^:const USE_MONGER_DB "monger-db")
(def ^:const USE_AMAZONICA_DB "amazonica-db")

(def ^:const LOCAL_CONFIG "./local-config.edn")
(def ^:const HEROKU_CONFIG "../heroku-config.edn")



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
(def ^:const SMS-NEW-MONTH "Start of a new month test!")

(def ^:const WWW-SFFAUDIO-COM "www.sffaudio.com_?")
(def ^:const SFFAUDIO-CHECK-KEYS  [:article :div.blog-item-wrap] )


(def ^:const WWW-SFF-SEARCH "sffaudio-search.herokuapp.com_?author=" )
(def ^:const SFF-SEARCH-CHECK-KEYS  [:div] )

(def ^:const SFF-SEARCH
  {:check-page WWW-SFF-SEARCH
   :enlive-keys SFF-SEARCH-CHECK-KEYS
   :at-least 6400})

(def ^:const SFF-AUDIO
  {:check-page WWW-SFFAUDIO-COM
   :enlive-keys SFFAUDIO-CHECK-KEYS
   :at-least 6})

(def ^:const WWW-SFF-RSD "sffaudio-search.herokuapp.com_table_rss_?" )
(def ^:const SFF-RSD-CHECK-KEYS  [:item] )

(def ^:const SFF-RSD
  {:check-page WWW-SFF-RSD
  :enlive-keys SFF-RSD-CHECK-KEYS,
   :at-least 1})


(def ^:const WWW-SFF-PODCAST "sffaudio-search.herokuapp.com_podcast_table_?" )
(def ^:const SFF-PODCAST-CHECK-KEYS  [:item] )

(def ^:const SFF-PODCAST
  {:check-page WWW-SFF-PODCAST
   :enlive-keys SFF-PODCAST-CHECK-KEYS
   :at-least 525})


(def ^:const WWW-SFF-PDF "sffaudio-search.herokuapp.com_pdf_table_?" )
(def ^:const SFF-PDF-CHECK-KEYS  [:item] )

(def ^:const SFF-PDF
  {:check-page WWW-SFF-PDF
     :enlive-keys SFF-PDF-CHECK-KEYS
   :at-least 6100})


(def ^:const WWW-SFF-MEDIA "sffaudio-graph-ql.herokuapp.com/media-radio-lists?" )
(def ^:const SFF-MEDIA-CHECK-KEYS  [:input] )

(def ^:const SFF-MEDIA
  {:check-page WWW-SFF-MEDIA
   :enlive-keys SFF-MEDIA-CHECK-KEYS
   :at-least 5})

(def ^:const WWW-CANADIAN-QUOTATIONS "www.canadianquotations.ca_?" )
(def ^:const CANADIAN-QUOTATIONS-CHECK-KEYS  [:article.post] )


(def ^:const CANADIAN-QUOTATIONS
  {:check-page WWW-CANADIAN-QUOTATIONS
   :enlive-keys CANADIAN-QUOTATIONS-CHECK-KEYS
 :at-least 21})


(def ^:const WWW-JERKER-SEARCHER "www.jerkersearcher.com_?" )
(def ^:const JERKER-SEARCHER-CHECK-KEYS  [:article.post :h2.entry-title] )

(def ^:const JERKER-SEARCHER
  {:check-page WWW-JERKER-SEARCHER
   :enlive-keys JERKER-SEARCHER-CHECK-KEYS,
   :at-least 10})

(def ^:const THE-CHECK-PAGES
  [
SFF-AUDIO
   SFF-RSD
   SFF-PODCAST
   SFF-PDF
   SFF-SEARCH
   CANADIAN-QUOTATIONS
   JERKER-SEARCHER
   SFF-MEDIA
   ])


(def ^:const TEST-CONFIG-FILE "./local-config.edn")
(def ^:const SMS-NO-ERROR [])
