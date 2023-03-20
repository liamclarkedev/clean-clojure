(ns clean-clojure.system-test
  (:require [clean-clojure.adapter.storage.client :as storage]
            [clean-clojure.adapter.storage.fake :as storage-fake]
            [clean-clojure.application.service :as service]
            [clean-clojure.infrastructure.database.driver :as database]
            [com.stuartsierra.component :as component]
            [orchestra.spec.test :as st]))

(defn fake-system
  []
  (st/instrument)
  (component/system-map
    :storage (storage-fake/new)
    :service (component/using (service/new)
               {:storage :storage})))

(defn storage-system
  []
  (st/instrument)
  (component/system-map
    :database (database/new)
    :storage (component/using (storage/new)
               {:db :database})))
