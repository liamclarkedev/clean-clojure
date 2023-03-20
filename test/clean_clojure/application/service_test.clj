(ns clean-clojure.application.service-test
  (:require [clean-clojure.application.port.use-case :as use-case]
            [clean-clojure.domain.todo :as entity]
            [clean-clojure.system-test :as system]
            [clj-time.core :as time]
            [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [clojure.test :refer :all]
            [com.stuartsierra.component :as component])
  (:import (clojure.lang ExceptionInfo)))

(deftest -create-test
  (let [system (component/start (system/fake-system))
        service (:service system)]

    (testing "expect create to create a todo and return it"
      (let [expect (gen/generate (s/gen ::entity/create-command))
            got (use-case/create service expect)]
        (is (= (dissoc got :id :created-at) expect))))))

(deftest -read-test
  (let [system (component/start (system/fake-system))
        service (:service system)]

    (testing "expect read to find a created todo"
      (let [create-command (gen/generate (s/gen ::entity/create-command))
            expect (use-case/create service create-command)
            got (use-case/read service (:id expect))]
        (is (= got expect))))

    (testing "expect read to throw given todo does not exist"
      (let [id (gen/generate (s/gen ::entity/id))]
        (is (thrown-with-msg? ExceptionInfo #"todo not found" (use-case/read service id)))))))

(deftest -update-test
  (let [system (component/start (system/fake-system))
        service (:service system)]
    (try
      
      (testing "expect update to update a created todo and return it"
        (let [create-command (assoc (gen/generate (s/gen ::entity/create-command))
                                    :completed-at nil)
              todo (use-case/create service create-command)
              expect (assoc todo :title (gen/generate (s/gen ::entity/title)))
              got (use-case/update service expect)]
          (is (= got expect))))

      (testing "expect update to throw given invalid completed time"
        (let [create-command (gen/generate (s/gen ::entity/todo))
              todo (use-case/create service create-command)
              expect (assoc todo :completed-at (time/minus (:created-at todo) (time/days 1)))]
          (is (thrown-with-msg?
                ExceptionInfo
                #"completed time must be after the created time"
                (use-case/update service expect)))))

      (testing "expect update to throw given todo does not exist"
        (let [update-command (gen/generate (s/gen ::entity/update-command))]
          (is (thrown-with-msg? ExceptionInfo #"todo not found" (use-case/update service update-command)))))

      (finally (component/stop system)))))

(deftest -delete-test
  (let [system (component/start (system/fake-system))
        service (:service system)]

    (testing "expect delete to delete an existing todo"
      (let [create-command (gen/generate (s/gen ::entity/create-command))
            todo (use-case/create service create-command)]
        (is (= todo (use-case/read service (:id todo))))
        (is (nil? (use-case/delete service (:id todo))))))

    (testing "expect delete to throw given id does not exist"
      (let [id (gen/generate (s/gen ::entity/id))]
        (is (thrown-with-msg? ExceptionInfo #"todo not found" (use-case/delete service id)))))))
