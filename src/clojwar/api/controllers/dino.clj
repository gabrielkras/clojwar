(ns clojwar.api.controllers.dino
  (:require [clojwar.game :as play]
            [clojwar.api.middleware.responses :as res]
            [clojwar.api.middleware.validator :as vld]))

(defn get-dino-handler
  "This function handles the GET method of dinos, returning the list of dinos."
  [grid-id]
  (let [grid (play/get-grid grid-id)]
    (if (empty? grid)
      (res/wrap-404-response {:grid-id grid-id} "There are no registered grid with this name")
      (if (empty? (:dinos grid))
        (res/wrap-404-response {:grid grid} "This grid does not contains dinosaurs.")
        (res/wrap-200-response {:dinos (:dinos grid)})))))

(defn post-dino-handler
  "This function handles the POST method of dinos, creating a new dino resource."
  [body grid-id]
  (let [grid (play/get-grid grid-id)]
    (if (empty? grid)
      (res/wrap-404-response {:grid-id grid-id} "There are no registered grid with this name")
      (if (empty? (vld/dino-body-validator body grid-id))
        (res/wrap-400-response {:body body} "Invalid request body! 
 Take a look on the docs for more details")
        (try
          (if (empty? (play/add-dino! grid-id
                                      (:dino-pos-x body)
                                      (:dino-pos-y body)))
            (res/wrap-409-response {:dinos grid :request-body body}
                                   "There already exists a dinosaur in this position, 
 try another one")
            (res/wrap-201-response (play/get-grid grid-id)))
          (catch AssertionError e
                        (res/wrap-400-response grid 
                                               "You can't create a dinosaur outside of grid area!")))))))

;; TODO
(defn delete-dino-handler
  "This function handles the DELETE method of dinos deleting them all. 
 NOT IMPLEMENTED YET"
  [body grid-id]
  (res/wrap-501-response {:grid-id grid-id}))