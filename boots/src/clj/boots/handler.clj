(ns boots.handler
  (:require [compojure.core :refer [routes wrap-routes]]
            [boots.layout :refer [error-page]]
            [boots.routes.home :refer [home-routes]]
            [compojure.route :as route]
            [boots.env :refer [defaults]]
            [mount.core :as mount]
            [boots.middleware :as middleware]))

(mount/defstate init-app
                :start ((or (:init defaults) identity))
                :stop  ((or (:stop defaults) identity)))

(def app-routes
  (routes
    (-> #'home-routes
        (wrap-routes middleware/wrap-csrf)
        (wrap-routes middleware/wrap-formats))
    (route/not-found
      (:body
        (error-page {:status 404
                     :title "page not found"})))))


(defn app [] (middleware/wrap-base #'app-routes))
