(ns clean-clojure.adapter.storage.model
  (:require [clj-time.coerce :as time.coerce]
            [clojure.spec.alpha :as s]))

(s/def ::id uuid?)
(s/def ::title string?)
(s/def ::description string?)
(s/def ::created-at inst?)
(s/def ::completed-at (s/nilable inst?))

(defn create-command->row
  [create-command]
  (-> create-command
      (select-keys [:title
                    :description
                    :completed-at])
      (update-in [:completed-at] time.coerce/to-sql-time)))

(defn update-command->row
  [update-command]
  (-> {:id           (:id update-command)
       :title        (:title update-command)
       :description  (:description update-command)
       :completed-at (:completed-at update-command)}
      (update-in [:completed-at] time.coerce/to-sql-time)))

(defn ->todo
  [row]
  (-> row
      (select-keys [:id
                    :title
                    :description
                    :created-at
                    :completed-at])
      (update-in [:created-at] time.coerce/from-sql-time)
      (update-in [:completed-at] time.coerce/from-sql-time)))
