(ns clean-clojure.adapter.storage.fake
  (:require [clean-clojure.application.port.storage :as storage]
            [clj-time.core :as time]
            [clojure.test :refer :all]))

(defrecord FakeStorage
  [db]
  storage/Storage
  (-store [_ create-command]
    (when-not (get @db (:id create-command))
      (let [todo (assoc create-command
                        :id (java.util.UUID/randomUUID)
                        :created-at (time/now))]
        (swap! db assoc (:id todo) todo)
        todo)))

  (-find-one [_ id]
    (get @db id nil))

  (-update [_ update-command]
    (when-let [row (get @db (:id update-command))]
      (let [todo (merge row update-command)]
        (swap! db assoc (:id update-command) (merge row update-command))
        todo)))

  (-delete [_ id]
    (when-let [row (get @db id)]
      (swap! db dissoc id)
      row)))

(defn new
  []
  (map->FakeStorage {:db (atom {})}))