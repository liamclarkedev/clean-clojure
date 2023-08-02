(defproject clean-clojure "1.0.0"
  :description "An example project used as a template for Clean Architecture."
  :main clean-clojure.core/-main
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [environ "1.2.0"]

                 ; Dependency
                 [com.stuartsierra/component "1.1.0"]

                 ; Web
                 [ring/ring-core "1.10.0"]
                 [ring/ring-defaults "0.3.4"]
                 [ring/ring-jetty-adapter "1.8.2"]
                 [metosin/compojure-api "2.0.0-alpha31"]
                 [compojure "1.7.0"]
                 [clj-http "3.12.3"]

                 ; Data
                 [oksql "1.3.2"]

                 ; Entity
                 [clj-time "0.15.2"]

                 ; O11y
                 [org.clojure/tools.logging "1.2.4"]]
  :plugins [[lein-environ "1.2.0"]]
  :profiles {:dev {:env          {:database-url "jdbc:postgresql://localhost:5432/clean-clojure?user=postgres&password=secret"}
                   :dependencies [[orchestra "2021.01.01-1"]
                                  [org.clojure/test.check "0.9.0"]]
                   :plugins      [[lein-cloverage "1.2.2"]]}}
  :jvm-opts     ["-Djava.net.preferIPv4Stack=true"])