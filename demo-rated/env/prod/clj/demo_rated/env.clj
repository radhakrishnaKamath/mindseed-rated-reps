(ns demo-rated.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[demo-rated started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[demo-rated has shut down successfully]=-"))
   :middleware identity})
