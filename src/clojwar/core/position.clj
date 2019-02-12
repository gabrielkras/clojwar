(ns clojwar.core.position
  (:require [clojwar.core.position :as pos]
            [clojwar.configuration :as conf]))

;; POSITION TYPE ;;
(defrecord Position [x y])

(defn position-valid?
  "This function checks if a map position element is valid. The function consider
valid when a position contains X and Y keys with integer values greater or 
equal 0 or if the values aren't greater than the default grid size values."
  [position] (and
              (instance? Position position)
              (contains? position :x)
              (contains? position :y)
              (not (neg-int? (:x position)))
              (not (>= (:x position) (:x (:size conf/param))))
              (not (neg-int? (:y position)))
              (not (>= (:y position) (:y (:size conf/param))))))

(defn position-is-equal?
  "This function checks if the positions are equals. This function consider equals
when the both x-axis and y-axis (on both positions objects) has the same value."
  [position-a position-b]
  {:pre [(instance? Position position-a) (instance? Position position-b)]}
  (when (and (= (:x position-a) (:x position-b))
             (= (:y position-a) (:y position-b)))
    true))

(defn position
  "This is a constructor position function. This function returns a position object.
If none parameter is inform to the function, the function will consider x-axis 0 and
y-axis 0. If the position is invalid, the function will return nil."
  ([] (position 0 0))
  ([x-axis y-axis]
   {:pre [(int? x-axis) (int? y-axis)]}
   (let [position (->Position x-axis y-axis)]
     (when (position-valid? position)
       position))))

(defn increment-x-axis
  "This function get a position object and increments their x-axis returning a new
position object."
  [position]
  {:pre [(instance? Position position)]}
  (pos/position (-> position :x inc) (:y position)))

(defn decrement-x-axis
  "This function get a position object and decrements their x-axis returning a new
position object."
  [position]
  {:pre [(instance? Position position)]}
  (pos/position (-> position :x dec) (:y position)))

(defn increment-y-axis
  "This function get a position object and increments their y-axis returning a new
position object."
  [position]
  {:pre [(instance? Position position)]}
  (pos/position (:x position) (-> position :y inc)))

(defn decrement-y-axis
  "This function get a position object and decrements their y-axis returning a new
position object."
  [position]
  {:pre [(instance? Position position)]}
  (pos/position (:x position) (-> position :y dec)))

(defn proximity-position
  "This function just return a list of positions based on a range and a start position."
  [position range]
  {:pre [(instance? Position position) (pos-int? range)]}
  (list
   (pos/position (- (:x position) range) (:y position))   ; Left-Side
   (pos/position (+ (:x position) range) (:y position))   ; Right-Side
   (pos/position (:x position) (- (:y position) range))   ; Down-Side
   (pos/position (:x position) (+ (:y position) range)))) ; Up-Side

(defn remove-invalid-positions
  "This function will remove the negative or invalid positions 
(such as {:a 1 :y 2} or {:x -10 :y 1}) based on a input list. 
If the list doesn't have anything to remove, the function just 
will return the input itself."
  ([positions]
   {:pre [(list? positions)]}
   (remove-invalid-positions positions '()))
  ([positions new-positions]
   (if (empty? positions)
     new-positions
     (recur
      (remove (partial = (first positions)) positions)
      (if-not (and
               (contains? (first positions) :x)
               (contains? (first positions) :y))
        new-positions
        (if-not (position-valid? (first positions))
          new-positions
          (conj new-positions (first positions))))))))

(remove-invalid-positions (list (->Position 14 10) (->Position 14 70)))

;; FACING DIRECTION METHODS ;;
(def facing-directions '(:up      ; Looking up.
                         :down    ; Looking down.
                         :left    ; Looking left.
                         :right)) ; Looking right.

(defn facing-direction-valid?
  "This function checks if the facing-direction is one of (:up, :down, :left and :right), otherwise
the function will return false or nil."
  [facing] (and
            (some (partial = facing) facing-directions)
            (keyword? facing)))

(defn get-random-direction []
  "This function gets a random facing direction."
  (rand-nth facing-directions))