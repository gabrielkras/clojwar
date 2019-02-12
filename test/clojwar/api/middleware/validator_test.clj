(ns clojwar.api.middleware.validator-test
  (:require [clojure.test :refer [deftest testing is]]
            [clojwar.api.middleware.validator :as vld]))

(deftest grid-body-validator
  (testing "Passing the body as string"
    (is (thrown? AssertionError (vld/grid-body-validator "String"))
        "Should be an Assertion Error!"))

  (testing "Passing wrong key in the body"
    (is (nil? (vld/grid-body-validator {:game "clojwar"}))
        "Should be nil!"))

  (testing "Passing correct key in the body"
    (is (= (vld/grid-body-validator {:name "clojwar"})
           {:name "clojwar"})
        "Should be {:name \"clojwar\"}")))

(deftest grid-body-actions-validator
  (testing "Passing the body as string"
    (is (thrown? AssertionError (vld/grid-body-actions-validator "String" "a"))
        "Should be an Assertion Error!"))

  (testing "Passing wrong key in the body"
    (is (nil? (vld/grid-body-actions-validator {:game "clojwar"} "a"))
        "Should be nil!"))
  
  (testing "Passing incorrect action in the body"
    (is (nil? (vld/grid-body-actions-validator {:grid-id "clojwar" 
                                                :action "nu"} "clojwar"))
        "Should be nil!"))
  
  (testing "Passing different grid-id in the body"
    (is (nil? (vld/grid-body-actions-validator {:grid-id "nu"
                                                :action "start"} "clojwar"))
        "Should be nil!"))

  (testing "Passing correct key in the body"
    (is (= (vld/grid-body-actions-validator {:grid-id "a"
                                             :action "start"} "a")
           {:grid-id "a"
            :action "start"})
        "Should be {:grid-id \"a\" :action \"start\"}")))

(deftest dino-body-validator
  (testing "Passing the body as string"
    (is (thrown? AssertionError (vld/dino-body-validator "String" "a"))
        "Should be an Assertion Error!"))

  (testing "Passing wrong key in the body"
    (is (nil? (vld/dino-body-validator {:game "clojwar"} "a"))
        "Should be nil!"))

  (testing "Passing invalid number in the body - x-axis"
    (is (nil? (vld/dino-body-validator {:grid-id "clojwar"
                                        :dino-pos-x -200
                                        :dino-pos-y 10} "clojwar"))
        "Should be nil!"))

  (testing "Passing invalid number in the body - y-axis"
    (is (nil? (vld/dino-body-validator {:grid-id "clojwar"
                                        :dino-pos-x 10
                                        :dino-pos-y -210} "clojwar"))
        "Should be nil!"))

  (testing "Passing different grid-id in the body"
    (is (nil? (vld/dino-body-validator {:grid-id "nu"
                                        :dino-pos-x 10
                                        :dino-pos-y 10} "clojwar"))
        "Should be nil!"))

  (testing "Passing correct key in the body"
    (is (= (vld/dino-body-validator {:grid-id "nu"
                                     :dino-pos-x 10
                                     :dino-pos-y 10} "nu")
           {:grid-id "nu"
            :dino-pos-x 10
            :dino-pos-y 10})
        "Should be {:grid-id \"nu\" :dino-pos-x 10 :dino-pos-y 10}")))

