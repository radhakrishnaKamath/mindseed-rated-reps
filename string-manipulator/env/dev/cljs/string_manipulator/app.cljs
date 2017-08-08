(ns ^:figwheel-no-load string-manipulator.app
  (:require [string-manipulator.core :as core]
            [devtools.core :as devtools]))

(enable-console-print!)

(devtools/install!)

(core/init!)
