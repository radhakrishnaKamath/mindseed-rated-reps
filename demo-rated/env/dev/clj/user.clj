(ns user
  (:require [mount.core :as mount]
            [demo-rated.figwheel :refer [start-fw stop-fw cljs]]
            demo-rated.core))

(defn start []
  (mount/start-without #'demo-rated.core/repl-server))

(defn stop []
  (mount/stop-except #'demo-rated.core/repl-server))

(defn restart []
  (stop)
  (start))


