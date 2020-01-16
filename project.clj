
(defproject sffaudio/crash-sms "0.1.0-SNAPSHOT"
  :description "Website checker which notifies errors via SMS"
  :url "https://fathomless-woodland-85635.herokuapp.com/"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [
	[org.clojure/clojure "1.10.0"]
        [amazonica "0.3.142"]
        [chrisjd/edn-config "0.1.1"]
        [com.novemberain/monger "3.5.0"]
        [clojure.java-time "0.3.2"]
        [clj-http "3.10.0"]
        [me.raynes/fs "1.4.6"]
        [enlive "1.1.6"]
        [ring "1.6.3"]
	[org.slf4j/slf4j-log4j12 "1.6.4"]
	[clj-logging-config "1.9.7"]
  ]
  :plugins [
	[lein-cljfmt "0.6.4"]
        [lein-cloverage "1.0.2"]
        [jonase/eastwood "0.3.5"]
  ]
  :uberjar-name "crash-sms.jar"
  :min-lein-version "2.0.0"
  :main ^:skip-aot start-heroku            ; the main() that heroku calls
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
	     :dev {:dependencies [
                        [org.clojure/test.check "0.9.0"]
		  ]}
	     }
)
