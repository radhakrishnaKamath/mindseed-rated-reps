(ns user
  (:require [mount.core :as mount]
            [string-manipulator.figwheel :refer [start-fw stop-fw cljs]]
            string-manipulator.core))

(defn start []
  (mount/start-without #'string-manipulator.core/repl-server))

(defn stop []
  (mount/stop-except #'string-manipulator.core/repl-server))

(defn restart []
  (stop)
  (start))


