(ns clojwar.api.controllers.grid
  (:require [clojwar.game :as play]
            [clojwar.api.middleware.responses :as res]
            [clojwar.api.middleware.validator :as vld]))

(defn get-grid-handler 
  "This function handles the GET method of grids route"
  [request]
  (let [grids (play/get-grid-all)]
    (if (empty? grids)
      (res/wrap-404-response grids "There are no registered grids")
      (res/wrap-200-response grids))))

(defn get-grid-by-id-handler
  "This function handles the GET method of grid-by-id route"
  [grid-id]
  (let [grids (play/get-grid grid-id)]
    (if (empty? grids)
      (res/wrap-404-response grids "There are no registered grids")
      (res/wrap-200-response grids))))

(defn post-grid-handler
  "This function handles the POST method of grids route"
  [body]
  (if (empty? (vld/grid-body-validator body))
    (res/wrap-400-response body "Invalid request body! Take a look on the docs for more details")
    (if (empty? (play/get-grid (:name body)))
      (do (play/create-grid! (:name body))
          (res/wrap-201-response (play/get-grid (:name body))
                                 "The grid has been created successfully!"))
      (res/wrap-409-response {:chosen-name (:name body)}
                             "The chosen name for the grid already exists, please choose another."))))

(defn put-grid-handler
  "This function handles the PUT method of grid, altering the current game status"
  [body grid-id]
  (let [grid (play/get-grid grid-id)]
    (if (empty? grid)
      (res/wrap-404-response {:grid-id grid-id} "There are no registered grid with this name")
      (if (or (empty? body) (empty? (vld/grid-body-actions-validator body grid-id)))
        (res/wrap-400-response {:body body} "Invalid request body! 
 Take a look on the docs for more details")
        (cond
          ;; PREPARE ACTION
          (= (:action body)
             "prepare") (let [grids (play/prepare-game! grid-id)
                              grd (play/get-grid grid-id)]
                          (if (empty? grids)
                            (res/wrap-409-response grd
                                                   "Sorry, but you need to add at least 
 one robot and one dinosaur to prepare the game.")
                            (res/wrap-201-response grd
                                                   "Congrats your game is ready to be started")))
          ;; START ACTION
          (= (:action body)
             "start") (let [grids (play/start-game! grid-id)
                            grd (play/get-grid grid-id)]
                        (if (empty? grids)
                          (res/wrap-409-response grd
                                                 "Sorry, but you need prepare the game before 
 started it")
                          (res/wrap-201-response grd
                                                 "Congrats your game is started!")))
          ;; STOP ACTION
          (= (:action body)
             "stop") (let [grids (play/stop-game! grid-id)
                            grd (play/get-grid grid-id)]
                        (if (empty? grids)
                          (res/wrap-409-response grd
                                                 "Sorry, but you can only stop the game 
 if it is in a running state!")
                          (res/wrap-201-response grd
                                                 "Congrats your game is stopped"))))))))