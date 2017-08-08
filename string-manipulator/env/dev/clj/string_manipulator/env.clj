(ns string-manipulator.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [string-manipulator.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[string-manipulator started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[string-manipulator has shut down successfully]=-"))
   :middleware wrap-dev})
