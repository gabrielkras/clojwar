(ns clojwar.web.controllers.game
  (:require [selmer.parser :as tmpl]
            [clojure.java.io :as io]
            [clojwar.game :as play]
            [clojwar.configuration :as config]))

;; DEFAULT TEMPLATE DIR ;;
(tmpl/set-resource-path! (io/resource "pages"))

(defn get-main-game-handler
  "This function handles with the main route of web"
  [request]
  (tmpl/render-file "main.html" {}))

(defn get-game-handler
  "This function handles with the game route of web"
  [game-id]
  (let [grid (play/get-grid (str game-id))]
    (if-not (empty? grid)
      (tmpl/render-file "game.html" {:game grid :size {:x-range (-> config/param :size :x range)
                                                       :y-range (-> config/param :size :y range reverse)}})
      (tmpl/render-file "404.html" {}))))