(deftest robot-body-validator
  (testing "Passing the body as string"
    (is (thrown? AssertionError (vld/robot-body-validator "String" "a"))
        "Should be an Assertion Error!"))

  (testing "Passing wrong key in the body"
    (is (nil? (vld/robot-body-validator {:game "clojwar"} "a"))
        "Should be nil!"))

  (testing "Passing invalid number in the body - x-axis"
    (is (nil? (vld/robot-body-validator {:grid-id "clojwar"
                                         :name "wall-e"
                                         :dino-pos-x -200
                                         :dino-pos-y 10
                                         :facing "up"} "clojwar"))
        "Should be nil!"))

  (testing "Passing invalid number in the body - y-axis"
    (is (nil? (vld/robot-body-validator {:grid-id "clojwar"
                                         :name "wall-e"
                                         :dino-pos-x 10
                                         :dino-pos-y -10
                                         :facing "up"} "clojwar"))
        "Should be nil!"))

  (testing "Passing invalid name in the body"
    (is (nil? (vld/robot-body-validator {:grid-id "clojwar"
                                         :name {:some_name "this is a name"}
                                         :dino-pos-x 10
                                         :dino-pos-y 10
                                         :facing "up"} "clojwar"))
        "Should be nil!"))

  (testing "Passing invalid facing in the body"
    (is (nil? (vld/robot-body-validator {:grid-id "clojwar"
                                         :name "0100101"
                                         :dino-pos-x 10
                                         :dino-pos-y 10
                                         :facing "BUG"} "clojwar"))
        "Should be nil!"))

  (testing "Passing invalid facing in the body"
    (is (nil? (vld/robot-body-validator {:grid-id "nu"
                                         :name "010010101"
                                         :dino-pos-x 10
                                         :dino-pos-y 10
                                         :facing "up"} "clojwar"))
        "Should be nil!"))  


  (testing "Passing correct key in the body"
    (is (= (vld/dino-body-validator {:grid-id "nu"
                                     :name "010010101"
                                     :dino-pos-x 10
                                     :dino-pos-y 10
                                     :facing "up"} "nu")
           {:grid-id "nu"
            :name "010010101"
            :dino-pos-x 10
            :dino-pos-y 10
            :facing "up"})
        "Should return the body!")))

(deftest robot-body-actions-validator
  (testing "Passing the body as string"
    (is (thrown? AssertionError (vld/robot-body-actions-validator "String" "a"))
        "Should be an Assertion Error!"))

  (testing "Passing wrong key in the body"
    (is (nil? (vld/robot-body-actions-validator {:game "clojwar"} "a"))
        "Should be nil!"))

  (testing "Passing incorrect action in the body"
    (is (nil? (vld/robot-body-actions-validator {:grid-id "clojwar"
                                                 :action "nu"} "clojwar"))
        "Should be nil!"))

  (testing "Passing walk action without direction"
    (is (nil? (vld/robot-body-actions-validator {:grid-id "clojwar"
                                                 :action "walk"} "clojwar"))
        "Should be nil!"))

  (testing "Passing different grid-id in the body"
    (is (nil? (vld/robot-body-actions-validator {:grid-id "nu"
                                                 :action "attack"} "clojwar"))
        "Should be nil!"))

  (testing "Passing correct key in the body"
    (is (= (vld/robot-body-actions-validator {:grid-id "nu"
                                              :action "attack"} "nu")
           {:grid-id "nu"
            :action "attack"})
        "Should be {:grid-id \"nu\" :action \"attack\"}"))

  (testing "Passing walk action - forward"
    (is (= (vld/robot-body-actions-validator {:grid-id "nu"
                                              :action "walk"
                                              :value "forward"} "nu")
           {:grid-id "nu"
            :action "walk"
            :value "forward"})
        "Should be return the body"))

  (testing "Passing walk action - leftward"
    (is (= (vld/robot-body-actions-validator {:grid-id "nu"
                                              :action "walk"
                                              :value "leftward"} "nu")
           {:grid-id "nu"
            :action "walk"
            :value "leftward"})
        "Should be return the body"))

  (testing "Passing walk action - rightward"
    (is (= (vld/robot-body-actions-validator {:grid-id "nu"
                                              :action "walk"
                                              :value "rightward"} "nu")
           {:grid-id "nu"
            :action "walk"
            :value "rightward"})
        "Should be return the body"))

  (testing "Passing walk action - backward"
    (is (= (vld/robot-body-actions-validator {:grid-id "nu"
                                              :action "walk"
                                              :value "backward"} "nu")
           {:grid-id "nu"
            :action "walk"
            :value "backward"})
        "Should be return the body")))