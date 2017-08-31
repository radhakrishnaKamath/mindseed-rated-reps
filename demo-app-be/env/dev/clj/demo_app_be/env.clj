(ns demo-app-be.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [demo-app-be.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[demo-app-be started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[demo-app-be has shut down successfully]=-"))
   :middleware wrap-dev})
