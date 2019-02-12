(ns clojwar.core.dino-test
  (:require [clojure.test :refer [deftest testing is]]
            [clojwar.core.dino :as dino]
            [clojwar.core.position :as pos]))

(deftest generate-a-dino
  (testing "Create a dinosaur with params"
    (is (= (frequencies (dino/dino (pos/->Position 10 20) :up))
           (frequencies '{:position #clojwar.core.position.Position{:x 10 :y 20} 
                          :facing :up}))))

  (testing "Create a dino with all params are numbers."
    (is (thrown? AssertionError (dino/dino 1 1)) "Should be an assertion error!"))

  (testing "Create a dino with all params are lists."
    (is (thrown? AssertionError (dino/dino '(1) '(1)) 
                 "Should be an assertion error!")))

  (testing "Create a dino with facing position wrong."
    (is (thrown? AssertionError (dino/dino {:x 10 :y 10} :edge) 
                 "Should be an assertion error!")))

  (testing "Create a dino with negative numbers position."
    (is (thrown? AssertionError (dino/dino {:x -10 :y -10} :up) 
                 "Should be an assertion error!"))))

(deftest remove-a-dino
  (testing "Remove a dino from a dino set."
    (is (= (frequencies (dino/remove-dino (hash-set
                                           (dino/->Dino (pos/->Position 0 0) :up)
                                           (dino/->Dino (pos/->Position 1 1) :up))
                                          (pos/->Position 1 1)))
           (frequencies (hash-set
                         (dino/->Dino (pos/->Position 0 0) :up))))))

  (testing "Remove a dino with all params are numbers."
    (is (thrown? AssertionError (dino/remove-dino 1 1)) "Should be an assertion error!"))

  (testing "Remove a dino with all params are lists."
    (is (thrown? AssertionError (dino/remove-dino '(1) '(1))
                 "Should be an assertion error!"))))

(deftest remove-dinos-by-position-list
  (testing "Remove dinos by position list"
    (is (= (frequencies (dino/remove-dinos-by-position (hash-set
                                                        (dino/->Dino (pos/->Position 5 6) :up)
                                                        (dino/->Dino (pos/->Position 5 4) :up)
                                                        (dino/->Dino (pos/->Position 6 5) :up)
                                                        (dino/->Dino (pos/->Position 4 5) :up)
                                                        (dino/->Dino (pos/->Position 3 2) :up))
                                                       (list (pos/->Position 5 6)
                                                             (pos/->Position 5 4)
                                                             (pos/->Position 6 5)
                                                             (pos/->Position 4 5))))
           (frequencies (hash-set
                         (dino/->Dino (pos/->Position 3 2) :up))))))

  (testing "Remove a dino with all params are numbers."
    (is (thrown? AssertionError (dino/remove-dino 1 1)) "Should be an assertion error!"))

  (testing "Remove a dino with all params are lists."
    (is (thrown? AssertionError (dino/remove-dino '(1) '(1))
                 "Should be an assertion error!"))))

(deftest get-a-dino
  (testing "Get a dino from a dino set."
    (is (= (frequencies (dino/get-dino (hash-set
                                           (dino/->Dino (pos/->Position 0 0) :up)
                                           (dino/->Dino (pos/->Position 1 1) :up))
                                          (pos/->Position 1 1)))
           (frequencies (hash-set
                         (dino/->Dino (pos/->Position 1 1) :up))))))

  (testing "Create a dino with all params are numbers."
    (is (thrown? AssertionError (dino/get-dino 1 1)) "Should be an assertion error!"))

  (testing "Create a dino with all params are lists."
    (is (thrown? AssertionError (dino/get-dino '(1) '(1))
                 "Should be an assertion error!"))))

(deftest put-a-dino
  (testing "Put a dino from a dino set."
    (is (= (frequencies (dino/put-dino (hash-set
                                        (dino/->Dino (pos/->Position 0 0) :up)
                                        (dino/->Dino (pos/->Position 1 1) :up))
                                       (dino/->Dino (pos/->Position 3 3) :up)))
           (frequencies (hash-set
                         (dino/->Dino (pos/->Position 1 1) :up)
                         (dino/->Dino (pos/->Position 0 0) :up)
                         (dino/->Dino (pos/->Position 3 3) :up))))))

  (testing "Create a dino with all params are numbers."
    (is (thrown? AssertionError (dino/put-dino 1 1)) "Should be an assertion error!"))

  (testing "Create a dino with all params are lists."
    (is (thrown? AssertionError (dino/put-dino '(1) '(1))
                 "Should be an assertion error!"))))

(deftest contains-a-dino
  (testing "Check if a dino is into a dino set."
    (is (true? (dino/contains-dino? (hash-set
                                     (dino/->Dino (pos/->Position 0 0) :up)
                                     (dino/->Dino (pos/->Position 1 1) :up)
                                     (dino/->Dino (pos/->Position 3 3) :up))
                                    (pos/->Position 3 3)))))

  (testing "Check if a dino is not into a dino set."
    (is (nil? (dino/contains-dino? (hash-set
                                    (dino/->Dino (pos/->Position 0 0) :up)
                                    (dino/->Dino (pos/->Position 1 1) :up)
                                    (dino/->Dino (pos/->Position 3 3) :up))
                                   (pos/->Position 10 3)))))

  (testing "Create a dino with all params are numbers."
    (is (thrown? AssertionError (dino/contains-dino? 1 1)) "Should be an assertion error!"))

  (testing "Create a dino with all params are lists."
    (is (thrown? AssertionError (dino/contains-dino? '(1) '(1))
                 "Should be an assertion error!"))))