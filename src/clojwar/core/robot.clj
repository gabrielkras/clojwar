(ns clojwar.core.robot
  (:require [clojwar.core.robot :as rbt]
            [clojwar.core.position :as pos])
  (:import [clojwar.core.position Position])) ; Using this import to allow uses of Position defrecord

;; ROBOT TYPE ;;
(defrecord Robot [name      ; Robot name.
                  position  ; Robot grid position.
                  facing    ; Robot facing direction
                  points])  ; Robot points

(defn valid?
  "This function validates the robot configuration, such as position and points. 
This function return a boolean."
  [robot]
  {:pre [(and
          (instance? Robot robot)
          (contains? robot :name)
          (contains? robot :facing)
          (instance? Position (:position robot))
          (contains? robot :points))]}
  (cond
    ;; Validates if the robot position is into grid size.
    (not (pos/position-valid? (:position robot))) false
    ;; Validates if the facing direction are right.
    (not (pos/facing-direction-valid? (:facing robot))) false
    ;; Validates if the robot points are positive an integer.
    (neg-int? (:points robot)) false
    :else true))

(defn robot
  "This function creates and returns information about robot. The params are 
':name -> for robot identification', ':position -> for the current position 
on grid' and ':facing -> for the robot facing direction'"
  ([name position facing] (robot name position facing 0))
  ([name position facing points]
  {:pre [(string? name)
         (pos/position-valid? position)
         (pos/facing-direction-valid? facing)
         (not (neg-int? points))]}
  (let [robot (->Robot name position facing points)]
    (when (valid? robot))
    robot)))         

;; WALKING METHODS ;;
(defmulti walk
  "This function takes a robot and changes its current position. To use this 
function you need to inform what direction you would like to pursuit. 
The available options are :forward, :backward, :leftward :rightward. 
If the movement is invalid or you didn't inform the desired direction, this 
function will return a nil value."
  (fn [direction robot] direction))

;; Walking Forward
(defmethod walk :forward [_ robot]
  (when (valid? robot)
    (rbt/robot (:name robot)
               (pos/increment-y-axis (:position robot))
               :up
               (:points robot))))

;; Walking Backward
(defmethod walk :backward [_ robot]
  (when (valid? robot)
    (rbt/robot (:name robot)
               (pos/decrement-y-axis (:position robot))
               :down
               (:points robot))))

;; Walking Leftward
(defmethod walk :leftward [_ robot]
  (when (valid? robot)
    (rbt/robot (:name robot)
               (pos/decrement-x-axis (:position robot))
               :left
               (:points robot))))

;; Walking Rightward
(defmethod walk :rightward [_ robot]
  (when (valid? robot)
    (rbt/robot (:name robot)
               (pos/increment-x-axis (:position robot))
               :right
               (:points robot))))

;; POINTS METHOD ;;
(defmulti point
  "This function takes a robot and changes its current points. To use this
function you need to inform the quantity of points and if you will increase
or decrease them. The available options are ':increase' and ':decrease'. If
the operation is invalid (such as removing more points than you have),
the function will return a nil value."
  (fn [operation robot points] operation))

;; Increasing points
(defmethod point :increase [_ robot points]
  (when (valid? robot)
    (if (or (neg-int? points) (string? points))
      (throw (AssertionError. "The points number can't be negative, decimal or string!"))
      (rbt/robot (:name robot)
                 (:position robot)
                 (:facing robot)
                 (+ (:points robot) points)))))

;; Decreasing points
(defmethod point :decrease [_ robot points]
  (when (valid? robot)
     (if (or (neg-int? points) (string? points))
       (throw (AssertionError. "The points number can't be negative, decimal or string!"))
       (rbt/robot (:name robot)
                  (:position robot)
                  (:facing robot)
                  (- (:points robot) points)))))

(defn proximity-robot-range
  "This function calculates the positions around the robot based on a certain range (e.g 
if the robot is on {:x 5 :y 5} and the range is 1, this function will return the following 
list of positions => '({:x 4 :y 5} {:x 6 :y 5} {:x 5 :y 6} {:x 5 :y 4})). If some of these
positions are negative, this function will remove it, returning only the valid ones. If none
range was informed, the function will use 1 as a default value. If the positions can't be 
calculated, the function will return a nil value."
  ([robot]
   {:pre [(valid? robot)]} (proximity-robot-range robot 1))
  ([robot range]
   {:pre [(valid? robot) (pos-int? range)]}
   (let [list-positions (atom '())
         ran (atom range)]
     (while (pos? @ran)
       (do
         (swap! list-positions #(concat % (pos/proximity-position (:position robot) @ran)))
         (swap! ran dec)))
     (pos/remove-invalid-positions (into '() @list-positions)))))