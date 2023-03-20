(ns clean-clojure.infrastructure.web.router
  (:require [clean-clojure.adapter.http.request :as request]
            [clean-clojure.adapter.http.response :as response]
            [clean-clojure.application.port.web :as handler]
            [clean-clojure.infrastructure.web.middleware :as middleware]
            [com.stuartsierra.component :refer [Lifecycle]]
            [compojure.api.exception :as ex]
            [compojure.api.sweet :refer [DELETE GET PATCH POST api context routes]]
            [compojure.route :refer [not-found]]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.defaults :refer [api-defaults wrap-defaults]]))

(defrecord WebRouter
  [handler]
  Lifecycle
  (start [_]
    (->
      (api
        {:exceptions {:handlers {::ex/default middleware/wrap-exception-handler}}
         :coercion   :spec
         :swagger
         {:ui   "/docs"
          :spec "/swagger.json"
          :data {:info     {:title       "Clean Clojure API"
                            :description "Example application used as a template."}
                 :consumes ["application/json"]
                 :produces ["application/json"]}}}

        (context "/api/v1" []
          (context "/todo" []
            :tags ["Todo"]
            (POST "/" []
              :summary "Create a Todo"
              :body [body ::request/create-request]
              :responses {201 {:schema ::response/todo}}
              (handler/-create handler body))
            (PATCH "/" []
              :summary "Update a Todo"
              :body [body ::request/update-request]
              :responses {200 {:schema ::response/todo}}
              (handler/-update handler body))
            (context ["/:id"] []
              (GET "/" []
                :summary "Get a Todo"
                :path-params [id :- ::request/id]
                :responses {200 {:schema ::response/todo}}
                (handler/-read handler id))
              (DELETE "/" []
                :summary "Delete a Todo"
                :path-params [id :- ::request/id]
                (handler/-delete handler id))))))
      (routes
        (not-found "not found"))
      (wrap-defaults api-defaults)
      (jetty/run-jetty {:port 3000, :join? false}))))

(defn new
  []
  (map->WebRouter {}))
