(ns pinned-notes.db.core
    (:require [monger.core :as mg]
              [monger.collection :as mc]
              [monger.operators :refer :all]
              [mount.core :refer [defstate]]
              [pinned-notes.config :refer [env]]))

(defstate db*
  :start (-> env :database-url mg/connect-via-uri)
  :stop (-> db* :conn mg/disconnect))

(defstate db
  :start (:db db*))

(defn create-task [task-map]
  (mc/insert db "task" task-map))

(defn done-task [task author]
  (mc/update db "task" {:task-name task :author author}
             {$set {:status "done"}} {:multi true}))

(defn delete-task [task author]
  (mc/update db "task" {:task-name task :author author}
             {$set {:status "deleted"}} {:multi true}))

(defn get-tasks []
  (mc/find-maps db "task"))

(defn update-task [task author task-desc]
  (mc/update db "task" {:task-name task :author author}
             {$set {:task-desc task-desc}} {:multi true}))
