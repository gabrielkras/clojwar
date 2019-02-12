(ns clojwar.protocols.grid)

(defprotocol GridStore
  "This protocol is responsible for manages the storage of Grid objects."
  (get-grids [store]
    "This function returns all grids from the storage.")
  (put-grid! [store grid]
    "This function insert a grid into the storage.")
  (update-grid! [store grid]
    "This function update the current grid into the storage.")
  (get-grid-by-id [store id]
    "This function returns a grid based on this id.")
  (delete-grid! [store grid]
    "This function deletes a grid from the storage.")
  (reset-storage! [store]
    "This function reset the content of storage."))