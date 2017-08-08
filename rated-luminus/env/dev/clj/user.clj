(ns user
  (:require [mount.core :as mount]
            rated-luminus.core))

(defn start []
  (mount/start-without #'rated-luminus.core/repl-server))

(defn stop []
  (mount/stop-except #'rated-luminus.core/repl-server))

(defn restart []
  (stop)
  (start))


