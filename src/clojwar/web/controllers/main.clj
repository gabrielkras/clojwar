(ns clojwar.web.controllers.main
  (:require [selmer.parser :as tmpl]
            [clojure.java.io :as io]
            [ring.util.response :as ring]))

;; DEFAULT TEMPLATE DIR ;;
(tmpl/set-resource-path! (io/resource "pages"))

(defn main-redirect
  "This function redirects the request to game route"
  [request]
  (ring/redirect "/game"))

(defn not-found-main-handler
  "This function handles with the main not-found route of web"
  []
  (tmpl/render-file "404.html" {}))