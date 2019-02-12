(ns clojwar.api.controllers.robot-test
  (:require [clojure.test :refer [deftest testing is]]
            [clojwar.api.controllers.robot :as rbt]
            [clojwar.api.middleware.responses :as res]
            [clojwar.game :as play]))

(deftest get-robot-handler
  (testing "Test get robot without a grid"
    (is (= (frequencies (rbt/get-robot-handler "clojwar011"))
           (frequencies (res/wrap-404-response {:grid-id "clojwar011"}
                                               "There are no registered grid with this name")))))
  
  (testing "Test get robot without a robot"
    (play/create-grid! "clojwar011")
    (is (= (frequencies (rbt/get-robot-handler "clojwar011"))
           (frequencies (res/wrap-404-response {:grid (play/get-grid "clojwar011")}
                                               "This grid does not contains a robot.")))))
  
  (testing "Test get robot correctly"
    (play/create-robot! "clojwar011" "wall-e" 10 10 :up)
    (is (= (frequencies (rbt/get-robot-handler "clojwar011"))
           (frequencies (res/wrap-200-response {:robot (:robot (play/get-grid "clojwar011"))}))))))

(deftest post-robot-handler
  (testing "Test post robot without a grid"
    (is (= (frequencies (rbt/post-robot-handler {} "clojwar012"))
           (frequencies (res/wrap-404-response {:grid-id "clojwar012"}
                                               "There are no registered grid with this name")))))
  
  (testing "Test post robot with wrong body"
    (is (= (frequencies (rbt/post-robot-handler {:grid-id "clojwar011" :action "walk"} "clojwar011"))
           (frequencies (res/wrap-400-response {:body {:grid-id "clojwar011" :action "walk"}}
                                               "Invalid request body! 
 Take a look on the docs for more details")))))
  
  (testing "Test post robot with an existent robot in grid"
    (is (= (frequencies (rbt/post-robot-handler {:grid-id "clojwar011"
                                                 :name "wall-e2" 
                                                 :robot-pos-x 1 
                                                 :robot-pos-y 2
                                                 :facing-direction "up"}
                                                "clojwar011"))
           (frequencies (res/wrap-409-response {:robot (:robot (play/get-grid "clojwar011"))}
                                               "There already exists a robot on 
 this grid or you trying to create a robot in the same existent dinosaur position")))))
  
  (testing "Test post robot - try to create robot outside of grid"
    (play/create-grid! "clojwar012")
    (is (= (frequencies (rbt/post-robot-handler {:grid-id "clojwar012"
                                                 :name "wall-e2"
                                                 :robot-pos-x 200
                                                 :robot-pos-y 2
                                                 :facing-direction "up"}
                                                "clojwar012"))
           (frequencies (res/wrap-400-response (play/get-grid "clojwar012")
                                               "You can't create a robot outside of grid area!")))))

 (testing "Test post robot - create robot"
   (play/create-grid! "clojwar013")
   (is (= (frequencies (rbt/post-robot-handler {:grid-id "clojwar013"
                                                :name "wall-e2"
                                                :robot-pos-x 2
                                                :robot-pos-y 2
                                                :facing-direction "up"}
                                               "clojwar013"))
          (frequencies (res/wrap-201-response (play/get-grid "clojwar013")))))))

(deftest delete-robot-handler
  (testing "Test delete robot without a grid"
    (is (= (frequencies (rbt/delete-robot-handler "clojwar014"))
           (frequencies (res/wrap-404-response {:grid-id "clojwar014"}
                                               "There are no registered grid with this name")))))
  
  (testing "Test delete robot without a robot into the grid"
    (play/create-grid! "clojwar014")
    (is (= (frequencies (rbt/delete-robot-handler "clojwar014"))
           (frequencies (res/wrap-404-response {:grid (play/get-grid "clojwar014")}
                                               "There is no robot into this grid!")))))
  
  (testing "Test delete robot"
    (play/create-robot! "clojwar014" "wall-e" 10 10 :up)
    (is (= (frequencies (rbt/delete-robot-handler "clojwar014"))
           (frequencies (res/wrap-200-response (play/get-grid "clojwar014")))))))

(deftest put-robot-handler
  (testing "Test put robot without a grid"
    (is (= (frequencies (rbt/put-robot-handler {} "clojwar015"))
           (frequencies (res/wrap-404-response {:grid-id "clojwar015"}
                                               "There are no registered grid with this name")))))
  
  (testing "Test put robot with an invalid body"
    (play/create-grid! "clojwar015")
    (is (= (frequencies (rbt/put-robot-handler {} "clojwar015"))
           (frequencies (res/wrap-400-response {:body {}}
                                               "Invalid request body! 
 Take a look on the docs for more details")))))
  
  (testing "Test walking with the robot without direction"
    (play/create-robot! "clojwar015" "wall-e" 10 10 :up)
    (is (= (frequencies (rbt/put-robot-handler {:grid-id "clojwar015" :action "walk"} "clojwar015"))
           (frequencies (res/wrap-400-response {:body {:grid-id "clojwar015" :action "walk"}}
                                               "Invalid request body! 
 Take a look on the docs for more details")))))
  
  (testing "Test walking with the robot with direction but not prepared yet!"
    (is (= (frequencies (rbt/put-robot-handler {:grid-id "clojwar015" 
                                                :action "walk" 
                                                :value "forward"} "clojwar015"))
           (frequencies (res/wrap-409-response (play/get-grid "clojwar015")
                                               "Sorry, but you need to start the game first 
 before pursuing with this operation")))))
  
  (testing "Test walking with the robot with direction but not stated yet!"
    (play/prepare-game! "clojwar015")
    (is (= (frequencies (rbt/put-robot-handler {:grid-id "clojwar015"
                                                :action "walk"
                                                :value "forward"} "clojwar015"))
           (frequencies (res/wrap-409-response (play/get-grid "clojwar015")
                                               "Sorry, but you need to start the game first 
 before pursuing with this operation")))))
  
  (testing "Test walking with the robot with direction but have an entity collision"
    (play/add-dino! "clojwar015" 10 11)
    (play/prepare-game! "clojwar015")
    (play/start-game! "clojwar015")
    (is (= (frequencies (rbt/put-robot-handler {:grid-id "clojwar015"
                                                :action "walk"
                                                :value "forward"} "clojwar015"))
           (frequencies (res/wrap-409-response (play/get-grid "clojwar015")
                                               "Sorry, entity collision detected! 
 try to walk in another direction")))))
  
  (testing "Test walking with the robot"
    (is (= (frequencies (rbt/put-robot-handler {:grid-id "clojwar015"
                                                :action "walk"
                                                :value "backward"} "clojwar015"))
           (frequencies (res/wrap-201-response (play/get-grid "clojwar015"))))))
  
  (testing "Test the robot attack but none dino has died"
    (is (= (frequencies (rbt/put-robot-handler {:grid-id "clojwar015"
                                                :action "attack"} "clojwar015"))
           (frequencies (res/wrap-200-response (play/get-grid "clojwar015")
                                               "Sorry, you not killed any dinosaur")))))

  (testing "Test the robot attack and killed a dino"
    (play/robot-walk! "clojwar015" :forward)
    (is (= (frequencies (rbt/put-robot-handler {:grid-id "clojwar015"
                                                :action "attack"} "clojwar015"))
           (frequencies (res/wrap-201-response (play/get-grid "clojwar015")
                                               "Congrats you killed some dinosaur")))))
  
  (testing "Try to walk across the grid limits"
    (play/create-grid! "clojwar016")
    (play/create-robot! "clojwar016" "wall-e3" 0 0 :left)
    (play/add-dino! "clojwar016" 10 10)
    (play/prepare-game! "clojwar016")
    (play/start-game! "clojwar016")
    (is (= (frequencies (rbt/put-robot-handler {:grid-id "clojwar016"
                                                :action "walk"
                                                :value "leftward"} "clojwar016"))
           (frequencies (res/wrap-409-response {:grid-id "clojwar016"
                                                :action "walk"
                                                :value "leftward"}
                                               "Sorry, but you can't walk 
 further on that direction"))))))