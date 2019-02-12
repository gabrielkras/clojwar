(ns clojwar.game
  (:require [clojwar.core.grid :as grd]
            [clojwar.core.robot :as rbt]
            [clojwar.core.dino :as din]
            [clojwar.core.position :as pos]
            [clojwar.db.memory :refer (->AtomStore)]
            [clojwar.protocols.grid :refer [GridStore
                                          get-grids
                                          put-grid!
                                          update-grid!
                                          get-grid-by-id
                                          delete-grid!
                                          reset-storage!]]))

;; START INSTANCE STORAGE ;;
(def storage (->AtomStore (atom #{})))

;; ESPECIAL MEMORY DB FUNCTION - TEST ONLY ;;
(defn reset-store! []
  "This is an especial function used for cleaning the database. Be careful with that!
Use only for test propose!"
  (reset-storage! storage))

;; GAME METHODS LOGIC ;;
(defn create-grid! 
  [grid-id]
  "This function creates a new grid and saves into storage. If the grid already exists
the function will return null."
  (let [grid (get-grid-by-id storage grid-id)]
    (when (empty? grid)
      (put-grid! storage (grd/grid grid-id :created)))))

(defn create-robot! 
  [grid-id robot-name robot-pos-x robot-pos-y facing-direction]
  "This function gets a grid from storage, create a new robot and update the storage
information. If the function can't find the grid or the robot already exists the 
function will return nil."
  (let [grid (get-grid-by-id storage grid-id)
        old-robot (:robot grid)
        robot (rbt/robot robot-name (pos/position robot-pos-x robot-pos-y) facing-direction)
        new-grid (grd/update-robot grid robot)]
    (when (empty? old-robot)
      (when (seq new-grid)
        (update-grid! storage new-grid)))))

(defn remove-robot! 
  [grid-id]
  "This function gets a grid from storage, remove the robot and update the storage
information. If the function can't find the grid or the robot doesn't exists or
the grid status is not equal 'created', the function will return nil."
  (let [grid (get-grid-by-id storage grid-id)
        robot (:robot grid)
        new-grid (grd/remove-robot grid)]
    (when (grd/grid-is-created? grid)
      (when (seq robot)
        (update-grid! storage new-grid)))))

(defn add-dino! 
  [grid-id dino-pos-x dino-pos-y]
  "This function adds a new dino into the grid game. If you try to add an dino in the same
position or add in an use position (such as robot position) or if the function can't find
the grid,  this function will return nil."
  (let [grid (get-grid-by-id storage grid-id)
        dino (din/dino (pos/position dino-pos-x dino-pos-y) (pos/get-random-direction))
        new-grid (grd/add-dino grid dino)]
    (when (seq new-grid)
      (update-grid! storage new-grid))))

(defn get-grid 
  [grid-id]
  "This function gets from storage the grid based on his id. If the function can't find
the grid, this function will return nil."
  (get-grid-by-id storage grid-id))

(defn get-grid-all []
  "This function gets from storage the grid based on his id. If the function can't find
the grid, this function will return nil."
  (get-grids storage))

(defn prepare-game! 
  [grid-id]
  "This function check if the grid has at least one dino and a robot to start the game. If the function
can't find the grid or doesn't has a robot and a dino, this function will return nil. If every think
is ok, the function updates the status from created to ready of from finished to ready."
  (let [grid (get-grid-by-id storage grid-id)]
    (when (and (or (grd/grid-is-created? grid) (grd/grid-is-finished? grid))
               (seq (:robot grid))
               (seq (:dinos grid)))
      (update-grid! storage (grd/update-status grid :ready)))))

(defn start-game! 
  [grid-id]
  "This function starts the game changing the grid status from ready to running. If the function
can't find the grid or the status is different of ready, this function will return nil."
  (let [grid (get-grid-by-id storage grid-id)]
    (when (grd/grid-is-ready? grid)
      (update-grid! storage (grd/update-status grid :running)))))

(defn stop-game! 
  [grid-id]
  "This function stops the game changing the grid status from running to finished. If the function
can't find the grid or the status is different of running, this function will return nil."
  (let [grid (get-grid-by-id storage grid-id)]
    (when (grd/grid-is-running? grid)
      (update-grid! storage (grd/update-status grid :finished)))))

(defn robot-walk! 
  [grid-id direction]
  "This function executes the robot movement, if the robot can't move, the function will 
return nil. If the robot try to walk across the grid, a Position AssertionError will be
thrown. The direction param only accepts ':forward', ':backward', ':leftward' and ':rightward'.
The function can only be executed with the grid status is running."
  (let [grid (get-grid-by-id storage grid-id)]
    (when (grd/grid-is-running? grid)
      (let [new-robot (rbt/walk direction (:robot grid))]
        (when-not (din/contains-dino? (:dinos grid) (:position new-robot))
          (when (= (:status grid) :running)
            (update-grid! storage (grd/update-robot grid new-robot))))))))

(defn robot-attack! 
  [grid-id]
  "This function executes an attack from robot. All dinosaurs will be destroyed if they stay a
one square position of distance from robot."
  (let [grid (get-grid-by-id storage grid-id)]
    (when (grd/grid-is-running? grid)
      (let [positions-list (rbt/proximity-robot-range (:robot grid))
            dinos-set-count (count (:dinos grid))
            new-dinos-set (din/remove-dinos-by-position (:dinos grid) positions-list)
            robot-points (- dinos-set-count (count new-dinos-set))]
        (when (pos-int? robot-points)
          (update-grid! storage (grd/update-dinos (grd/update-robot grid
                                                                    (rbt/point :increase
                                                                               (:robot grid)
                                                                               robot-points))
                                                  new-dinos-set)))))))