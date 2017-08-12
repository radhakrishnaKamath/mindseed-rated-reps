(ns string-manipulator.core
  (:require [reagent.core :as r]
            [ajax.core :refer [GET POST]]
            [secretary.core :as secretary :include-macros true]
            [reagent.session :as session]
            [accountant.core :as accountant]))

(def server "http://localhost:3000/")

(defn get-by-id [id] (.getElementById js/document id))

(defn set-by-id [id value]
  (set! (.-value (.getElementById js/document id)) value))

(defn log [& msg]
  (.log js/console (apply str msg)))

(defn alert [msg]
  (js/alert msg))

(def user (atom "rated"))

(def all-tasks (r/atom []))

(defn show-output [params]
  (log "!!!!!" params)
  (reset! all-tasks params)
  (log @all-tasks)
  #_(secretary/dispatch! "/todo"))

(defn error-output [params]
  (log "@@@ error" params))

(defn show-hiccup []
  [:div
   (doall (for [i (range (count @all-tasks))]
            ^{:key i} [:div
                       [:input {:disabled true
                                     :id i
                                     :type "text"
                                     :value ((nth @all-tasks i) :task-name)}]
                       [:button {:on-click (fn [e]
                                        #_(log "^^^^" @user)
                                        (let [task ((nth @all-tasks i) :task-name)]
                                          (GET (str server "done-todo")
                                               {:params {:task-name task
                                                         :user @user}
                                                :format :json
                                                :response-format :json
                                                :keywords? true
                                                :handler show-output
                                                :error-handler error-output}))) :class "btn btn-danger"} "done"]
                       [:button {:on-click #(session/put! :page :update) :class "btn btn-primary"} "update"]]))])

(defn home-page []
  [:div
   [:h2 "Please enter the task"]
   [:form {:action "#"
           :on-submit (fn [e]
                        (let [task (.-value (get-by-id "input-str"))]
                          (log "###" task "^^^^" @user)
                          (GET (str server "todo")
                               {:params {:task-name task
                                         :user @user}
                                :format :json
                                :response-format :json
                                :keywords? true
                                :handler show-output
                                :error-handler error-output})))}
    [:div [:input {:type "text" :id "input-str" :placeholder "enter your task"}]]
    [:div [:input {:type "submit" :value "Submit" :class "btn btn-primary"}]]
    [:div [:input {:type "button" :value "Logout" :on-click #(session/put! :page :login) :class "btn btn-primary"}]]]
   [:div [show-hiccup]]])


(defn update-page []
  [:div
   [:h2 "Please udpate the task"]
   [:form {:action "#"
           :on-submit (fn [e]
                        (let [task-old (.-value (get-by-id "input-str1"))
                              task-upd (.-value (get-by-id "input-str2"))]
                          (log "###" task-old "^^^^" @user)
                          (GET (str server "upd-todo")
                               {:params {:task-name-old task-old
                                         :task-name-upd task-upd
                                         :user @user}
                                :format :json
                                :response-format :json
                                :keywords? true
                                :handler show-output
                                :error-handler error-output})))}
    [:input {:type "text" :id "input-str1" :placeholder "enter your old task"}]
    [:input {:type "text" :id "input-str2" :placeholder "enter your updated task"}]
    [:input {:type "submit" :value "Update" :class "btn btn-primary"}]]
   [:div [show-hiccup]]])

(defn show-login [params]
  (log "login: " params)
  (if (not= (count params) 1)
    (do
      (session/put! :page :login)
      (alert "user name or password incorrect"))
    (do
      (reset! user (:user (first params)))
      (log @user)
      (GET (str server "todos")
           {:params {:user @user}
            :format :json
            :response-format :json
            :keywords? true
            :handler show-output
            :error-handler error-output})
      (session/put! :page :home))))

(defn error-login [params]
  (log "*** error" params))

(defn login-page []
  [:div
   [:h2 "Please login"]
   [:form {:action "#"
           :on-submit (fn [e]
                        (let [user-name (.-value (get-by-id "ip-user"))
                              password (.-value (get-by-id "ip-pass"))]
                          (log "###" user-name "@@@" password)
                          (GET (str server "login")
                               {:params {:user user-name
                                         :pass password}
                                :format :json
                                :response-format :json
                                :keywords? true
                                :handler show-login
                                :error-handler error-login})))}
    [:div [:input {:type "text" :id "ip-user" :placeholder "enter user name"}]]
    [:div [:input {:type "text" :id "ip-pass" :placeholder "enter password"}]]
    [:div [:input {:type "submit" :value "Submit" :class "btn btn-primary"}]]]])

(def pages
  {:home #'home-page
   :login #'login-page
   :update #'update-page})

(defn page []
  (log (session/get :page))
  [(pages (session/get :page))])

;;--------------------------------

;; Routes

(secretary/set-config! :prefix "/?#")

(secretary/defroute "/todo" []
  (session/put! :page :home))

(secretary/defroute "/" []
  (session/put! :page :login))

(secretary/defroute "/update" []
  (session/put! :page :update))

;;;;;;;;;;;;;;;;;;;;;;;;

#_(defn show-page []
  [:div.container
   (condp = (get @page :page)
       :login [login-page]
       :todo [home-page]
       :update [update-page])])

(defn mount-components []
  (r/render [#'page] (.getElementById js/document "app")))

(defn init! []
  (session/put! :page :login)
  (accountant/configure-navigation!
   {:nav-handler
    (fn [path]
      (secretary/dispatch! path))
    :path-exists?
    (fn [path]
      (secretary/locate-route path))})
  (accountant/dispatch-current!)
  (mount-components))
