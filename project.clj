(defproject xmljs "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.4.0"]
                 [ring/ring-defaults "0.1.5"]
                 [http-kit "2.1.18"]
                 [javax.servlet/servlet-api "2.5"]
                 [org.clojure/data.zip "0.1.2"]
                 [org.clojure/tools.namespace "0.2.11"]
                 [org.clojure/data.xml "0.0.8"]
                 [org.clojure/data.json "0.2.6"]
                 [clj-http "2.1.0"]
                 [clj-http-fake "1.0.2"]
                 [digest "1.4.4"]
                 [crypto-random "1.2.0"]]

  :main xmljs.handler
  :resource-paths ["resources"]
  :plugins [[lein-ring "0.9.7"]]
  :ring {:handler xmljs.handler/app}
  :profiles { :uberjar {:aot :all}
              :dev {:dependencies
                    [[ring/ring-mock "0.3.0"]]}})
