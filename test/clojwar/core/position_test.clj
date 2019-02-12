(ns clojwar.core.position-test
  (:require [clojure.test :refer [deftest testing is]]
            [clojwar.core.position :as pos])
  (:import [clojwar.core.position Position])) ; Using this import to allow uses of Position defrecord

(deftest create-a-position
  (testing "Create a new position."
    (is (= (frequencies (pos/position 10 20))
           (frequencies '{:x 10 :y 20} ))))
  
  (testing "Create a string position."
    (is (thrown? AssertionError (pos/position "N" "U")) "Should be nil"))
  
  (testing "Create a negative position."
    (is (nil? (pos/position 10 -10)) "Should be nil"))
  
  (testing "Create over grid size position."
    (is (nil? (pos/position 10 1000)) "Should be nil")))

(deftest checking-if-position-is-valid
  (testing "Test a valid position"
    (is (true? (pos/position-valid? (pos/->Position 1 1))) "Should be true!"))

  (testing "Test an invalid position - 01"
    (is (false? (pos/position-valid? (pos/->Position -1 1))) "Should be false!"))

  (testing "Test an invalid position - 02"
    (is (false? (pos/position-valid? (pos/->Position 1 -2))) "Should be false!"))

  (testing "Test an invalid position - 03"
    (is (false? (pos/position-valid? (pos/->Position 55 0))) "Should be false!"))

  (testing "Test an invalid position - 04"
    (is (false? (pos/position-valid? (pos/->Position 0 55))) "Should be false!"))

  (testing "Test an invalid position - 05"
    (is (false? (pos/position-valid? (pos/->Position 55 55))) "Should be false!")))

(deftest checking-if-position-is-equal
  (testing "Test a valid position"
    (is (true? (pos/position-is-equal? (pos/->Position 1 1)
                                       (pos/->Position 1 1))) 
        "Should be true!"))

  (testing "Test a not equal position - 01"
    (is (nil? (pos/position-is-equal? (pos/->Position 1 1)
                                      (pos/->Position 10 1))) 
        "Should be false!"))

  (testing "Test a not equal position - 02"
    (is (nil? (pos/position-is-equal? (pos/->Position 1 0)
                                      (pos/->Position 1 10))) 
        "Should be false!"))

  (testing "Test an invalid position - 03"
    (is (thrown? AssertionError (pos/position-is-equal? 0 0)) 
        "Should Throw AssertionError!")))

(deftest incrementing-x-axis-position
  (testing "Increments the x-axis position"
    (is (= (frequencies (pos/increment-x-axis (pos/position 10 20)))
           (frequencies '{:x 11 :y 20})) "Should be {:x 11 :y 20}"))

  (testing "Test increment an invalid position parameter"
    (is (thrown? AssertionError (pos/increment-x-axis (pos/->Position -1 "string"))) 
        "Should be AssertionError!")))

(deftest decremented-x-axis-position
  (testing "Decrements the x-axis position"
    (is (= (frequencies (pos/decrement-x-axis (pos/position 10 20)))
           (frequencies '{:x 9 :y 20})) "Should be {:x 9 :y 20}"))

  (testing "Test increment an invalid position parameter"
    (is (thrown? AssertionError (pos/decrement-x-axis (pos/->Position -1 "string")))
        "Should be AssertionError!")))

(deftest incrementing-y-axis-position
  (testing "Increments the x-axis position"
    (is (= (frequencies (pos/increment-y-axis (pos/position 10 20)))
           (frequencies '{:x 10 :y 21})) "Should be {:x 10 :y 20}"))

  (testing "Test increment an invalid position parameter"
    (is (thrown? AssertionError (pos/increment-y-axis (pos/->Position "string" 0)))
        "Should be AssertionError!")))

(deftest decremented-y-axis-position
  (testing "Decrements the y-axis position"
    (is (= (frequencies (pos/decrement-y-axis (pos/position 10 20)))
           (frequencies '{:x 10 :y 19})) "Should be {:x 10 :y 19}"))

  (testing "Test increment an invalid position parameter"
    (is (thrown? AssertionError (pos/decrement-y-axis (pos/->Position "string" 0)))
        "Should be AssertionError!")))

(deftest cleaning-position-list
  (testing "Remove the invalid position => {:x -50 :y 10}."
    (is (= (frequencies (pos/remove-invalid-positions 
                         (list (pos/->Position 10 10) 
                               (pos/->Position 20 20)
                               (pos/->Position -50 1))))
           (frequencies '(#clojwar.core.position.Position{:x 10 :y 10} 
                          #clojwar.core.position.Position{:x 20 :y 20})))))

  (testing "Remove the invalid position => {:x 1 :y -50}."
    (is (= (frequencies (pos/remove-invalid-positions (list
                                                       (pos/position 20 20)
                                                       (pos/position 1 -50))))
           (frequencies '(#clojwar.core.position.Position{:x 20 :y 20})))))
  
  (testing "Trying to remove a position from a single map."
    (is (thrown? AssertionError (pos/decrement-y-axis '({:x 1 :y 2} {:x -50 y -50})))
        "Should be AssertionError!")))

(deftest checking-if-facing-direction-is-valid
  (testing "Test a valid facing direction - 01"
    (is (true? (pos/facing-direction-valid? :up)) "Should be true!"))

  (testing "Test a valid facing direction - 02"
    (is (true? (pos/facing-direction-valid? :down)) "Should be true!"))

  (testing "Test a valid facing direction - 03"
    (is (true? (pos/facing-direction-valid? :left)) "Should be true!"))

  (testing "Test a valid facing direction - 04"
    (is (true? (pos/facing-direction-valid? :right)) "Should be true!"))

  (testing "Test an invalid facing direction - 01"
    (is (nil? (pos/facing-direction-valid? :ops)) "Should be nil!"))

  (testing "Test an invalid facing direction - 02"
    (is (nil? (pos/facing-direction-valid? "HELP")) "Should be nil!"))

  (testing "Test an invalid facing direction - 03"
    (is (nil? (pos/facing-direction-valid? 1)) "Should be nil!"))

  (testing "Test an invalid facing direction - 04"
    (is (nil? (pos/facing-direction-valid? '())) "Should be nil!")))