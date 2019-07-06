(defproject sffaudio/web-server "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [ 
                  [org.clojure/clojure "1.10.0"]
                  [amazonica "0.3.142"] 
                  [chrisjd/edn-config "0.1.1"]
                  [com.novemberain/monger "3.1.0"] 
                  [overtone/at-at "1.2.0"]
                  [clojure.java-time "0.3.2"]
                  [clj-http "3.10.0"]
																		[me.raynes/fs "1.4.6"]
                  [enlive "1.1.6"]
                  [ring "1.6.3"]

                  [log4j/log4j "1.2.16" :exclusions [javax.mail/mail javax.jms/jms com.sun.jdmk/jmxtools com.sun.jmx/jmxri]]
                  [org.slf4j/slf4j-log4j12 "1.6.4"]
                  [org.clojure/tools.logging "0.2.3"]                
                  [clj-logging-config "1.9.7"]
                  
                ]

  :ring {:handler sff-audio.web-server/main}
  :plugins [
             [lein-heroku "0.5.3"]
             [lein-zprint "0.3.16"] 
             ]
  :heroku {
    :app-name "your-heroku-app-name"
    :jdk-version "1.8"
    :include-files ["target/uberjar/sff-audio-web-server-standalone.jar"]
    :process-types { "web" "java -jar target/uberjar/sff-audio-web-server-standalone.jar" }
  }
  ;:zprint {:old? false}
  :uberjar-name "sff-audio-web-server-standalone.jar"
  :min-lein-version "2.0.0"
  :main ^:skip-aot sff-audio.web-server
  :target-path "target/%s"
  :profiles {
           ;  :dev {:dependencies [[org.clojure/test.check "0.9.0"]]}
             :uberjar {:aot :all}})

