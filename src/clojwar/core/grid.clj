(ns clojwar.core.grid
  (:require [clojwar.core.grid :as g]
            [clojwar.core.dino :as din]
            [clojwar.core.robot :as rbt]
            [clojwar.core.position :as pos])
  (:import [clojwar.core.dino Dino]
           [clojwar.core.robot Robot]))  ; Using this import to allow uses of Dino defrecord

;; GRID TYPE ;;
(defrecord Grid [id       ; The id of the grid -> Used to identify the current game.
                 status   ; The status game.
                 robot    ; The robot representation -> Check robot namespace for details.
                 dinos])  ; The dinos list positions -> Check dinos namespace for details.

;; GRID CONSTRAINTS ;;
(def valid-status '(:created    ; Represents when a grid is created but not prepared to be executed.
                    :ready      ; Represents when a grid is ready to be executed.
                    :running    ; Represents when a grid is being executed.
                    :finished)) ; Represents when a grid finished the execution.

(defn grid
  "This function generate a grid map and return it. The params are 
':id -> for grid identification', ':size -> for the grid size' and 
':status -> for the grid status which only have :created, :running 
or :finished value'."
  ([id status] (grid id status '{} #{}))
  ([id status robot dinos]
  {:pre [(string? id)
         (and (some (partial = status) valid-status) 
              (keyword? status))]}
  (->Grid id status robot dinos)))

(defn grid-is-equal?
  "This function checks if the grids are equals. This function consider equals
when the both grid-id (on both grid objects) has the same value"
  [grid-a grid-b]
  {:pre [(instance? Grid grid-a) (instance? Grid grid-b)]}
  (when (= (:id grid-a) (:id grid-b))
    true))

(defn find-grid-by-id
  "This function finds the grid based on id and return it."
  [grid-set id]
  {:pre [(set? grid-set) (string? id)]}
  (first (filter #(= id (:id %)) grid-set)))

(defn remove-grid
  "This function removes a grid object from a grid set based on his
id and returns a new set containing the rest of objects. If the function
cant remove the grid, the function will return nil."
  [grid-set id]
  {:pre [(set? grid-set)]}
  (let [del-grid (g/find-grid-by-id grid-set id)]
    (when (seq del-grid)
      (set (remove #(g/grid-is-equal? del-grid %) grid-set)))))

(defn put-grid
  "This function put a grid object into a grid-set."
  [grid-set grid]
  {:pre [(set? grid-set) (instance? Grid grid)]}
  (conj grid-set grid))

(defn contains-grid?
  "This function checks if has some grid object into grid-set, based on
his id. If the function cant find the grid, the function will return nil."
  [grid-set id]
  {:pre [(set? grid-set) (string? id)]}
  (let [grid (g/find-grid-by-id grid-set id)]
    (when (seq grid) true)))

(defn grid-is-running? [grid]
  "This function returns true if the grid status is running."
  (when (= (:status grid) :running)
    true))

(defn grid-is-ready? [grid]
  "This function returns true if the grid status is ready."
  (when (= (:status grid) :ready)
    true))

(defn grid-is-created? [grid]
  "This function returns true if the grid status is created."
  (when (= (:status grid) :created)
    true))

(defn grid-is-finished? [grid]
  "This function returns true if the grid status is finished."
  (when (= (:status grid) :finished)
    true))

(defn update-status 
  "This function update the grid status. The valid status are ':created'
':ready', ':running' or ':finished'"
  [grid status]
  {:pre [(instance? Grid grid) 
         (and (some (partial = status) valid-status) (keyword? status))]}
  (g/grid (:id grid)
          status
          (:robot grid)
          (:dinos grid)))

(defn update-robot
  "This function takes a grid, a robot and updates the robot information returning
the new grid object."
  [grid robot]
  {:pre [(instance? Grid grid) (instance? Robot robot)]}
  (let [new-grid (g/grid (:id grid)
                         (:status grid)
                         robot
                         (:dinos grid))]
    (when-not (and (empty? (-> grid :robot :position))
                   (din/contains-dino? (:dinos grid) (:position robot)))
      new-grid)))

(defn remove-robot
  "This function takes a grid and removes the robot information returning
the new grid object."
  [grid]
  {:pre [(instance? Grid grid)]}
  (g/grid (:id grid) (:status grid) {} (:dinos grid)))

(defn update-dinos
  "This function takes a grid, a dinos set and updates the dinos information returning
the new grid object."
  [grid dinos-set]
  {:pre [(instance? Grid grid) (set? dinos-set)]}
  (g/grid (:id grid) (:status grid) (:robot grid) dinos-set))

(defn add-dino
  "This function adds a dino to a grid and returns the new grid. If you try to add
a new dino in the same position, the function will return nil."
  [grid dino]
  {:pre [(instance? Grid grid) (instance? Dino dino)]}
  (when-not (din/contains-dino? (:dinos grid) (:position dino))
    (let [new-grid (g/grid (:id grid)
                           (:status grid)
                           (:robot grid)
                           (din/put-dino (:dinos grid) dino))]
      (if-not (empty? (-> grid :robot :position))
        (when-not (pos/position-is-equal? (-> grid :robot :position) (:position dino))
          new-grid)
        new-grid))))

(defn remove-dino
  "This function removes a dino from a grid and returns the new grid. If you try to
remove an nonexistent dino, the function will return nil."
  [grid dino]
  {:pre [(instance? Grid grid) (instance? Dino dino)]}
  (when (din/contains-dino? (:dinos grid) (:position dino))
    (g/grid (:id grid)
            (:status grid)
            (:robot grid)
            (din/remove-dino (:dinos grid) (:position dino)))))