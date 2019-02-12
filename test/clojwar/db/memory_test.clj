(ns clojwar.db.memory-test
  (:require [clojure.test :refer [deftest testing is]]
            [clojwar.protocols.grid :refer [get-grids
                                          put-grid!
                                          update-grid!
                                          get-grid-by-id
                                          delete-grid!]]
            [clojwar.db.memory :refer (->AtomStore)]
            [clojwar.core.grid :as grid]
            [clojwar.core.robot :as rbt]
            [clojwar.core.position :as pos]
            [clojwar.core.dino :as din]))

(def storage (->AtomStore (atom #{})))

(deftest testing-memory-storage
  (testing "Put one grid into the storage"
    (is (= (frequencies (put-grid! storage (grid/grid "game01" :created)))
           (frequencies (hash-set (grid/grid "game01" :created))))))

  (testing "Put another grid into the storage"
    (is (= (frequencies (put-grid! storage (grid/grid "game02" :created)))
           (frequencies (hash-set (grid/grid "game01" :created)
                                  (grid/grid "game02" :created))))))

  (testing "Put another grid into the storage"
    (is (= (frequencies (put-grid! storage (grid/grid "game03" :created)))
           (frequencies (hash-set (grid/grid "game01" :created)
                                  (grid/grid "game02" :created)
                                  (grid/grid "game03" :created))))))

  (testing "Getting grids"
    (is (= (frequencies (get-grids storage))
           (frequencies (hash-set (grid/grid "game01" :created)
                                  (grid/grid "game02" :created)
                                  (grid/grid "game03" :created))))))

  (testing "Update grid game02."
    (is (= (frequencies (update-grid! storage (grid/grid
                                               "game02"
                                               :finished
                                               (rbt/robot "wall-e" (pos/position 10 10) :up)
                                               (din/put-dino #{} (din/dino (pos/position 11 11) :down)))))
           (frequencies (hash-set (grid/grid "game01" :created)
                                  (grid/grid "game02"
                                             :finished
                                             (rbt/robot "wall-e" (pos/position 10 10) :up)
                                             (din/put-dino #{} (din/dino (pos/position 11 11) :down)))
                                  (grid/grid "game03" :created))))))

  (testing "Get grid by Id"
    (is (= (frequencies (get-grid-by-id storage "game02"))
           (frequencies (grid/grid "game02"
                                   :finished
                                   (rbt/robot "wall-e" (pos/position 10 10) :up)
                                   (din/put-dino #{} (din/dino (pos/position 11 11) :down)))))))

  (testing "Delete grid game02."
    (is (= (frequencies (delete-grid! storage (grid/grid
                                               "game02"
                                               :finished
                                               (rbt/robot "wall-e" (pos/position 10 10) :up)
                                               (din/put-dino #{} (din/dino (pos/position 11 11) :down)))))
           (frequencies (hash-set (grid/grid "game01" :created)
                                  (grid/grid "game03" :created)))))))