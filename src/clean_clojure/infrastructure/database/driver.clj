(ns clean-clojure.infrastructure.database.driver
  (:require [com.stuartsierra.component :refer [Lifecycle]]
            [environ.core :refer [env]]
            [oksql.core :as oksql]))

(defrecord DatabaseDriver
  []
  Lifecycle
  (start [this]
    (assoc this :query (partial oksql/query {:connection-uri (env :database-url)})))
  (stop [this]
    (dissoc this :query)))

(defn new
  []
  (map->DatabaseDriver {}))
