(ns clean-clojure.application.port.web)

(defprotocol Web
  (-create [this body])
  (-read [this id])
  (-update [this body])
  (-delete [this id]))

