(ns clojwar.web.routes
  (:require [compojure.core :refer [defroutes GET POST ANY]]
            [compojure.route :refer [not-found resources]]
            [clojwar.web.controllers.main :as main]
            [clojwar.web.controllers.game :as game]))

(defroutes clojwar-web
  "This function is responsible to handle with WEB Routes of clojwar application"
  ;; MAIN ROUTE
  (GET "/" req (main/main-redirect req))

  ;; GAME ROUTE
  (GET "/game" req (game/get-main-game-handler req))
  (GET "/game/:game-id" [game-id] (game/get-game-handler game-id))

  ;; STATIC RESOURCES ROUTE
  (resources "/")

  ;; NOT FOUND ROUTES
  (ANY "/*" [] (main/not-found-main-handler)))