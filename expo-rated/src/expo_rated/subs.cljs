(ns expo-rated.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
 :get-greeting
 (fn [db _]
   (:greeting db)))

(reg-sub
 :get-a
 (fn [db _]
   (or (:a db) 0)))
