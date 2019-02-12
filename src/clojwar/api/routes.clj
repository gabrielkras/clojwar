(ns clojwar.api.routes
  (:require [compojure.core :refer [defroutes GET POST PUT DELETE ANY]]
            [compojure.route :refer [not-found]]
            [clojwar.api.middleware.responses :as res]
            [clojwar.api.controllers.robot :as rbt]
            [clojwar.api.controllers.grid :as grd]
            [clojwar.api.controllers.dino :as din]))

(defroutes clojwar-api
  "This function is responsible to handle with REST API Routes of clojwar Application."
  ;; GRID
  (GET "/api/grid" req (grd/get-grid-handler req))
  (POST "/api/grid" req (grd/post-grid-handler (:body req)))
  (GET "/api/grid/:grid-id" [grid-id] (grd/get-grid-by-id-handler grid-id))
  (PUT "/api/grid/:grid-id" req (grd/put-grid-handler (:body req)
                                                      (-> req :params :grid-id)))

  ;; DINOSAUR 
  (GET "/api/grid/:grid-id/dino" [grid-id] (din/get-dino-handler grid-id))
  (POST "/api/grid/:grid-id/dino" req (din/post-dino-handler (:body req)
                                                             (-> req :params :grid-id)))
  (DELETE "/api/grid/:grid-id/dino" req (din/delete-dino-handler (:body req)
                                                                 (-> req :params :grid-id)))

  ;; ROBOT
  (GET "/api/grid/:grid-id/robot" [grid-id] (rbt/get-robot-handler grid-id))
  (POST "/api/grid/:grid-id/robot" req (rbt/post-robot-handler (:body req)
                                                               (-> req :params :grid-id)))
  (DELETE "/api/grid/:grid-id/robot" [grid-id] (rbt/delete-robot-handler grid-id))
  (PUT "/api/grid/:grid-id/robot/actions" req (rbt/put-robot-handler (:body req)
                                                                     (-> req :params :grid-id)))
  
   ;; NOT FOUND ROUTES
  (ANY "/api/*" [] (res/wrap-404-response {} "Route not found!")))