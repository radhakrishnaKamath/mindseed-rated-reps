(ns ^:figwheel-no-load boots.app
  (:require [boots.core :as core]
            [devtools.core :as devtools]))

(enable-console-print!)

(devtools/install!)

(core/init!)
