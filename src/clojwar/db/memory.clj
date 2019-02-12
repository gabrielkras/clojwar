(ns clojwar.db.memory
  "This is an implementation of in-memory storage for the application"
  (:require [clojwar.protocols.grid :refer [GridStore
                                          get-grids
                                          put-grid!
                                          update-grid!
                                          get-grid-by-id
                                          delete-grid!
                                          reset-storage!]]
            [clojwar.core.grid :as g]))

;; MEMORY STORAGE IMPLEMENTATION ;;
(defrecord AtomStore [data]
  GridStore
  (get-grids [store]
    @(:data store))
  (put-grid! [store grid]
    (swap! (:data store) #(conj % grid)))
  (update-grid! [store grid]
    (let [new_grid (g/find-grid-by-id @(:data store) (:id grid))]
      (when-not (nil? new_grid)
        (swap! (:data store) #(set (conj
                                         (g/remove-grid % (:id grid))
                                         grid))))))
  (get-grid-by-id [store id]
    (g/find-grid-by-id @(:data store) id))
  (delete-grid! [store grid]
    (let [new_grid (g/find-grid-by-id @(:data store) (:id grid))]
      (when-not (nil? new_grid)
        (swap! (:data store) #(set (g/remove-grid % (:id grid)))))))
  (reset-storage! [store]
    (reset! (:data store) #{})))