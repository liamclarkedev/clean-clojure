(ns clean-clojure.application.port.storage
  (:require [clean-clojure.domain.todo :as entity]
            [clojure.spec.alpha :as s]))

(defprotocol Storage
  (-store [this create-command])
  (-find-one [this id])
  (-update [this update-command])
  (-delete [this id]))

(s/def ::port #(satisfies? Storage %))

(defn store
  [this create-command]
  (-store this create-command))

(s/fdef store
  :args (s/cat :this ::port
               :todo ::entity/create-command)
  :ret (s/nilable ::entity/todo))

(defn find-one
  [this id]
  (-find-one this id))

(s/fdef find-one
  :args (s/cat :this ::port
               :id ::entity/id)
  :ret (s/nilable ::entity/todo))

(defn update
  [this update-command]
  (-update this update-command))

(s/fdef update
  :args (s/cat :this ::port
               :todo ::entity/update-command)
  :ret (s/nilable ::entity/todo))

(defn delete
  [this id]
  (-delete this id))

(s/fdef delete
  :args (s/cat :this ::port
               :id ::entity/id)
  :ret (s/nilable ::entity/todo))
