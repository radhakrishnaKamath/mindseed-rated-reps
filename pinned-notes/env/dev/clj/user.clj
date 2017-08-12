(ns user
  (:require [mount.core :as mount]
            [pinned-notes.figwheel :refer [start-fw stop-fw cljs]]
            pinned-notes.core))

(defn start []
  (mount/start-without #'pinned-notes.core/repl-server))

(defn stop []
  (mount/stop-except #'pinned-notes.core/repl-server))

(defn restart []
  (stop)
  (start))


