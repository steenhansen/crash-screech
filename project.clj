
(defproject sffaudio/web-server "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0",
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies   [[org.clojure/clojure "1.10.0"]

;; july 8/stat - Copy (10) was first log4j thing
[lambdaisland/deep-diff "0.0-47"]

                   [log4j/log4j "1.2.16" :exclusions [javax.mail/mail javax.jms/jms com.sun.jdmk/jmxtools com.sun.jmx/jmxri]]

                   [amazonica "0.3.142"]
                   [chrisjd/edn-config "0.1.1"]
                   [com.novemberain/monger "3.1.0"]
                   [overtone/at-at "1.2.0"]
                   [clojure.java-time "0.3.2"]
                   [clj-http "3.10.0"]
                   [cheshire "5.8.1"]
                   [me.raynes/fs "1.4.6"]

[cljc.java-time "0.1.5"]
[clojure.java-time "0.3.2"]
[expound "0.7.2"]

                   [enlive "1.1.6"]
                   [ring "1.6.3"]
                   [org.slf4j/slf4j-log4j12 "1.6.4"]
                   [org.clojure/tools.logging "0.2.3"]
                   [clj-logging-config "1.9.7"]
                   [io.aviso/pretty "0.1.37"]]


 ; :ring {:handler start-heroku/main}


  :plugins [[lein-cljfmt "0.6.4"]
            [lein-cloverage "1.0.2"]
            [jonase/eastwood "0.3.5"]]
  :injections [(require 'io.aviso.repl
                        'clojure.repl
                        'clojure.main)
               (alter-var-root #'clojure.main/repl-caught
                               (constantly @#'io.aviso.repl/pretty-pst))
               (alter-var-root #'clojure.repl/pst                    ; nicer (pst) 
                               (constantly @#'io.aviso.repl/pretty-pst))]     ; (print stack trace) 
  :uberjar-name "crash-screech.jar"
  :min-lein-version "2.0.0"
  :main ^:skip-aot start-heroku            ; the main() that heroku calls
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :dev {:dependencies [[org.clojure/test.check "0.9.0"]]}})
