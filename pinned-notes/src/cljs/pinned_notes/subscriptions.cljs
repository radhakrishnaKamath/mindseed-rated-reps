(ns pinned-notes.subscriptions
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :page
  (fn [db _]
    (:page db)))

(reg-sub
 :docs
 (fn [db _]
   (:docs db)))

(reg-sub
 :tasks
 (fn [db _]
   (:tasks db)))

(reg-sub
 :done-tasks
 (fn [db _]
   (:done-tasks db)))

(reg-sub
 :get-update-data
 (fn [db _]
   (:update-task db)))
