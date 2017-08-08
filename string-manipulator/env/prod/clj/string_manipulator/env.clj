(ns string-manipulator.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[string-manipulator started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[string-manipulator has shut down successfully]=-"))
   :middleware identity})
