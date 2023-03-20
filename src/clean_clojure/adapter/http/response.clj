(ns clean-clojure.adapter.http.response
  (:require [clojure.spec.alpha :as s]))

(s/def ::id string?)
(s/def ::title string?)
(s/def ::description string?)
(s/def ::created-at inst?)
(s/def ::completed-at (s/nilable inst?))

(s/def ::todo
  (s/keys :req-un [::id
                   ::title
                   ::description
                   ::created-at
                   ::completed-at]))

(defn ->single-response
  [entity]
  (-> entity
      (select-keys [:id
                    :title
                    :description
                    :created-at
                    :completed-at])
      (update-in [:id] str)))
