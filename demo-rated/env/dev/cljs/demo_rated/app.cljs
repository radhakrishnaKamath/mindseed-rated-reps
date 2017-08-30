(ns ^:figwheel-no-load demo-rated.app
  (:require [demo-rated.core :as core]
            [devtools.core :as devtools]))

(enable-console-print!)

(devtools/install!)

(core/init!)
