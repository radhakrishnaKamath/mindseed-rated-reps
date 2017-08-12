(ns pinned-notes.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[pinned-notes started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[pinned-notes has shut down successfully]=-"))
   :middleware identity})
