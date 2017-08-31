(ns demo-app-be.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[demo-app-be started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[demo-app-be has shut down successfully]=-"))
   :middleware identity})
