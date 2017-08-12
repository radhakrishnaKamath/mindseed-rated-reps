(ns string-manipulator.routes.home
  (:require [string-manipulator.layout :as layout]
            [compojure.core :refer [defroutes GET POST]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]
            [clojure.string :as string]
            [string-manipulator.view :as view]))

(defn manipulator-string [s]
  (layout/render-json [(string/capitalize s) (string/upper-case s)]))

(defn home-page []
  (layout/render "home.html"))

(defroutes home-routes
  (GET "/" []
       (home-page))
  (GET "/manipulator-string" [s]
       (manipulator-string s))
  (GET "/todo" [task-name user]
       (view/add-todo task-name user))
  (GET "/todos" [user]
       (view/show-todo user))
  (GET "/login" [user pass]
       (view/check-login user pass))
  (GET "/done-todo" [task-name user]
       (view/done-task task-name user))
  (GET "/upd-todo" [task-name-old task-name-upd user]
       (view/upd-task task-name-old task-name-upd user))
  #_(POST "/todo" request (view/add-todo request)))
