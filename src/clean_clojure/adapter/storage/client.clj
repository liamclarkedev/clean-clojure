(ns clean-clojure.adapter.storage.client
  (:require [clean-clojure.adapter.storage.model :as model]
            [clean-clojure.application.port.storage :refer [Storage]]))

(defrecord PostgresStorage
  [db]
  Storage
  (-store [_ create-command]
    (let [query (:query db)
          todo (query :todo/insert-one (model/create-command->row create-command))]
      (model/->todo todo)))

  (-find-one [_ id]
    (let [query (:query db)
          todo (query :todo/find-by-id {:id id})]
      (when-not (empty? todo)
        (model/->todo todo))))

  (-update [_ update-command]
    (let [query (:query db)
          todo (query :todo/update-one (model/update-command->row update-command))]
      (when-not (empty? todo)
        (model/->todo todo))))

  (-delete [_ id]
    (let [query (:query db)
          todo (query :todo/delete-one {:id id})]
      (when-not (empty? todo)
        (model/->todo todo)))))

(defn new
  []
  (map->PostgresStorage {}))
