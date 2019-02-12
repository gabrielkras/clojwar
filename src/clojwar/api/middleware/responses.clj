(ns clojwar.api.middleware.responses
  (:require [clojure.data.json :as json]))

(def status-code-types {:200 "ok"
                        :201 "created"
                        :400 "bad request"
                        :404 "not found"
                        :405 "method not allowed"
                        :409 "conflict"
                        :500 "internal server error"
                        :501 "not implemented"})

(defn- generate-response
  "This function creates an API body response"
  [status title message data]
  {:pre [(pos-int? status) (string? title) (string? message)]}
  {:status status
   :headers {"Content-Type" "application/json"}
   :body (json/write-str {:type ((-> status str keyword) status-code-types)
                          :title title
                          :status status
                          :message message
                          :data data})})

(defn wrap-200-response
  ([data] (wrap-200-response data "Operation Successful" "Successful"))
  ([data message] (wrap-200-response data "Operation Successful" message))
  ([data title message]
   "This wrapper creates a 200 status code response"
   (generate-response 200 title message data)))

(defn wrap-201-response
  ([data] (wrap-201-response data "Created" "Resource created successfully"))
  ([data message] (wrap-201-response data "Created" message))
  ([data title message]
   "This wrapper creates a 201 status code response"
   (generate-response 201 title message data)))

(defn wrap-400-response
  ([data] (wrap-400-response data "Bad Request" "Validation Error"))
  ([data message] (wrap-400-response data "Bad Request" message))
  ([data title message]
   "This wrapper creates a 400 status code response"
   (generate-response 400 title message data)))

(defn wrap-404-response
  ([data] (wrap-404-response data "Not Found" "The resource was not found"))
  ([data message] (wrap-404-response data "Not Found" message))
  ([data title message]
   "This wrapper creates a 404 status code response"
   (generate-response 404 title message data)))

(defn wrap-405-response
  ([data] (wrap-405-response data "Method not allowed" "This HTTP method is not allowed"))
  ([data message] (wrap-405-response data "Method not allowed" message))
  ([data title message]
   "This wrapper creates a 405 status code response"
   (generate-response 405 title message data)))

(defn wrap-409-response
  ([data] (wrap-409-response data "A conflict was found" "Something is wrong! Try again"))
  ([data message] (wrap-409-response data "A conflict was found" message))
  ([data title message]
   "This wrapper creates a 409 status code response"
   (generate-response 409 title message data)))

(defn wrap-500-response
  ([data] (wrap-500-response data "Internal server error" "D'oh! We are having some troubles here!"))
  ([data message] (wrap-500-response data "Internal server error" message))
  ([data title message]
   "This wrapper creates a 500 status code response"
   (generate-response 500 title message data)))

(defn wrap-501-response
  ([data] (wrap-501-response data "Not implemented" "Sorry, this resource doesn't work yet! 
 Maybe some day?"))
  ([data message] (wrap-501-response data "Not implemented" message))
  ([data title message]
   "This wrapper creates a 501 status code response"
   (generate-response 501 title message data)))