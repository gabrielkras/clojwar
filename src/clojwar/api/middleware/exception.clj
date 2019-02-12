(ns clojwar.api.middleware.exception
  (:require [clojwar.api.middleware.responses :as res]))

(defn wrap-exception
  "This function handles with API exceptions"
  [handler]
  (fn [request]
    (try (handler request)
         (catch Exception e 
           (let [exception-map (Throwable->map e)
                 cause (:cause exception-map)]
           (res/wrap-500-response {:exception {:cause cause}})))
         (catch AssertionError e
           (res/wrap-500-response {:assertion-error (str e)})))))
