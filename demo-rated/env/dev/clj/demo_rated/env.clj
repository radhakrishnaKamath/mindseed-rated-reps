(ns demo-rated.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [demo-rated.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[demo-rated started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[demo-rated has shut down successfully]=-"))
   :middleware wrap-dev})
