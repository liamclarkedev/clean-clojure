(ns clean-clojure.adapter.http.request
  (:require [clj-time.coerce :as time.coerce]
            [clojure.spec.alpha :as s]))

(s/def ::id uuid?)
(s/def ::title string?)
(s/def ::description string?)
(s/def ::created-at inst?)
(s/def ::completed-at (s/nilable inst?))

(s/def ::create-request
  (s/keys :req-un [::title
                   ::description
                   ::completed-at]))

(defn ->create-command
  [create-request]
  (-> create-request
      (select-keys [:title
                    :description
                    :completed-at])
      (update-in [:completed-at] time.coerce/from-date)))

(s/def ::update-request
  (s/keys :req-un [::id]
          :opt-un [::title
                   ::description
                   ::completed-at]))

(defn ->update-command
  [update-request]
  (-> update-request
      (select-keys [:id
                    :title
                    :description
                    :completed-at])
      (update-in [:completed-at] time.coerce/from-date)))
