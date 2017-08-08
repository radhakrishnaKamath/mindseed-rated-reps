(ns my-spiral-game.prod
  (:require [my-spiral-game.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
