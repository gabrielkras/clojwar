(ns clojwar.api.controllers.grid-test
  (:require [clojure.test :refer [deftest testing is]]
            [clojwar.api.controllers.grid :as grd]
            [clojwar.api.middleware.responses :as res]
            [clojwar.core.grid :as g]
            [clojwar.game :as play]))

(deftest get-grid-handler
  (testing "Try to get a non existent grid"
    (play/reset-store!) ; CLEAR MEMORY BEFORE TEST
    (is (= (frequencies
            (grd/get-grid-handler nil))
           (frequencies
            (res/wrap-404-response #{}
                                   "There are no registered grids")))))
  
  (testing "Try to get an existent grid"
    (play/create-grid! "clojwar06")
    (is (= (frequencies
            (grd/get-grid-handler nil))
           (frequencies
            (res/wrap-200-response (play/get-grid-all)))))))

(deftest get-grid-by-id-handler
  (testing "Try to get a non existent grid"
    (is (= (frequencies
            (grd/get-grid-by-id-handler "clojwar005"))
           (frequencies
            (res/wrap-404-response nil
                                   "There are no registered grids")))))

  (testing "Try to get an existent grid"
    (play/create-grid! "clojwar005")
    (is (= (frequencies
            (grd/get-grid-by-id-handler "clojwar005"))
           (frequencies
            (res/wrap-200-response (g/grid "clojwar005" :created)))))))

(deftest post-grid-handler
  (testing "Try to post a grid with invalid body"
    (is (= (frequencies
            (grd/post-grid-handler {:test "a"}))
           (frequencies
            (res/wrap-400-response {:test "a"}
                                   "Invalid request body! Take a look on the docs for more details")))))

  (testing "Try to post a grid with a valid body"
    (is (= (frequencies
            (grd/post-grid-handler {:name "clojwar007"}))
           (frequencies
            (res/wrap-201-response (play/get-grid "clojwar007")
                                   "The grid has been created successfully!")))))
  
  (testing "Try to re-post a grid with a valid body"
    (is (= (frequencies
            (grd/post-grid-handler {:name "clojwar007"}))
           (frequencies
            (res/wrap-409-response {:chosen-name "clojwar007"}
                                   "The chosen name for the grid already exists, please choose another."))))))

(deftest put-grid-handler
  (testing "Try to put a grid with invalid body"
    (is (= (frequencies
            (grd/put-grid-handler {} "clojwar005"))
           (frequencies
            (res/wrap-400-response {:body {}}
                                   "Invalid request body! 
 Take a look on the docs for more details")))))

  (testing "Try to put a grid with invalid body and non existent grid"
    (is (= (frequencies
            (grd/put-grid-handler {} "clojwar007"))
           (frequencies
            (res/wrap-404-response {:grid-id "clojwar007"}
                                   "There are no registered grid with this name")))))

  (testing "Try to put a grid with a valid body and non existent grid"
    (is (= (frequencies
            (grd/put-grid-handler {:grid-id "clojwar007"
                                   :action "prepare"} "clojwar007"))
           (frequencies
            (res/wrap-404-response {:grid-id "clojwar007"}
                                   "There are no registered grid with this name")))))

  (testing "Try to put a grid with a valid body a existent grid - PREPARE ACTION"
    (play/create-grid! "clojwar008")
    (is (= (frequencies
            (grd/put-grid-handler {:grid-id "clojwar008"
                                   :action "prepare"} "clojwar008"))
           (frequencies
            (res/wrap-409-response (play/get-grid "clojwar008")
                                   "Sorry, but you need to add at least 
 one robot and one dinosaur to prepare the game.")))))

  (testing "Try to put a grid with a valid body a existent grid - PREPARE ACTION - OK"
    (play/create-grid! "clojwar009")
    (play/add-dino! "clojwar009" 10 10)
    (play/create-robot! "clojwar009" "wall-e" 10 9 :up)
    (is (= (frequencies
            (grd/put-grid-handler {:grid-id "clojwar009"
                                   :action "prepare"} "clojwar009"))
           (frequencies
            (res/wrap-201-response (play/get-grid "clojwar009")
                                   "Congrats your game is ready to be started")))))

  (testing "Try to put a grid with a valid body a existent grid - START ACTION"
    (play/create-grid! "clojwar010")
    (play/add-dino! "clojwar010" 10 10)
    (play/create-robot! "clojwar010" "wall-e" 10 9 :up)
    (is (= (frequencies
            (grd/put-grid-handler {:grid-id "clojwar010"
                                   :action "start"} "clojwar010"))
           (frequencies
            (res/wrap-409-response (play/get-grid "clojwar010")
                                   "Sorry, but you need prepare the game before 
 started it")))))

  (testing "Try to put a grid with a valid body a existent grid - START ACTION -OK"
    (grd/put-grid-handler {:grid-id "clojwar010"
                           :action "prepare"} "clojwar010")
    (is (= (frequencies
            (grd/put-grid-handler {:grid-id "clojwar010"
                                   :action "start"} "clojwar010"))
           (frequencies
            (res/wrap-201-response (play/get-grid "clojwar010")
                                   "Congrats your game is started!")))))

  (testing "Try to put a grid with a valid body a existent grid - STOP ACTION"
    (is (= (frequencies
            (grd/put-grid-handler {:grid-id "clojwar009"
                                   :action "stop"} "clojwar009"))
           (frequencies
            (res/wrap-409-response (play/get-grid "clojwar009")
                                   "Sorry, but you can only stop the game 
 if it is in a running state!")))))

  (testing "Try to put a grid with a valid body a existent grid - STOP ACTION -OK"
    (is (= (frequencies
            (grd/put-grid-handler {:grid-id "clojwar010"
                                   :action "stop"} "clojwar010"))
           (frequencies
            (res/wrap-201-response (play/get-grid "clojwar010")
                                   "Congrats your game is stopped"))))))