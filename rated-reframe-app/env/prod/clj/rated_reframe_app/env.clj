(ns rated-reframe-app.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[rated-reframe-app started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[rated-reframe-app has shut down successfully]=-"))
   :middleware identity})
