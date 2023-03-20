(ns clean-clojure.infrastructure.web.middleware
  (:require [clojure.tools.logging :as log]
            [ring.util.response :as ring]))

(defn wrap-exception-handler [ex _data _req]
  (let [error (ex-data ex)
        message (.getMessage ex)
        type (:type error)]
    (case type
      :not-found (ring/not-found message)
      :invalid (ring/bad-request message)
      (do
        (log/error ex "unhandled exception" {:message message})
        (ring/status (ring/response "unexpected error occurred") 500)))))
