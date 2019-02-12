(ns clojwar.api.middleware.validator)

;; Example Request Body
;; {"name":"some-name"}
(defn grid-body-validator
  "This function validates the body request of grid resource. If the body
is valid, then the function will return it. Otherwise the function will return nil."
  [body]
  {:pre [(map? body)]}
  (when (and (contains? body :name) (string? (:name body)))
    body))

;; Example Request Body
;; {"grid-id":"some-value",  "action":"start"}
(def grid-actions-list '("prepare" "start" "stop"))

(defn grid-body-actions-validator
  "This function validates the body request of grid actions. If the body
is valid, then the function will return it. Otherwise the function will return nil."
  [body grid-id]
  {:pre [(map? body) (string? grid-id)]}
  (when (and (and (contains? body :grid-id)
                  (string? (:grid-id body))
                  (= grid-id (:grid-id body)))
             (and (contains? body :action)
                  (string? (:action body))
                  (some (partial = (:action body)) grid-actions-list)))
    body))

;; Example Request Body
;; {"grid-id":"some-value", "dino-pos-x": number, "dino-pos-y": number}
(defn dino-body-validator
  "This function validates the body request of dino resource. If the body
is valid, then the function will return it. Otherwise the function will return nil."
  [body grid-id]
  {:pre [(map? body) (string? grid-id)]}
  (when (and (and (contains? body :grid-id) 
                  (string? (:grid-id body)) 
                  (= grid-id (:grid-id body)))
             (and (contains? body :dino-pos-x) (not (neg-int? (:dino-pos-x body))))
             (and (contains? body :dino-pos-y) (not (neg-int? (:dino-pos-y body)))))
             body))

;; Example Request Body
;; {"grid-id":"some-value",  "name":"some-value", "robot-pos-x": number, "robot-pos-y": number,
;; "facing-direction": "some-facing"}
(defn robot-body-validator
  "This function validates the body request of robot resource. If the body
is valid, then the function will return it. Otherwise the function will return nil."
  [body grid-id]
  {:pre [(map? body) (string? grid-id)]}
  (when (and (and (contains? body :grid-id)
                  (string? (:grid-id body))
                  (= grid-id (:grid-id body)))
             (and (contains? body :name) (string? (:name body)))
             (and (contains? body :facing-direction) (string? (:facing-direction body)))
             (and (contains? body :robot-pos-x) (not (neg-int? (:robot-pos-x body))))
             (and (contains? body :robot-pos-y) (not (neg-int? (:robot-pos-y body)))))
    body))

;; Example Request Body
;; {"grid-id":"some-value",  "action":"walk" , "value":"forward"}
;; {"grid-id":"some-value", "action":"attack"}
(def robot-actions-list '("attack" "walk"))

(defn robot-body-actions-validator
  "This function validates the body request of robot actions. If the body
is valid, then the function will return it. Otherwise the function will return nil."
  [body grid-id]
  {:pre [(map? body) (string? grid-id)]}
  (when (and (and (contains? body :grid-id)
                  (string? (:grid-id body))
                  (= grid-id (:grid-id body)))
             (and (contains? body :action) 
                  (string? (:action body))
                  (some (partial = (:action body)) robot-actions-list)))
    (if (= (:action body) "walk")
      (when (contains? body :value)
        body)
      body)))