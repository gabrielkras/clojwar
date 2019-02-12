(ns clojwar.api.middleware.responses-test
  (:require [clojure.test :refer [deftest testing is]]
            [clojwar.api.middleware.responses :as res]
            [clojure.data.json :as json]))

(deftest wrap-200-response
  (testing "Generate HTTP 200 status code response with only data"
    (is (= (res/wrap-200-response {:field "value"})
           {:status 200
            :headers {"Content-Type" "application/json"}
            :body (json/write-str {:type "ok"
                                   :title "Operation Successful"
                                   :status 200
                                   :message "Successful"
                                   :data {:field "value"}})})))
  
  (testing "Generate HTTP 200 status code response with data and message"
    (is (= (res/wrap-200-response {:field "value"} "This is a message")
           {:status 200
            :headers {"Content-Type" "application/json"}
            :body (json/write-str {:type "ok"
                                   :title "Operation Successful"
                                   :status 200
                                   :message "This is a message"
                                   :data {:field "value"}})})))
  
  (testing "Generate HTTP 200 status code response with data, message and title"
    (is (= (res/wrap-200-response {:field "value"} "This is a title" "This is a message")
           {:status 200
            :headers {"Content-Type" "application/json"}
            :body (json/write-str {:type "ok"
                                   :title "This is a title"
                                   :status 200
                                   :message "This is a message"
                                   :data {:field "value"}})}))))

(deftest wrap-201-response
  (testing "Generate HTTP 201 status code response with only data"
    (is (= (res/wrap-201-response {:field "value"})
           {:status 201
            :headers {"Content-Type" "application/json"}
            :body (json/write-str {:type "created"
                                   :title "Created"
                                   :status 201
                                   :message "Resource created successfully"
                                   :data {:field "value"}})})))

  (testing "Generate HTTP 201 status code response with data and message"
    (is (= (res/wrap-201-response {:field "value"} "This is a message")
           {:status 201
            :headers {"Content-Type" "application/json"}
            :body (json/write-str {:type "created"
                                   :title "Created"
                                   :status 201
                                   :message "This is a message"
                                   :data {:field "value"}})})))

  (testing "Generate HTTP 201 status code response with data, message and title"
    (is (= (res/wrap-201-response {:field "value"} "This is a title" "This is a message")
           {:status 201
            :headers {"Content-Type" "application/json"}
            :body (json/write-str {:type "created"
                                   :title "This is a title"
                                   :status 201
                                   :message "This is a message"
                                   :data {:field "value"}})}))))

(deftest wrap-400-response
  (testing "Generate HTTP 400 status code response with only data"
    (is (= (res/wrap-400-response {:field "value"})
           {:status 400
            :headers {"Content-Type" "application/json"}
            :body (json/write-str {:type "bad request"
                                   :title "Bad Request"
                                   :status 400
                                   :message "Validation Error"
                                   :data {:field "value"}})})))

  (testing "Generate HTTP 400 status code response with data and message"
    (is (= (res/wrap-400-response {:field "value"} "This is a message")
           {:status 400
            :headers {"Content-Type" "application/json"}
            :body (json/write-str {:type "bad request"
                                   :title "Bad Request"
                                   :status 400
                                   :message "This is a message"
                                   :data {:field "value"}})})))

  (testing "Generate HTTP 400 status code response with data, message and title"
    (is (= (res/wrap-400-response {:field "value"} "This is a title" "This is a message")
           {:status 400
            :headers {"Content-Type" "application/json"}
            :body (json/write-str {:type "bad request"
                                   :title "This is a title"
                                   :status 400
                                   :message "This is a message"
                                   :data {:field "value"}})}))))

(deftest wrap-404-response
  (testing "Generate HTTP 404 status code response with only data"
    (is (= (res/wrap-404-response {:field "value"})
           {:status 404
            :headers {"Content-Type" "application/json"}
            :body (json/write-str {:type "not found"
                                   :title "Not Found"
                                   :status 404
                                   :message "The resource was not found"
                                   :data {:field "value"}})})))

  (testing "Generate HTTP 404 status code response with data and message"
    (is (= (res/wrap-404-response {:field "value"} "This is a message")
           {:status 404
            :headers {"Content-Type" "application/json"}
            :body (json/write-str {:type "not found"
                                   :title "Not Found"
                                   :status 404
                                   :message "This is a message"
                                   :data {:field "value"}})})))

  (testing "Generate HTTP 404 status code response with data, message and title"
    (is (= (res/wrap-404-response {:field "value"} "This is a title" "This is a message")
           {:status 404
            :headers {"Content-Type" "application/json"}
            :body (json/write-str {:type "not found"
                                   :title "This is a title"
                                   :status 404
                                   :message "This is a message"
                                   :data {:field "value"}})}))))

