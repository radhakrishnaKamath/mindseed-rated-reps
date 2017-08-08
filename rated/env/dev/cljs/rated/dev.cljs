(ns ^:figwheel-no-load rated.dev
  (:require
    [rated.core :as core]
    [devtools.core :as devtools]))

(devtools/install!)

(enable-console-print!)

(core/init!)
