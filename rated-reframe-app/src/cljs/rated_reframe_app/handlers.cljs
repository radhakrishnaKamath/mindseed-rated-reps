(ns rated-reframe-app.handlers
  (:require [rated-reframe-app.db :as db]
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
 :set-count
 (fn [db [_ _]]
   (or (update db :count inc) 0)))

(reg-event-db
 :reset-count
 (fn [db [_ _]]
   (assoc db :count 0)))
