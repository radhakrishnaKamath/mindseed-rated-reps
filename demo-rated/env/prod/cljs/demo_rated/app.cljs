(ns demo-rated.app
  (:require [demo-rated.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
