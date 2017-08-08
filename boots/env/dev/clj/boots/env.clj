(ns boots.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [boots.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[boots started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[boots has shut down successfully]=-"))
   :middleware wrap-dev})
