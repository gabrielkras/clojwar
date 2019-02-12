(ns clojwar.configuration
  "This is a configuration namespace of project. Here 
you can change the application behavior"
  (:require [taoensso.timbre :as timbre]
            [taoensso.timbre.appenders.core :as appenders]))

;; Config map
(def param {:size {:x 50 :y 50}                          ; The size of the grid, represented by X (Horizontal) and Y (Vertical)
            :log-file "logs/prod.log"                    ; The application log file
            :log-locale "pt_BR"                          ; The log file locate
            :log-date-format "dd-MM-yyyy HH:mm:ss ZZ"    ; The log file format
            :server-port 8080})                          ; The clojwar server port application

(defn apply-configuration! []
  "This function applies the log configuration of timbre plugin"
;; LOG OPTIONS
  (timbre/merge-config!
   {:appenders {:spit (appenders/spit-appender {:fname (:log-file param)})}
    :timestamp-opts {:pattern (:log-date-format param)
                     :locale (java.util.Locale. (:log-locale param))}}))
              
