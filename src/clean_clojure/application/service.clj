(ns clean-clojure.application.service
  (:require [clean-clojure.application.port.storage :as storage]
            [clean-clojure.application.port.use-case :refer [UseCase]]
            [clean-clojure.domain.todo :as todo]))

(defrecord TodoService
  [storage]
  UseCase
  (-create [_ create-command]
    (storage/store storage create-command))

  (-read [_ id]
    (or (storage/find-one storage id)
        (throw (ex-info "todo not found" {:type :not-found}))))

  (-update [_ update-command]
    (if-let [todo (storage/find-one storage (:id update-command))]
      (if (todo/valid-completed? (merge todo update-command))
        (storage/update storage update-command)
        (throw (ex-info "completed time must be after the created time" {:type :invalid})))
      (throw (ex-info "todo not found" {:type :not-found}))))

  (-delete [_ id]
    (when-not (storage/delete storage id)
      (throw (ex-info "todo not found" {:type :not-found})))))

(defn new
  []
  (map->TodoService {}))
