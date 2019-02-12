(ns clojwar.api.controllers.robot
  (:require [clojwar.game :as play]
            [clojwar.api.middleware.responses :as res]
            [clojwar.api.middleware.validator :as vld]))

(defn get-robot-handler
  "This function handles the GET method of robot, returning the robot."
  [grid-id]
  (let [grid (play/get-grid grid-id)]
    (if (empty? grid)
      (res/wrap-404-response {:grid-id grid-id} "There are no registered grid with this name")
      (if (empty? (:robot grid))
        (res/wrap-404-response {:grid grid} "This grid does not contains a robot.")
        (res/wrap-200-response {:robot (:robot grid)})))))

(defn post-robot-handler
  "This function handles the POST method of robot, creating a new robot resource."
  [body grid-id]
  (let [grid (play/get-grid grid-id)]
    (if (empty? grid)
      (res/wrap-404-response {:grid-id grid-id} "There are no registered grid with this name")
      (if (or (empty? body) (empty? (vld/robot-body-validator body grid-id)))
        (res/wrap-400-response {:body body} "Invalid request body! 
 Take a look on the docs for more details")
        (try 
          (if (empty? (play/create-robot! grid-id
                                          (:name body)
                                          (:robot-pos-x body)
                                          (:robot-pos-y body)
                                          (keyword (:facing-direction body))))
            (res/wrap-409-response {:robot (:robot grid)} "There already exists a robot on 
 this grid or you trying to create a robot in the same existent dinosaur position")
            (res/wrap-201-response (play/get-grid grid-id)))
          (catch AssertionError e
                        (res/wrap-400-response grid 
                                               "You can't create a robot outside of grid area!")))))))

(defn delete-robot-handler
  "This function handles the DELETE method of robot, altering the current robot resource"
  [grid-id]
  (let [grid (play/get-grid grid-id)]
    (if (empty? grid)
      (res/wrap-404-response {:grid-id grid-id} "There are no registered grid with this name")
      (if (empty? (play/remove-robot! grid-id))
        (res/wrap-404-response {:grid grid} "There is no robot into this grid!")
        (res/wrap-200-response (play/get-grid grid-id))))))

(defn put-robot-handler
  "This function handles the PUT method of robot, altering the current robot resource"
  [body grid-id]
  (let [grid (play/get-grid grid-id)]
    (if (empty? grid)
      (res/wrap-404-response {:grid-id grid-id} "There are no registered grid with this name")
      (if (or (empty? body) (empty? (vld/robot-body-actions-validator body grid-id)))
        (res/wrap-400-response {:body body} "Invalid request body! 
 Take a look on the docs for more details")
        (if (not= (:status grid) :running)
          (res/wrap-409-response grid "Sorry, but you need to start the game first 
 before pursuing with this operation")
          (cond
          ;; ATTACK ACTION
            (= (:action body)
               "attack") (let [grids (play/robot-attack! grid-id)
                               grd (play/get-grid grid-id)]
                           (if (empty? grids)
                             (res/wrap-200-response grd
                                                    "Sorry, you not killed any dinosaur")
                             (res/wrap-201-response grd
                                                    "Congrats you killed some dinosaur")))
          ;; WALK ACTION
            (= (:action body)
               "walk") (let [grids (try
                                     (play/robot-walk! grid-id (keyword (:value body)))
                                     (catch AssertionError e
                                       (res/wrap-409-response body "Sorry, but you can't walk 
 further on that direction")))
                             grd (play/get-grid grid-id)]
                         (if (and (contains? grids :status) (= (:status grids) 409))
                           grids
                           (if (empty? grids)
                             (res/wrap-409-response grd "Sorry, entity collision detected! 
 try to walk in another direction")
                             (res/wrap-201-response grd))))))))))