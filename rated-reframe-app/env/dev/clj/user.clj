(ns user
  (:require [mount.core :as mount]
            [rated-reframe-app.figwheel :refer [start-fw stop-fw cljs]]
            rated-reframe-app.core))

(defn start []
  (mount/start-without #'rated-reframe-app.core/repl-server))

(defn stop []
  (mount/stop-except #'rated-reframe-app.core/repl-server))

(defn restart []
  (stop)
  (start))


