(ns user
  (:require [mount.core :as mount]
            demo-app-be.core))

(defn start []
  (mount/start-without #'demo-app-be.core/repl-server))

(defn stop []
  (mount/stop-except #'demo-app-be.core/repl-server))

(defn restart []
  (stop)
  (start))


