(ns ^:figwheel-no-load pinned-notes.app
  (:require [pinned-notes.core :as core]
            [devtools.core :as devtools]))

(enable-console-print!)

(devtools/install!)

(core/init!)
