(ns clojwar.api.controllers.dino-test
  (:require [clojure.test :refer [deftest testing is]]
            [clojwar.api.controllers.dino :as din]
            [clojwar.api.middleware.responses :as res]
            [clojwar.core.grid :as grd]
            [clojwar.game :as play]))

(deftest get-dino-handler
  (testing "Try to get dino without a grid"
    (is (= (frequencies
            (din/get-dino-handler "clojwar01"))
           (frequencies
            (res/wrap-404-response {:grid-id "clojwar01"}
                                   "There are no registered grid with this name")))))

  (testing "Try to get dino without a dino list"
    (play/create-grid! "clojwar001")
    (is (= (frequencies
            (din/get-dino-handler "clojwar001"))
           (frequencies
            (res/wrap-404-response {:grid (grd/grid "clojwar001" :created)}
                                   "This grid does not contains dinosaurs.")))))

  (testing "Try to get dino list"
    (play/create-grid! "clojwar0001")
    (play/add-dino! "clojwar0001" 10 10)
    (let [grid (play/get-grid "clojwar0001")]
      (is (= (frequencies
              (din/get-dino-handler "clojwar0001"))
             (frequencies
              (res/wrap-200-response {:dinos
                                      (:dinos
                                       (grd/grid "clojwar0001"
                                                 :created
                                                 {}
                                                 (:dinos grid)))})))))))

(deftest post-dino-handler
  (testing "Try to post dino without a grid"
    (is (= (frequencies
            (din/post-dino-handler {} "clojwar01"))
           (frequencies
            (res/wrap-404-response {:grid-id "clojwar01"}
                                   "There are no registered grid with this name")))))

  (testing "Try to post dino with an invalid body"
    (play/create-grid! "clojwar002")
    (is (= (frequencies
            (din/post-dino-handler {} "clojwar002"))
           (frequencies
            (res/wrap-400-response {:body {}}
                                   "Invalid request body! 
 Take a look on the docs for more details")))))

  (testing "Try to post dino with a valid body"
    (play/create-grid! "clojwar003")
    (is (= (frequencies
            (din/post-dino-handler {:dino-pos-x 10
                                    :dino-pos-y 10
                                    :grid-id "clojwar003"} "clojwar003"))
           (frequencies
            (res/wrap-201-response (play/get-grid "clojwar003"))))))

  (testing "Try to post dino with an invalid grid-id on body"
    (play/create-grid! "clojwar004")
    (is (= (frequencies
            (din/post-dino-handler {:dino-pos-x 10
                                    :dino-pos-y 10
                                    :grid-id "clojwar0"} "clojwar004"))
           (frequencies
            (res/wrap-400-response {:body {:dino-pos-x 10
                                           :dino-pos-y 10
                                           :grid-id "clojwar0"}} "Invalid request body! 
 Take a look on the docs for more details")))))

  (testing "Try to post dino with a valid body and in the same position"
    (play/create-grid! "clojwar003")
    (is (= (frequencies
            (din/post-dino-handler {:dino-pos-x 10
                                    :dino-pos-y 10
                                    :grid-id "clojwar003"} "clojwar003"))
           (frequencies
            (res/wrap-409-response {:dinos (play/get-grid "clojwar003")
                                    :request-body {:dino-pos-x 10
                                                   :dino-pos-y 10
                                                   :grid-id "clojwar003"}}
                                   "There already exists a dinosaur in this position, 
 try another one"))))))

;; TODO
(deftest delete-dino-handler
  (testing "Not implemented"
    (is (= (frequencies
            (din/delete-dino-handler {} ""))
           (frequencies
            (res/wrap-501-response {:grid-id ""}))))))