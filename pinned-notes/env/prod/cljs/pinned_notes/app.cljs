(ns pinned-notes.app
  (:require [pinned-notes.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
