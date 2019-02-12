(ns clojwar.core.robot-test
  (:require [clojure.test :refer [deftest testing is]]
            [clojwar.core.robot :as rbt]
            [clojwar.core.position :as pos])
  (:import [clojwar.core.position Position])) ; Using this import to allow uses of Position defrecord

(deftest generating-robot
  (testing "Create a robot with good params."
    (is (= (frequencies (rbt/robot "wall-e" (pos/position 10 20) :up))
           (frequencies '{:name "wall-e"
                          :position #clojwar.core.position.Position{:x 10 :y 20}
                          :facing :up
                          :points 0}))))

  (testing "Create a robot with all params are numbers."
    (is (thrown? AssertionError (rbt/robot 1 1 1)) "Should be an assertion error!"))

  (testing "Create a robot with all params are lists."
    (is (thrown? AssertionError (rbt/robot '(1) '(1) '(1)) "Should be an assertion error!")))

  (testing "Create a robot with facing position wrong."
    (is (thrown? AssertionError (rbt/robot "wall-e" (pos/position 10 10) :edge) "Should be an assertion error!")))
  
  (testing "Create a robot with negative numbers position."
    (is (thrown? AssertionError (rbt/robot "wall-e" (pos/->Position -10 -10) :up) "Should be an assertion error!"))))

(deftest validating-robot
  (testing "Validate robot position."
    (assert (rbt/valid? (rbt/->Robot "wall-e" (pos/position 10 20) :up 0)) "Should be true!"))
  
  (testing "Validate robot with x axis being negative."
    (is (false? (rbt/valid? (rbt/->Robot "wall-e" (pos/->Position -10 20) :up 0))) "Should be false!"))
  
  (testing "Validate robot with y axis being negative."
    (is (false? (rbt/valid? (rbt/->Robot "wall-e" (pos/->Position 10 -20) :up 0))) "Should be false!"))
  
  (testing "Validate robot with x and y axis being negative."
    (is (false? (rbt/valid? (rbt/->Robot "wall-e" (pos/->Position -10 -20) :up 0))) "Should be false!"))
  
  (testing "Validate robot with negative points."
    (is (false? (rbt/valid? (rbt/->Robot "wall-e" (pos/position 10 20) :up -1))) "Should be false!"))
  
  (testing "Validate robot without position key."
    (is (thrown? AssertionError (@#'rbt/valid? {:name "wall-e" :facing :up :points -1})) 
        "Should be an Assertion Error!")))

;; WALKING TESTS ;;
(deftest walking-forward
  (testing "Walk a single space forward."
    (is (= (frequencies (rbt/walk :forward (rbt/->Robot "wall-e" (pos/->Position 10 20) :down 0)))
           (frequencies '{:name "wall-e" 
                          :position #clojwar.core.position.Position{:x 10 :y 21} 
                          :facing :up 
                          :points 0}))))

  (testing "Walk across the edge."
    (is (thrown? AssertionError (rbt/walk :forward (rbt/->Robot "wall-e" (pos/->Position 10 49) :down 0))) 
        "Should be Assertion Error!"))
  
  (testing "Try to walk with a broken robot."
    (is (thrown? AssertionError (rbt/walk :forward {:name "wall-e" :facing :up :points 0})) 
        "Should be Assertion Error!")))

(deftest walking-backward
  (testing "Walk a single space backward."
    (is (= (frequencies (rbt/walk :backward (rbt/->Robot "wall-e" (pos/->Position 10 20) :up 0)))
           (frequencies '{:name "wall-e" 
                          :position #clojwar.core.position.Position{:x 10 :y 19} 
                          :facing :down 
                          :points 0}))))

  (testing "Walk across the edge."
    (is (thrown? AssertionError (rbt/walk :backward (rbt/->Robot "wall-e" (pos/->Position 10 0) :down 0))) 
        "Should be Assertion Error!"))

  (testing "Try to walk with a broken robot."
    (is (thrown? AssertionError (rbt/walk :backward {:name "wall-e" :facing :down :points 0})) 
        "Should be Assertion Error!")))

(deftest walking-leftward
  (testing "Walk a single space leftward."
    (is (= (frequencies (rbt/walk :leftward (rbt/->Robot "wall-e" (pos/->Position 10 20) :up 0)))
           (frequencies '{:name "wall-e" 
                          :position #clojwar.core.position.Position{:x 9 :y 20} 
                          :facing :left 
                          :points 0}))))

  (testing "Walk across the edge."
    (is (thrown? AssertionError (rbt/walk :leftward (rbt/->Robot "wall-e" (pos/->Position 0 0) :left 0))) 
        "Should be Assertion Error!"))

  (testing "Try to walk with a broken robot."
    (is (thrown? AssertionError (rbt/walk :leftward {:name "wall-e" :facing :up :points 0})) 
        "Should be Assertion Error!")))

(deftest walking-rightward
  (testing "Walk a single space rightward."
    (is (= (frequencies (rbt/walk :rightward (rbt/->Robot "wall-e" (pos/->Position 10 20) :up 0)))
           (frequencies '{:name "wall-e" 
                          :position #clojwar.core.position.Position{:x 11 :y 20} 
                          :facing :right 
                          :points 0}))))

  (testing "Walk across the edge."
    (is (thrown? AssertionError (rbt/walk :rightward (rbt/->Robot "wall-e" (pos/->Position 49 0) :up 0))) 
        "Should be Assertion Error!"))

  (testing "Try to walk with a broken robot."
    (is (thrown? AssertionError (rbt/walk :backward {:name "wall-e" :facing :up :points 0})) 
        "Should be Assertion Error!")))

(deftest increasing-points
  (testing "Increase a single point to a robot."
    (is (= (frequencies (rbt/point :increase (rbt/->Robot "wall-e" (pos/->Position 10 20) :up 0) 1))
           (frequencies '{:name "wall-e" 
                          :position #clojwar.core.position.Position{:x 10 :y 20} 
                          :facing :up 
                          :points 1}))))
  
  (testing "Increase three points to a robot."
    (is (= (frequencies (rbt/point :increase (rbt/->Robot "wall-e" (pos/->Position 10 20) :up 5) 3))
           (frequencies '{:name "wall-e" 
                          :position #clojwar.core.position.Position{:x 10 :y 20} 
                          :facing :up 
                          :points 8}))))

  (testing "Try to increase negative number."
    (is (thrown? AssertionError (rbt/point :increase (rbt/->Robot "wall-e"
                                                                  (pos/->Position 49 0) 
                                                                  :up 
                                                                  10) 
                                           -1)) 
        "Should be an Assertion Error!"))
  
  (testing "Try to increase string."
    (is (thrown? AssertionError (rbt/point :increase (rbt/->Robot "wall-e" 
                                                                  (pos/->Position 49 0) 
                                                                  :up 
                                                                  10) 
                                           "potato")) 
        "Should be an Assertion Error!"))

  (testing "Try to increase with a broken robot."
    (is (thrown? Exception (rbt/point :increase {:name "wall-e" :facing :up :points 0})) 
        "Should be Exception!")))

