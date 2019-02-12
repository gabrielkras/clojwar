(defproject clojwar "1.0.0"
  :description "ClojWar - The Great Battle - API and Web Application"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [ring "1.7.1"]
                 [ring/ring-json "0.4.0"]
                 [ring-logger "1.0.1"]
                 [com.taoensso/timbre "4.10.0"]
                 [org.clojure/data.json "0.2.6"]
                 [compojure "1.6.1"]
                 [selmer "1.12.5"]]
  :main clojwar.server
  :source-paths ["config", "src"])
