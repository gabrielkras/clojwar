(ns clojwar.core.grid-test
  (:require [clojure.test :refer [deftest testing is]]
            [clojwar.core.grid :as grid]
            [clojwar.core.robot :as rbt]
            [clojwar.core.position :as pos]
            [clojwar.core.dino :as din]))

(deftest generating-grid
  (testing "Create a grid with good params."
    (is (= (frequencies (grid/grid "my-string" :running))
           (frequencies '{:id "my-string" :status :running :robot {} :dinos #{}}))))

  (testing "Create a grid with all params are numbers."
    (is (thrown? AssertionError (grid/grid 1 1)) "Should be an assertion error!"))

  (testing "Create a grid with all params are lists."
    (is (thrown? AssertionError (grid/grid '(1) '(1)) "Should be an assertion error!")))

  (testing "Create a grid with a wrong status."
    (is (thrown? AssertionError (grid/grid "my-id" :hello) "Should be an assertion error!"))))

(deftest grid-is-equal
  (testing "Checking if a grid is valid!"
    (is (true? (grid/grid-is-equal?
                (grid/grid "my-string" :running)
                (grid/grid "my-string" :running)))
        "Should be True!"))

  (testing "Checking if a grid is invalid! - 01"
    (is (nil? (grid/grid-is-equal?
                 (grid/grid "my-stri" :running)
                 (grid/grid "my-string" :running)))
        "Should be False!"))

  (testing "Checking if a grid is valid! But the facing is different!"
    (is (true? (grid/grid-is-equal?
                 (grid/grid "my-string" :running)
                 (grid/grid "my-string" :created)))
        "Should be True!")))

(deftest find-grid-by-id
  (testing "Finding the a grid by id"
    (is (= (frequencies (grid/find-grid-by-id
                         (hash-set (grid/grid "my-string01" :running)
                                   (grid/grid "my-string02" :running))
                         "my-string01"))
           (frequencies (grid/grid "my-string01" :running)))
        "Should return the grid!"))

  (testing "Not Finding the a grid by id"
    (is (nil? (grid/find-grid-by-id
               (hash-set (grid/grid "my-string01" :running)
                         (grid/grid "my-string02" :running))
               "my-string03"))
        "Should be nil!")))

(deftest remove-grid-by-id
  (testing "Removing grid by id"
    (is (= (frequencies (grid/remove-grid
                         (hash-set (grid/grid "my-string01" :running)
                                   (grid/grid "my-string02" :running))
                         "my-string01"))
           (frequencies (hash-set (grid/grid "my-string02" :running))))
        "Should return the grid!"))

  (testing "Not Removing a grid by id"
    (is (nil? (grid/remove-grid
               (hash-set (grid/grid "my-string01" :running)
                         (grid/grid "my-string02" :running))
               "my-string03"))
        "Should be nil!")))

(deftest put-grid
  (testing "Putting grid"
    (is (= (frequencies (grid/put-grid
                         (hash-set (grid/grid "my-string01" :running)
                                   (grid/grid "my-string02" :running))
                         (grid/grid "my-string03" :running)))
           (frequencies (hash-set
                         (grid/grid "my-string02" :running)
                         (grid/grid "my-string01" :running)
                         (grid/grid "my-string03" :running))))
        "Should return the grid!"))

  (testing "Trying to put an string!"
    (is (thrown? AssertionError (grid/put-grid
                                 (hash-set (grid/grid "my-string01" :running)
                                           (grid/grid "my-string02" :running))
                                 "my-string03"))
        "Should be AssertionError!")))

(deftest contains-grid?
  (testing "Checking if a grid exists!"
    (is (true? (grid/contains-grid?
                (hash-set (grid/grid "my-string01" :running)
                          (grid/grid "my-string02" :running))
                "my-string01"))
        "Should be True!"))

  (testing "Checking if a grid don't exists!"
    (is (nil? (grid/contains-grid?
               (hash-set (grid/grid "my-string01" :running)
                         (grid/grid "my-string02" :running))
               "my-string03"))
        "Should be Nil!")))

(deftest update-grid-status
  (testing "Update the grid status."
    (is (= (frequencies (grid/update-status
                         (grid/grid "my-string01" :running)
                         :created))
           (frequencies (grid/grid "my-string01" :created)))
        "Should return the grid!"))

  (testing "Update the wrong grid status."
    (is (thrown? AssertionError (grid/update-status
                                 (grid/grid "my-string01" :running)
                                 :omg))
        "Should be AssertionError")))

(deftest update-robot
  (testing "Update the robot into the grid"
    (is (= (frequencies (grid/update-robot
                         (grid/grid "my-string01" :created)
                         (rbt/robot "wall-e" (pos/position 10 10) :up)))
           (frequencies (grid/grid "my-string01" 
                                   :created
                                   (rbt/robot "wall-e" (pos/position 10 10) :up)
                                   #{})))
        "Should return the grid!"))

  (testing "Update the wrong robot parameter."
    (is (thrown? AssertionError (grid/update-robot
                                 (grid/grid "my-string01" :running)
                                 "I'm a robot, this note is truth!"))
        "Should be AssertionError!")))

(deftest remove-robot
  (testing "Remove robot from grid"
    (is (= (frequencies (grid/remove-robot
                         (grid/grid "my-string01" 
                                    :created 
                                    (rbt/robot "wall-e" (pos/position 10 10) :up)
                                    #{})))
           (frequencies (grid/grid "my-string01"
                                   :created
                                   {}
                                   #{})))
        "Should return the grid!"))

  (testing "Update the wrong robot parameter."
    (is (thrown? AssertionError (grid/remove-robot
                                 "I'm a robot, this note is truth!"))
        "Should be AssertionError!")))

(deftest add-dino
  (testing "Add a new dino into a grid"
    (is (= (frequencies (grid/add-dino
                         (grid/grid "my-string01" :created)
                         (din/dino (pos/position 10 10) :up)))
           (frequencies (grid/grid "my-string01"
                                   :created
                                   {}
                                   #{(din/dino (pos/position 10 10) :up)})))
        "Should return the grid!"))
  
(testing "Add a dino in the same robot position."
  (is (nil? (grid/add-dino (grid/grid "my-string01"
                                      :created
                                      (rbt/robot "wall-e" (pos/position 10 11) :up)
                                      (hash-set (din/dino (pos/position 10 10) :up)))
                           (din/dino (pos/position 10 11) :up)))
      "Should return a nil!"))  
  
  (testing "Add a new dino into a grid with dinos already there!"
    (let [dinos (:dinos (grid/add-dino
                         (grid/grid "my-string01"
                                    :created
                                    {}
                                    (hash-set (din/dino (pos/position 10 10) :up)
                                              (din/dino (pos/position 10 11) :up)))
                         (din/dino (pos/position 10 12) :up)))]
      (is (true? (and (din/contains-dino? dinos (pos/position 10 10))
                      (din/contains-dino? dinos (pos/position 10 11))
                      (din/contains-dino? dinos (pos/position 10 12))))
          "Should return a grid with 3 dinos!")))

  (testing "Add a new dino as string"
    (is (thrown? AssertionError (grid/add-dino
                                 (grid/grid "my-string01" :created)
                                 "Raaarrrrrhhhh!"))
        "Should thrown AssertionError!")))

(deftest remove-dino
  (testing "Remove a dino from a grid with single dino"
    (let [dinos (:dinos (grid/remove-dino
                         (grid/grid "my-string01"
                                    :created
                                    {}
                                    (hash-set (din/dino (pos/position 10 10) :up)))
                         (din/dino (pos/position 10 10) :up)))]
      (is (nil? (and (din/contains-dino? dinos (pos/position 10 10))
                     (din/contains-dino? dinos (pos/position 10 12))))
          "Should return a grid with none dinos!")))

  (testing "Remove a dino from a grid with dinos already there!"
    (let [dinos (:dinos (grid/remove-dino
                         (grid/grid "my-string01"
                                    :created
                                    {}
                                    (hash-set (din/dino (pos/position 10 10) :up)
                                              (din/dino (pos/position 10 11) :up)))
                         (din/dino (pos/position 10 11) :up)))]
      (is (true? (and (din/contains-dino? dinos (pos/position 10 10))
                      (nil? (din/contains-dino? dinos (pos/position 10 11)))))
          "Should return a grid with 2 dinos!")))

  (testing "Remove a non existent dino from a grid with dinos already there!"
    (let [dinos (:dinos (grid/remove-dino
                         (grid/grid "my-string01"
                                    :created
                                    {}
                                    (hash-set (din/dino (pos/position 10 10) :up)
                                              (din/dino (pos/position 10 11) :up)))
                         (din/dino (pos/position 10 12) :up)))]
      (is (nil? dinos)
          "Should return nil!")))

  (testing "Add a new dino as string"
    (is (thrown? AssertionError (grid/remove-dino
                                 (grid/grid "my-string01" :created)
                                 "Raaarrrrrhhhh!"))
        "Should thrown AssertionError!")))

(deftest check-grid-status
  (testing "Check if grid is created"
    (is (true? (grid/grid-is-created? (grid/grid "nugrid01" :created)))
        "Should be True!"))

  (testing "Check if grid is not created"
    (is (nil? (grid/grid-is-created? (grid/grid "nugrid01" :running)))
        "Should be nil"))

  (testing "Check if grid is ready"
    (is (true? (grid/grid-is-ready? (grid/grid "nugrid01" :ready)))
        "Should be True!"))

  (testing "Check if grid is not ready"
    (is (nil? (grid/grid-is-ready? (grid/grid "nugrid01" :running)))
        "Should be nil"))

  (testing "Check if grid is running"
    (is (true? (grid/grid-is-running? (grid/grid "nugrid01" :running)))
        "Should be True!"))

  (testing "Check if grid is not running"
    (is (nil? (grid/grid-is-running? (grid/grid "nugrid01" :created)))
        "Should be nil"))

  (testing "Check if grid is finished"
    (is (true? (grid/grid-is-finished? (grid/grid "nugrid01" :finished)))
        "Should be True!"))

  (testing "Check if grid is not finished"
    (is (nil? (grid/grid-is-finished? (grid/grid "nugrid01" :created)))
        "Should be nil")))