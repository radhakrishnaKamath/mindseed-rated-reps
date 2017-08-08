(ns rated-luminus.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [rated-luminus.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[rated-luminus started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[rated-luminus has shut down successfully]=-"))
   :middleware wrap-dev})
