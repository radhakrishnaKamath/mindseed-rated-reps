(ns pinned-notes.view
  (:require [pinned-notes.db.core :as db]
            [pinned-notes.layout :as layout]))

(defn show-task []
  (layout/render-json (map #(select-keys % [:task-name :author :task-desc :status])
                           (remove #(or (= "done" (:status %)) (= "deleted" (:status %)))
                                   (db/get-tasks)))))

(defn show-done-task []
  (layout/render-json (map #(select-keys % [:task-name :author :task-desc :status])
                           (filter #(= "done" (:status %))
                                   (db/get-tasks)))))

(defn add-task [task-name author desc]
  (db/create-task {:task-name task-name :author author :task-desc desc :status "undone"})
  (show-task))

(defn done-task [task-name author]
  (db/done-task task-name author)
  (show-task))

(defn delete-task [task-name author]
  (db/delete-task task-name author)
  (show-task))

(defn update-task [task-name author desc]
  (db/update-task task-name author desc)
  (show-task))
