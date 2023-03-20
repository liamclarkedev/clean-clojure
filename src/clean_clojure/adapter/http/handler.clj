(ns clean-clojure.adapter.http.handler
  (:require [clean-clojure.adapter.http.request :as request]
            [clean-clojure.adapter.http.response :as response]
            [clean-clojure.application.port.use-case :as use-case]
            [clean-clojure.application.port.web :refer [Web]]
            [ring.util.response :as ring]))

(defrecord HTTPWebHandler
  [use-case]
  Web
  (-create [_ body]
    (let [todo (use-case/create use-case (request/->create-command body))]
      (ring/created (format "/api/v1/todo/%s" (:id todo)) (response/->single-response todo))))

  (-read [_ id]
    (let [todo (use-case/read use-case id)]
      (ring/response (response/->single-response todo))))

  (-update [_ body]
    (let [todo (use-case/update use-case (request/->update-command body))]
      (ring/response (response/->single-response todo))))

  (-delete [_ id]
    (use-case/delete use-case id)
    (ring/status 204)))

(defn new
  []
  (map->HTTPWebHandler {}))
