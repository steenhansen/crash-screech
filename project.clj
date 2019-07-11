(defproject sffaudio/web-server "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0",
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies
    [[org.clojure/clojure "1.10.0"]
     [amazonica "0.3.142"]
     [chrisjd/edn-config "0.1.1"]
     [com.novemberain/monger "3.1.0"]
     [overtone/at-at "1.2.0"]
     [clojure.java-time "0.3.2"]
     [clj-http "3.10.0"]
[cheshire "5.8.1"]
     [me.raynes/fs "1.4.6"]
     [enlive "1.1.6"]
     [ring "1.6.3"]
     [log4j/log4j
      "1.2.16"
      :exclusions
      [javax.mail/mail javax.jms/jms com.sun.jdmk/jmxtools com.sun.jmx/jmxri]]
     [org.slf4j/slf4j-log4j12 "1.6.4"]
     [org.clojure/tools.logging "0.2.3"]
     [clj-logging-config "1.9.7"]]
  :plugins [[lein-cljfmt "0.6.4"]]
  :uberjar-name "main-sff-audio-standalone.jar"
  :min-lein-version "2.0.0"
  :main ^:skip-aot main-sff-audio
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
