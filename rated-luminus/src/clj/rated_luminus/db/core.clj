(ns rated-luminus.db.core
    (:require [monger.core :as mg]
              [monger.collection :as mc]
              [monger.operators :refer :all]
              [mount.core :refer [defstate]]
              [rated-luminus.config :refer [env]]))

(defstate db*
  :start (-> env :database-url mg/connect-via-uri)
  :stop (-> db* :conn mg/disconnect))

(defstate db
  :start (:db db*))

(defn create-user [user]
  (mc/insert db "users" user))

(defn update-user [first-name last-name email]
  (mc/update db "users" {:name "rk"}
             {$set {:first_name first-name
                    :last_name last-name
                    :email email}}))

(defn get-user [task]
  (mc/find-one-as-map db "to-do-list-rated" {:task task}))








(defn print-msg []
  (println "hey there, what is your wish?\n 1. to make a new task\n 2. see all the tasks\n 3. edit any task\n 4. mark a task as done\n please enter any option "))

(defn create-task [[task-name task-content]]
  (mc/insert db "to-do-list-rated" {:task task-name :content task-content :status "undone"}))

(defn update-task [[[task-name content] flag]]
  (mc/update db "to-do-list-rated" {:task task-name}
             {$set {flag content}}))

(defn show-tasks []
  (clojure.pprint/pprint (map #(select-keys % [:task :content])
                              (remove #(= "done" (get % :status))
                                      (mc/find-maps db "to-do-list-rated")))))

(defn read-helper []
  (println "enter the name of task")
  (let [task-name (read-line)]
    (println "enter the content of task")
    [task-name (read-line)]))

(defn make-task []
  (create-task (read-helper)))

(defn edit-any-task []
  (update-task [(read-helper) :content]))

(defn mark-task-done []
  (println "enter the name of task which is to be marked done")
  (update-task  [[(read-line) "done"] :status]))

(defn make-to-do []
  (print-msg)
  (let [wish (read-string (read-line))]
    (condp = wish
      1 (make-task)
      2 (show-tasks)
      3 (edit-any-task)
      4 (mark-task-done))))
