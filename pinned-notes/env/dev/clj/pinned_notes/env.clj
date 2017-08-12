(ns pinned-notes.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [pinned-notes.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[pinned-notes started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[pinned-notes has shut down successfully]=-"))
   :middleware wrap-dev})
