(ns clojwar.game-test
  (:require [clojure.test :refer [deftest testing is]]
            [clojwar.core.grid :as grd]
            [clojwar.core.robot :as rbt]
            [clojwar.core.position :as pos]
            [clojwar.core.dino :as din]
            [clojwar.game :as play]))

(deftest clear-memory
  (testing "CLEAR MEMORY BEFORE TESTS"
    (play/reset-store!)
    (is (true? true))))

(deftest testing-gameplay
  (testing "Create a Grid!"
    ; (play/reset-store!)
    (is (= (frequencies (play/create-grid! "game01"))
           (frequencies (hash-set (grd/grid "game01" :created))))))

  (testing "Create Robot!"
    (is (= (frequencies (play/create-robot! "game01" "wall-e" 5 5 :up))
           (frequencies (hash-set (grd/grid "game01"
                                            :created
                                            (rbt/robot "wall-e" (pos/position 5 5) :up)
                                            #{}))))))

  (testing "Remove Robot!"
    (is (= (frequencies (play/remove-robot! "game01"))
           (frequencies (hash-set (grd/grid "game01"
                                            :created
                                            {}
                                            #{}))))))

  (testing "Remove Robot from a empty grid"
    (is (nil? (play/remove-robot! "game01"))
        "Should be nil!"))

  (testing "Create Robot Again!"
    (is (= (frequencies (play/create-robot! "game01" "wall-e" 5 5 :up))
           (frequencies (hash-set (grd/grid "game01"
                                            :created
                                            (rbt/robot "wall-e" (pos/position 5 5) :up)
                                            #{}))))))

  (testing "Add a Dino!"
    (play/add-dino! "game01" 4 4)
    (is (din/contains-dino? (:dinos (play/get-grid "game01")) (pos/position 4 4))
        "Should be True!."))

  (testing "Get Some Grid"
    (is (= "game01" (:id (play/get-grid "game01")))
        "Should be True!."))

  (testing "Preparing the Game"
    (play/create-grid! "nugame01")
    (play/create-robot! "nugame01" "Mr.Robot" 1 1 :up)
    (play/add-dino! "nugame01" 1 3)
    (is (= (:status (grd/find-grid-by-id (play/prepare-game! "nugame01") "nugame01")) :ready)
        "Should be ready!"))

  (testing "Starting the Game"
    (is (= (:status (grd/find-grid-by-id (play/start-game! "nugame01") "nugame01")) :running)
        "Should be ready!"))

  (testing "Remove Robot from a running grid status"
    (is (nil? (play/remove-robot! "nugame01"))
        "Should be nil!"))

  (testing "Robot Walk Forward!"
    (play/create-grid! "nugame02")
    (play/create-robot! "nugame02" "wall-e" 5 5 :down)
    (play/add-dino! "nugame02" 1 3)
    (play/prepare-game! "nugame02")
    (play/start-game! "nugame02")
    (is (= (:position (:robot
                       (grd/find-grid-by-id (play/robot-walk! "nugame02" :forward) "nugame02")))
           (pos/position 5 6)) "Should be {:x 5 :y 6}"))

  (testing "Robot Walk Leftward!"
    (is (= (:position (:robot
                       (grd/find-grid-by-id (play/robot-walk! "nugame02" :leftward) "nugame02")))
           (pos/position 4 6)) "Should be {:x 4 :y 6}"))

  (testing "Robot Walk Backward!"
    (is (= (:position (:robot
                       (grd/find-grid-by-id (play/robot-walk! "nugame02" :backward) "nugame02")))
           (pos/position 4 5)) "Should be {:x 4 :y 5}"))

  (testing "Robot Walk Rightward!"
    (is (= (:position (:robot
                       (grd/find-grid-by-id (play/robot-walk! "nugame02" :rightward) "nugame02")))
           (pos/position 5 5)) "Should be {:x 5 :y 5}"))

  (testing "Robot Attack!"
    (play/create-grid! "nugame03")
    (play/create-robot! "nugame03" "wall-e" 5 5 :down)
    (play/add-dino! "nugame03" 5 6)
    (play/add-dino! "nugame03" 5 4)
    (play/add-dino! "nugame03" 6 5)
    (play/add-dino! "nugame03" 4 5)
    (play/add-dino! "nugame03" 1 1)
    (play/prepare-game! "nugame03")
    (play/start-game! "nugame03")
    (is  (and (= (count
                  (:dinos (grd/find-grid-by-id
                           (play/robot-attack! "nugame03")
                           "nugame03")))
                 1)
              (din/contains-dino? (:dinos (play/get-grid "nugame03"))
                                  (pos/position 1 1)))))

  (testing "Stopping the Game"
    (is (= (:status (grd/find-grid-by-id (play/stop-game! "nugame03") "nugame03")) :finished)
        "Should be finished!")))