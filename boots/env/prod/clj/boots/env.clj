(ns boots.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[boots started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[boots has shut down successfully]=-"))
   :middleware identity})