(deftest decreasing-points
  (testing "Decrease a single point to a robot."
    (is (= (frequencies (rbt/point :decrease (rbt/->Robot "wall-e" (pos/->Position 10 20) :up 1) 1))
           (frequencies '{:name "wall-e" 
                          :position #clojwar.core.position.Position{:x 10 :y 20} 
                          :facing :up 
                          :points 0}))))

  (testing "Decrease three points to a robot."
    (is (= (frequencies (rbt/point :decrease (rbt/->Robot "wall-e" (pos/->Position 10 20) :up 5) 3))
           (frequencies '{:name "wall-e" 
                          :position #clojwar.core.position.Position{:x 10 :y 20} 
                          :facing :up 
                          :points 2}))))

  (testing "Decrease more points than you have."
    (is (thrown? AssertionError (rbt/point :decrease (rbt/->Robot "wall-e" (pos/->Position 49 0) :up 2) 5)) 
        "Should be an Assertion Error!"))

  (testing "Try to decrease negative number."
    (is (thrown? AssertionError (rbt/point :decrease (rbt/->Robot "wall-e" (pos/->Position 49 0) :up 10) -1)) 
        "Should be an Assertion Error!"))

  (testing "Try to decrease string."
    (is (thrown? AssertionError (rbt/point :decrease (rbt/->Robot "wall-e" (pos/->Position 49 0) :up 10) "potato")) 
        "Should be an Assertion Error!"))

  (testing "Try to decrease with a broken robot."
    (is (thrown? Exception (rbt/walk :decrease (rbt/->Robot "wall-e" :up 0))) 
        "Should be Exception!")))

;; POSITIONS TESTS ;;
(deftest calculates-position-proximity
  (testing "Get proximity list of {:x 5 :y 5} and with 1 of range."
    (is (= (frequencies (rbt/proximity-robot-range (rbt/->Robot "wall-e" (pos/->Position 5 5) :up 10)))
           (frequencies '(#clojwar.core.position.Position{:x 4 :y 5} 
                          #clojwar.core.position.Position{:x 6 :y 5} 
                          #clojwar.core.position.Position{:x 5 :y 4} 
                          #clojwar.core.position.Position{:x 5 :y 6})))))

  (testing "Get proximity list of {:x 5 :y 5} and with 2 of range."
    (is (= (frequencies (rbt/proximity-robot-range (rbt/->Robot "wall-e" (pos/->Position 5 5) :up 10) 2))
           (frequencies '(#clojwar.core.position.Position{:x 4 :y 5} 
                          #clojwar.core.position.Position{:x 3 :y 5} 
                          #clojwar.core.position.Position{:x 6 :y 5} 
                          #clojwar.core.position.Position{:x 7 :y 5} 
                          #clojwar.core.position.Position{:x 5 :y 4} 
                          #clojwar.core.position.Position{:x 5 :y 3} 
                          #clojwar.core.position.Position{:x 5 :y 6} 
                          #clojwar.core.position.Position{:x 5 :y 7})))))
  
  (testing "Get proximity list of {:x 0 :y 0} and with 1 of range."
    (is (= (frequencies (rbt/proximity-robot-range (rbt/->Robot "wall-e" (pos/->Position 0 0) :up 10)))
           (frequencies '(#clojwar.core.position.Position{:x 1 :y 0}
                          #clojwar.core.position.Position{:x 0 :y 1})))))

  (testing "Get proximity list of {:x 0 :y 0} and with 2 of range."
    (is (= (frequencies (rbt/proximity-robot-range (rbt/->Robot "wall-e" (pos/->Position 0 0) :up 10) 2))
           (frequencies '(#clojwar.core.position.Position{:x 1 :y 0} 
                          #clojwar.core.position.Position{:x 2 :y 0} 
                          #clojwar.core.position.Position{:x 0 :y 1} 
                          #clojwar.core.position.Position{:x 0 :y 2}))))))

