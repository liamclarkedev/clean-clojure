(ns clean-clojure.system
  (:require [clean-clojure.adapter.http.handler :as handler]
            [clean-clojure.adapter.storage.client :as storage]
            [clean-clojure.application.service :as service]
            [clean-clojure.infrastructure.database.driver :as database]
            [clean-clojure.infrastructure.web.router :as router]
            [com.stuartsierra.component :as component]))

(defn- system
  []
  (component/system-map
    :database (database/new)
    :storage (component/using (storage/new)
               {:db :database})
    :service (component/using (service/new)
               {:storage :storage})
    :handler (component/using (handler/new)
               {:use-case :service})
    :router (component/using (router/new)
              {:handler :handler})))

(defn start
  []
  (component/start (system)))
