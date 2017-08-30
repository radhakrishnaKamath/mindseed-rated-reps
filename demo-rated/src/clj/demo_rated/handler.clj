(ns demo-rated.handler
  (:require [compojure.core :refer [routes wrap-routes]]
            [demo-rated.layout :refer [error-page]]
            [demo-rated.routes.home :refer [home-routes]]
            [compojure.route :as route]
            [demo-rated.env :refer [defaults]]
            [mount.core :as mount]
            [demo-rated.middleware :as middleware]))

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
