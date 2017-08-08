(ns ^:figwheel-no-load my-spiral-game.dev
  (:require
    [my-spiral-game.core :as core]
    [devtools.core :as devtools]))

(devtools/install!)

(enable-console-print!)

(core/init!)
