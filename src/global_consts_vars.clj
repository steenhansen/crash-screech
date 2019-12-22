





(ns global-consts-vars
  (:gen-class))

(def ^:const FAKE-TEST-DATE    "31-23:59")
(def ^:const FAKE-SCRAPE-BYTES 1000009)
(def ^:const FAKE-SCRAPE-SPEED 800007)
(def ^:const FAKE-FAIL-CONTENT "failing html content")

(def ^:const PRODUCTION-COLLECTION "_prod_collection_")
(def ^:const T-TEST-COLLECTION "_test_collection_")

(def ^:const HTML-FAIL-COUNT 123456789)
(def ^:const HTML-OK-COUNT 1)

(def ^:const USE_MONGER_DB "monger-db")
(def ^:const USE_AMAZONICA_DB "amazonica-db")

(def ^:const LOCAL_CONFIG "./local-config.edn")
(def ^:const HEROKU_CONFIG "../heroku-config.edn")

(def ^:const START_CRON "start-cron")

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
(def ^:const SMS-TEST-CALL "Test sms call")

(def ^:const WWW-SFF-SEARCH "sffaudio-search.herokuapp.com_?author=")
(def ^:const SFF-SEARCH-CHECK-KEYS  [:div])
(def ^:const SFF-SEARCH-AMOUNT 6400)

(def ^:const WWW-SFFAUDIO-COM "www.sffaudio.com_?")
(def ^:const SFFAUDIO-CHECK-KEYS  [:article :div.blog-item-wrap])
(def ^:const SFFAUDIO-AMOUNT  6)
(def ^:const WWW-SFF-RSD "sffaudio.herokuapp.com_rsd_rss_?")
(def ^:const SFF-RSD-CHECK-KEYS  [:item])
(def ^:const SFF-RSD-AMOUNT  100)

(def ^:const WWW-SFF-PODCAST "sffaudio.herokuapp.com_podcast_rss_?")
(def ^:const SFF-PODCAST-CHECK-KEYS  [:item])
(def ^:const SFF-PODCAST-AMOUNT 500)
(def ^:const WWW-SFF-PDF "sffaudio.herokuapp.com_pdf_rss_?")
(def ^:const SFF-PDF-CHECK-KEYS  [:item])
(def ^:const SFF-PDF-AMOUNT 6000)

(def ^:const WWW-SFF-MEDIA "sffaudio-graph-ql.herokuapp.com/media-radio-lists?")
(def ^:const SFF-MEDIA-CHECK-KEYS  [:input])
(def ^:const SFF-MEDIA-AMOUNT  5)

(def ^:const WWW-CANADIAN-QUOTATIONS "www.canadianquotations.ca_?")
(def ^:const CANADIAN-QUOTATIONS-CHECK-KEYS  [:article.post])
(def ^:const CANADIAN-QUOTATIONS-AMOUNT  21)

(def ^:const WWW-JERKER-SEARCHER "www.jerkersearcher.com_?")
(def ^:const JERKER-SEARCHER-CHECK-KEYS  [:article.post :h2.entry-title])
(def ^:const JERKER-SEARCHER-AMOUNT 10)       ;   testing failure on real site 10)

(defn make-check-pages [fail-extra]
  (let [SFF-SEARCH {:check-page WWW-SFF-SEARCH
                    :enlive-keys SFF-SEARCH-CHECK-KEYS
                    :at-least (+ fail-extra SFF-SEARCH-AMOUNT)}
        SFF-AUDIO {:check-page WWW-SFFAUDIO-COM
                   :enlive-keys SFFAUDIO-CHECK-KEYS
                   :at-least (+ fail-extra SFFAUDIO-AMOUNT)}
        SFF-RSD {:check-page WWW-SFF-RSD
                 :enlive-keys  SFF-RSD-CHECK-KEYS,
                 :at-least (+ fail-extra SFF-RSD-AMOUNT)}
        SFF-PODCAST {:check-page WWW-SFF-PODCAST
                     :enlive-keys SFF-PODCAST-CHECK-KEYS
                     :at-least (+ fail-extra SFF-PODCAST-AMOUNT)}
        SFF-PDF {:check-page WWW-SFF-PDF
                 :enlive-keys SFF-PDF-CHECK-KEYS
                 :at-least (+ fail-extra SFF-PDF-AMOUNT)}
        SFF-MEDIA {:check-page WWW-SFF-MEDIA
                   :enlive-keys SFF-MEDIA-CHECK-KEYS
                   :at-least (+ fail-extra SFF-MEDIA-AMOUNT)}
        CANADIAN-QUOTATIONS {:check-page WWW-CANADIAN-QUOTATIONS
                             :enlive-keys CANADIAN-QUOTATIONS-CHECK-KEYS
                             :at-least (+ fail-extra CANADIAN-QUOTATIONS-AMOUNT)}
        JERKER-SEARCHER {:check-page WWW-JERKER-SEARCHER
                         :enlive-keys JERKER-SEARCHER-CHECK-KEYS,
                         :at-least (+ fail-extra JERKER-SEARCHER-AMOUNT)}
        the-check-pages [SFF-AUDIO
                         SFF-RSD
                         SFF-PODCAST
                         SFF-PDF
                         SFF-SEARCH
                         CANADIAN-QUOTATIONS
                         JERKER-SEARCHER
                         SFF-MEDIA]]
    the-check-pages))

(def ^:const TEST-CONFIG-FILE "./local-config.edn")
(def ^:const SMS-NO-ERROR [])
