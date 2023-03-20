(ns clean-clojure.adapter.http.handler-test
  (:require [clean-clojure.adapter.http.request :as request]
            [clean-clojure.system :as system]
            [clj-http.client :as client]
            [clj-time.coerce :as time.coerce]
            [clj-time.core :as time]
            [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [clojure.test :refer :all]
            [com.stuartsierra.component :as component])
  (:import (clojure.lang ExceptionInfo)))

(def ^:dynamic *system* nil)

(defn with-e2e-system
  [f]
  (let [system (component/start (system/start))]
    (binding [*system* system]
      (f))
    (component/stop system)))
(use-fixtures :once with-e2e-system)

(deftest create-test
  (testing "create responds with 201 with the created todo given valid request"
    (let [create-request (assoc (gen/generate (s/gen ::request/create-request))
                                :completed-at nil)
          body (cheshire.core/generate-string create-request)
          create (client/post "http://localhost:3000/api/v1/todo" {:body         body
                                                                   :content-type :json
                                                                   :accept       :json})
          create-response (cheshire.core/parse-string (:body create) true)]
      (is (= (:status create) 201))
      (is (= create-request (dissoc create-response :id :created-at))))))

(deftest read-test
  (testing "read responds with 200 with an existing todo given valid id"
    (let [create-request (assoc (gen/generate (s/gen ::request/create-request))
                                :completed-at nil)
          body (cheshire.core/generate-string create-request)
          create (client/post "http://localhost:3000/api/v1/todo" {:body         body
                                                                   :content-type :json
                                                                   :accept       :json})
          create-response (cheshire.core/parse-string (:body create) true)
          read (client/get
                 (format "http://localhost:3000/api/v1/todo/%s" (:id create-response))
                 {:content-type :json
                  :accept       :json})
          read-response (cheshire.core/parse-string (:body read) true)]
      (is (= (:status read) 200))
      (is (= create-response read-response))))

  (testing "read responds with 404 given id does not exist"
    (let [id (gen/generate (s/gen ::request/id))]
      (is (thrown-with-msg?
            ExceptionInfo #"clj-http: status 404"
            (client/get
              (format "http://localhost:3000/api/v1/todo/%s" id)
              {:content-type :json
               :accept       :json}))))))

(deftest update-test
  (testing "update responds with 200 with the updated todo given valid request"
    (let [create-request (assoc (gen/generate (s/gen ::request/create-request))
                                :completed-at nil)
          body (cheshire.core/generate-string create-request)
          create (client/post "http://localhost:3000/api/v1/todo" {:body         body
                                                                   :content-type :json
                                                                   :accept       :json})
          create-response (cheshire.core/parse-string (:body create) true)
          completed-at (time/now)
          update-request (assoc create-response :completed-at completed-at)
          body (cheshire.core/generate-string update-request)
          update (client/patch
                   "http://localhost:3000/api/v1/todo"
                   {:body         body
                    :content-type :json
                    :accept       :json})
          update-response (cheshire.core/parse-string (:body update) true)]
      (is (= (:status update) 200))
      (is (inst? (time.coerce/from-string (:completed-at update-response))))))

  (testing "update responds with 400 given invalid request"
    (let [create-request (assoc (gen/generate (s/gen ::request/create-request))
                                :completed-at nil)
          body (cheshire.core/generate-string create-request)
          create (client/post "http://localhost:3000/api/v1/todo" {:body         body
                                                                   :content-type :json
                                                                   :accept       :json})
          create-response (cheshire.core/parse-string (:body create) true)
          completed-at (time/minus (time/now) (time/days 1))
          update-request (assoc create-response :completed-at completed-at)
          body (cheshire.core/generate-string update-request)]
      (is (thrown-with-msg?
            ExceptionInfo #"clj-http: status 400"
            (client/patch
              "http://localhost:3000/api/v1/todo"
              {:body         body
               :content-type :json
               :accept       :json})))))

  (testing "update responds with 404 given id does not exist"
    (let [update-request (gen/generate (s/gen ::request/update-request))
          body (cheshire.core/generate-string update-request)]
      (is (thrown-with-msg?
            ExceptionInfo #"clj-http: status 404"
            (client/patch
              "http://localhost:3000/api/v1/todo"
              {:body         body
               :content-type :json
               :accept       :json}))))))

(deftest delete-test
  (testing "delete responds with 200 given id exists"
    (let [create-request (gen/generate (s/gen ::request/create-request))
          body (cheshire.core/generate-string create-request)
          create (client/post "http://localhost:3000/api/v1/todo" {:body         body
                                                                   :content-type :json
                                                                   :accept       :json})
          create-response (cheshire.core/parse-string (:body create) true)
          delete (client/delete
                   (format (format "http://localhost:3000/api/v1/todo/%s" (:id create-response)))
                   {:content-type :json
                    :accept       :json})]
      (is (= (:status delete) 204))))

  (testing "delete responds with 404 given id does not exist"
    (let [id (gen/generate (s/gen ::request/id))]
      (is (thrown-with-msg?
            ExceptionInfo #"clj-http: status 404"
            (client/delete
              (format "http://localhost:3000/api/v1/todo/%s" id)
              {:content-type :json
               :accept       :json}))))))
