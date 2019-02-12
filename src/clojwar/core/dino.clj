(ns clojwar.core.dino
  (:require [clojwar.core.position :as pos])
  (:import [clojwar.core.position Position]))

;; DINOSAUR TYPE ;;
(defrecord Dino [position  ; Robot grid position
                 facing])  ; Robot facing direction

(defn dino
  "This function creates and returns information about dinosaur."
  [position facing]
  {:pre [(pos/position-valid? position)
         (pos/facing-direction-valid? facing)]}
  (->Dino position facing))

(defn remove-dino
  "This function removes a dino object from a dino set based on his
position and returns a new list containing the rest of objects."
  [dino-set position]
  {:pre [(set? dino-set) (instance? Position position)]}
  (set (remove #(pos/position-is-equal? position (:position %)) dino-set)))

(defn get-dino
  "This function gets a dino object from a dino set based on his
position or returns nil if can't found-it!"
  [dino-set position]
  {:pre [(set? dino-set) (instance? Position position)]}
  (filter #(pos/position-is-equal? position (:position %)) dino-set))

(defn put-dino
  "This function put a dino object into a dino-set."
  [dino-set dino]
  {:pre [(set? dino-set) (instance? Dino dino)]}
  (conj dino-set dino))

(defn contains-dino?
  "This function checks if has some dino object into dino-set, based on
his position."
  [dino-set position]
  {:pre [(set? dino-set) (instance? Position position)]}
  (some #(pos/position-is-equal? position (:position %)) dino-set))

(defn remove-dinos-by-position 
  "This function removes all dinos from a dino-set based on a position list. If the function
cant remove any dino from dino-set, the function will return nil."
  [dino-set positions-list]
  {:pre [(set? dino-set) (coll? positions-list)]}
  (if (empty? positions-list)
    dino-set
    (recur (remove-dino dino-set (first positions-list)) (rest positions-list))))