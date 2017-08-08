(ns string-manipulator.view
  (:require [string-manipulator.db.core :as db]
            [string-manipulator.layout :as layout]))

(defn get-multipart-param [request name]
  (get-in request [:multuipart-params name]))

(defn add-todo [task-name user]
  (println "@@@" task-name "###" user)
  (db/create-task task-name user)
  (layout/render-json (map #(select-keys % [:task-name :status])
                           (remove #(= "done" (:status %))
                                   (db/get-tasks user)))))

#_(defn add-todo [request]
  (println "@@@" request)
  (let [task-name (get-multipart-param request "task-name")
        user (get-multipart-param request "user")]
    (db/create-task task-name user)
    (layout/render-json (map #(select-keys % [:task-name :status]) (db/get-tasks "rated")))))

(defn check-login [user pass]
  (println "@@@ user" user "### pass" pass)
  (layout/render-json (map #(select-keys % [:user :password]) (db/get-user user pass))))

(defn done-task [task-name user]
  (println "@@@" task-name "###" user)
  (db/update-task task-name user)
  (layout/render-json (map #(select-keys % [:task-name :status])
                           (remove #(= "done" (:status %))
                                   (db/get-tasks user)))))

(defn show-todo [user]
  (layout/render-json (map #(select-keys % [:task-name :status])
                           (remove #(= "done" (:status %))
                                   (db/get-tasks user)))))

(defn upd-task [task-name-old task-name-upd user]
  (db/update-todo task-name-old task-name-upd user)
  (layout/render-json (map #(select-keys % [:task-name :status])
                           (remove #(= "done" (:status %))
                                   (db/get-tasks user)))))
