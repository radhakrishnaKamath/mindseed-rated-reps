(ns ^:figwheel-no-load rated-reframe-app.app
  (:require [rated-reframe-app.core :as core]
            [devtools.core :as devtools]))

(enable-console-print!)

(devtools/install!)

(core/init!)
