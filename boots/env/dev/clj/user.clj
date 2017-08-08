(ns user
  (:require [mount.core :as mount]
            [boots.figwheel :refer [start-fw stop-fw cljs]]
            boots.core))

(defn start []
  (mount/start-without #'boots.core/repl-server))

(defn stop []
  (mount/stop-except #'boots.core/repl-server))

(defn restart []
  (stop)
  (start))


