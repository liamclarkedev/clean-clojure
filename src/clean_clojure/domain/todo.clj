(ns clean-clojure.domain.todo
  (:require [clj-time.core :as time]
            [clj-time.spec :as time.spec]
            [clojure.spec.alpha :as s]))

(s/def ::id uuid?)
(s/def ::title string?)
(s/def ::description string?)
(s/def ::created-at ::time.spec/date-time)
(s/def ::completed-at (s/nilable ::time.spec/date-time))

(s/def ::todo
  (s/keys :req-un [::id
                   ::title
                   ::description
                   ::created-at
                   ::completed-at]))

(s/def ::create-command
  (s/keys :req-un [::title
                   ::description
                   ::completed-at]))

(s/def ::update-command
  (s/keys :req-un [::id]
          :opt-un [::title
                   ::description
                   ::completed-at]))

(defn valid-completed?
  [todo]
  (if-let [completed-at (:completed-at todo)]
    (time/before? (:created-at todo) completed-at)
    true))