(deftest wrap-405-response
  (testing "Generate HTTP 405 status code response with only data"
    (is (= (res/wrap-405-response {:field "value"})
           {:status 405
            :headers {"Content-Type" "application/json"}
            :body (json/write-str {:type "method not allowed"
                                   :title "Method not allowed"
                                   :status 405
                                   :message "This HTTP method is not allowed"
                                   :data {:field "value"}})})))

  (testing "Generate HTTP 405 status code response with data and message"
    (is (= (res/wrap-405-response {:field "value"} "This is a message")
           {:status 405
            :headers {"Content-Type" "application/json"}
            :body (json/write-str {:type "method not allowed"
                                   :title "Method not allowed"
                                   :status 405
                                   :message "This is a message"
                                   :data {:field "value"}})})))

  (testing "Generate HTTP 405 status code response with data, message and title"
    (is (= (res/wrap-405-response {:field "value"} "This is a title" "This is a message")
           {:status 405
            :headers {"Content-Type" "application/json"}
            :body (json/write-str {:type "method not allowed"
                                   :title "This is a title"
                                   :status 405
                                   :message "This is a message"
                                   :data {:field "value"}})}))))

(deftest wrap-409-response
  (testing "Generate HTTP 409 status code response with only data"
    (is (= (res/wrap-409-response {:field "value"})
           {:status 409
            :headers {"Content-Type" "application/json"}
            :body (json/write-str {:type "conflict"
                                   :title "A conflict was found"
                                   :status 409
                                   :message "Something is wrong! Try again"
                                   :data {:field "value"}})})))

  (testing "Generate HTTP 409 status code response with data and message"
    (is (= (res/wrap-409-response {:field "value"} "This is a message")
           {:status 409
            :headers {"Content-Type" "application/json"}
            :body (json/write-str {:type "conflict"
                                   :title "A conflict was found"
                                   :status 409
                                   :message "This is a message"
                                   :data {:field "value"}})})))

  (testing "Generate HTTP 409 status code response with data, message and title"
    (is (= (res/wrap-409-response {:field "value"} "This is a title" "This is a message")
           {:status 409
            :headers {"Content-Type" "application/json"}
            :body (json/write-str {:type "conflict"
                                   :title "This is a title"
                                   :status 409
                                   :message "This is a message"
                                   :data {:field "value"}})}))))

(deftest wrap-500-response
  (testing "Generate HTTP 500 status code response with only data"
    (is (= (res/wrap-500-response {:field "value"})
           {:status 500
            :headers {"Content-Type" "application/json"}
            :body (json/write-str {:type "internal server error"
                                   :title "Internal server error"
                                   :status 500
                                   :message "D'oh! We are having some troubles here!"
                                   :data {:field "value"}})})))

  (testing "Generate HTTP 500 status code response with data and message"
    (is (= (res/wrap-500-response {:field "value"} "This is a message")
           {:status 500
            :headers {"Content-Type" "application/json"}
            :body (json/write-str {:type "internal server error"
                                   :title "Internal server error"
                                   :status 500
                                   :message "This is a message"
                                   :data {:field "value"}})})))

  (testing "Generate HTTP 500 status code response with data, message and title"
    (is (= (res/wrap-500-response {:field "value"} "This is a title" "This is a message")
           {:status 500
            :headers {"Content-Type" "application/json"}
            :body (json/write-str {:type "internal server error"
                                   :title "This is a title"
                                   :status 500
                                   :message "This is a message"
                                   :data {:field "value"}})}))))

(deftest wrap-501-response
  (testing "Generate HTTP 501 status code response with only data"
    (is (= (res/wrap-501-response {:field "value"})
           {:status 501
            :headers {"Content-Type" "application/json"}
            :body (json/write-str {:type "not implemented"
                                   :title "Not implemented"
                                   :status 501
                                   :message "Sorry, this resource doesn't work yet! 
 Maybe some day?"
                                   :data {:field "value"}})})))

  (testing "Generate HTTP 501 status code response with data and message"
    (is (= (res/wrap-501-response {:field "value"} "This is a message")
           {:status 501
            :headers {"Content-Type" "application/json"}
            :body (json/write-str {:type "not implemented"
                                   :title "Not implemented"
                                   :status 501
                                   :message "This is a message"
                                   :data {:field "value"}})})))

  (testing "Generate HTTP 501 status code response with data, message and title"
    (is (= (res/wrap-501-response {:field "value"} "This is a title" "This is a message")
           {:status 501
            :headers {"Content-Type" "application/json"}
            :body (json/write-str {:type "not implemented"
                                   :title "This is a title"
                                   :status 501
                                   :message "This is a message"
                                   :data {:field "value"}})}))))