(ns clean-clojure.application.port.use-case
  (:require [clean-clojure.domain.todo :as entity]
            [clojure.spec.alpha :as s]))

(defprotocol UseCase
  (-create [this todo] "create stores a given entity and returns the id")
  (-read [this id] "read finds a single entity and returns it")
  (-update [this todo] "update partially updates a given entity")
  (-delete [this id] "delete removes a todo entity with the given id"))

(s/def ::port #(satisfies? UseCase %))

(defn create
  [this todo]
  (-create this todo))

(s/fdef create
  :args (s/cat :this ::port
               :todo ::entity/create-command)
  :ret ::entity/todo)

(defn read
  [this id]
  (-read this id))

(s/fdef read
  :args (s/cat :this ::port
               :id ::entity/id)
  :ret ::entity/todo)

(defn update
  [this todo]
  (-update this todo))

(s/fdef update
  :args (s/cat :this ::port
               :todo ::entity/update-command)
  :ret ::entity/todo)

(defn delete
  [this id]
  (-delete this id))

(s/fdef delete
  :args (s/cat :this ::port
               :id ::entity/id)
  :ret nil?)
