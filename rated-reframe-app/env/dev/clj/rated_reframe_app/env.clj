(ns rated-reframe-app.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [rated-reframe-app.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[rated-reframe-app started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[rated-reframe-app has shut down successfully]=-"))
   :middleware wrap-dev})
