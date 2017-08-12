(ns pinned-notes.routes.home
  (:require [pinned-notes.layout :as layout]
            [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]
            [pinned-notes.view :as view]))

(defn home-page []
  (layout/render "home.html"))

(defroutes home-routes
  (GET "/" []
       (home-page))
  (GET "/task" [task-name author desc]
       (view/add-task task-name author desc ))
  (GET "/tasks" []
       (view/show-task))
  (GET "/done-task" [task-name author]
       (view/done-task task-name author))
  (GET "/del-task" [task-name author]
       (view/delete-task task-name author))
  (GET "/finished-task" []
       (view/show-done-task))
  (GET "/update-task" [task-name author desc]
       (view/update-task task-name author desc))
  #_(GET "/upd-todo" [task-name-old task-name-upd user]
       (view/upd-task task-name-old task-name-upd user)))
