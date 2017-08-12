(ns string-manipulator.db.core
    (:require [monger.core :as mg]
              [monger.collection :as mc]
              [monger.operators :refer :all]
              [mount.core :refer [defstate]]
              [string-manipulator.config :refer [env]]))

(defstate db*
  :start (-> env :database-url mg/connect-via-uri)
  :stop (-> db* :conn mg/disconnect))

(defstate db
  :start (:db db*))

(defn create-user [user password]
  (mc/insert db "users" {:user user :password password}))

(defn get-users []
  (mc/find-maps db "users"))

(defn get-user [user pass]
  (mc/find-maps db "users" {:user user :password pass}))

(defn create-task [task user]
  (mc/insert db "to-do-list" {:task-name task :user user :status "undone"}))

(defn update-task [task user]
  (mc/update db "to-do-list" {:task-name task :user user}
             {$set {:status "done"}} {:multi true}))

(defn get-tasks [user]
  (mc/find-maps db "to-do-list" {:user user}))

(defn update-todo [task-old task-new user]
  (mc/update db "to-do-list" {:task-name task-old :user user}
             {$set {:task-name task-new}}))
