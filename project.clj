(defproject sffaudio/web-stat "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [ [org.clojure/clojure "1.10.0"]
                  [amazonica "0.3.142"] 
                  [chrisjd/edn-config "0.1.1"]
                  [com.novemberain/monger "3.1.0"]
                  [overtone/at-at "1.2.0"]
                  [org.clojure/java.data "0.1.1"]     
                  [clojure.java-time "0.3.2"]
                  [clj-http "3.10.0"]
                  [compojure "1.6.1"]


[me.raynes/fs "1.4.6"]


[environ "1.1.0"]


                   ;[ring/ring-jetty-adapter "1.4.0"]
                 









                  [enlive "1.1.6"]

                  [ring "1.6.3"]
     [net.cgrand/moustache "1.2.0-alpha2"]

                   [com.draines/postal "2.0.3"]
                                        ]

 :ring {:handler sff-audio.web-stat/handler}


:plugins [ [lein-beanstalk "0.2.7"]
           [lein-ring "0.12.5"]
[lein-tar "3.2.0"]
           [lein-heroku "0.5.3"] ]

:heroku {
  :app-name "your-heroku-app-name"
  :jdk-version "1.8"
  :include-files ["target/uberjar/sff-audio-web-stat-standalone.jar"]
  :process-types { "web" "java -jar target/uberjar/sff-audio-web-stat-standalone.jar" }
 }


                                        :uberjar-name "sff-audio-web-stat-standalone.jar"
 :min-lein-version "2.0.0"

  :main ^:skip-aot sff-audio.web-stat
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})

