(ns clean-clojure.adapter.storage.client-test
  (:require [clean-clojure.application.port.storage :as storage]
            [clean-clojure.domain.todo :as todo]
            [clean-clojure.system-test :as system]
            [clj-time.core :as time]
            [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [clojure.test :refer :all]
            [com.stuartsierra.component :as component]))

(defn- store-test
  [system]
  (testing "store-test"
    (let [storage (:storage system)]
      (testing "store creates with a create-command and returns a todo entity"
        (let [create-command (assoc (gen/generate (s/gen ::todo/create-command))
                                    :completed-at nil)
              todo (storage/store storage create-command)]
          (is (= create-command (dissoc todo :id :created-at)))
          (is (uuid? (:id todo)))
          (is (inst? (:created-at todo))))))))

(defn- find-one-test
  [system]
  (testing "find-one-test"
    (let [storage (:storage system)]
      (testing "find-one returns a todo entity given the id exists"
        (let [create-command (assoc (gen/generate (s/gen ::todo/create-command))
                                    :completed-at nil)
              todo (storage/store storage create-command)
              found (storage/find-one storage (:id todo))]
          (is (= found todo))))

      (testing "find-one returns nil given the id doesn't exists"
        (let [id (gen/generate (s/gen ::todo/id))
              found (storage/find-one storage id)]
          (is (nil? found)))))))

(defn- update-test
  [system]
  (testing "update-test"
    (let [storage (:storage system)]
      (testing "update updates an existing todo entity given new values"
        (let [create-command (assoc (gen/generate (s/gen ::todo/create-command))
                                    :completed-at nil)
              todo (storage/store storage create-command)
              completed (time/now)
              update-command (assoc todo
                                    :completed-at completed)
              updated (storage/update storage update-command)]
          (is (= (:completed-at updated) completed))))

      (testing "update returns nil given id does not exist"
        (let [update-command (gen/generate (s/gen ::todo/update-command))
              updated (storage/update storage update-command)]
          (is (nil? updated)))))))

(defn- delete-test
  [system]
  (testing "delete-test"
    (let [storage (:storage system)]
      (testing "delete deletes an existing todo entity and returns it given a valid id"
        (let [create-command (gen/generate (s/gen ::todo/create-command))
              todo (storage/store storage create-command)
              deleted (storage/delete storage (:id todo))]
          (is (= todo deleted))))

      (testing "update returns nil given id does not exist"
        (let [id (gen/generate (s/gen ::todo/id))
              deleted (storage/delete storage id)]
          (is (nil? deleted)))))))

(defn test-suite
  [system]
  (store-test system)
  (find-one-test system)
  (update-test system)
  (delete-test system))

(deftest storage-test
  (testing "storage fake"
    (let [system (component/start (system/fake-system))]
      (try
        (test-suite system)
        (finally (component/stop system)))))

  (testing "storage integration"
    (let [system (component/start (system/storage-system))]
      (try
        (test-suite system)
        (finally (component/stop system))))))
