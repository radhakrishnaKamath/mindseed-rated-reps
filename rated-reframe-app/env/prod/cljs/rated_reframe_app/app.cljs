(ns rated-reframe-app.app
  (:require [rated-reframe-app.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
