(ns clojwar.server
  (:require [clojwar.configuration :as config]
            [clojwar.api.routes :as api]
            [clojwar.api.middleware.exception :as exc]
            [clojwar.web.routes :as web]
            [compojure.core :refer [routes]]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.json :refer [wrap-json-body]]
            [ring.logger :as logger]
            [taoensso.timbre :as timbre :refer [log]]))

;; INITIALIZING CONFIGURATION
(config/apply-configuration!)

(def clojwar-app (routes (-> api/clojwar-api
                           (wrap-json-body  {:keywords? true :bigdecimals? true})
                           exc/wrap-exception
                           (logger/wrap-with-logger {:log-fn (fn [{:keys [level throwable message]}]
                                                              (timbre/log level throwable message))}))
                       (-> web/clojwar-web)))

;; MAIN TEST FUNCTION
(defn -main []
  (jetty/run-jetty clojwar-app {:port (:server-port config/param)}))