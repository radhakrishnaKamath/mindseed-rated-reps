(ns pinned-notes.handlers
  (:require [pinned-notes.db :as db]
            [re-frame.core :refer [dispatch reg-event-db]]))

(reg-event-db
  :initialize-db
  (fn [_ _]
    db/default-db))

(reg-event-db
  :set-active-page
  (fn [db [_ page]]
    (assoc db :page page)))

(reg-event-db
 :set-docs
 (fn [db [_ docs]]
   (assoc db :docs docs)))

(reg-event-db
 :set-tasks
 (fn [db [_ tasks]]
   (assoc db :tasks tasks)))

(reg-event-db
 :set-done-tasks
 (fn [db [_ tasks]]
   (assoc db :done-tasks tasks)))

(reg-event-db
 :set-update-data
 (fn [db [_ data]]
   (assoc db :update-task data)))
