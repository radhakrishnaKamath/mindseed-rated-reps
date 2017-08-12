(ns pinned-notes.core
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [secretary.core :as secretary]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            [markdown.core :refer [md->html]]
            [ajax.core :refer [GET POST]]
            [pinned-notes.ajax :refer [load-interceptors!]]
            [pinned-notes.handlers]
            [pinned-notes.subscriptions])
  (:import goog.History))

(def server "http://localhost:3000/")

(defn get-by-id [id] (.getElementById js/document id))

(defn set-by-id [id value]
  (set! (.-value (.getElementById js/document id)) value))

(defn log [& msg]
  (.log js/console (apply str msg)))

(defn alert [msg]
  (js/alert msg))

(defn show-current [params]
  (log "!" params)
  (rf/dispatch [:set-tasks params]))

(defn error-current [params]
  (log "@ error" params))

(defn show-done [params]
  (log "##!!" params)
  (rf/dispatch [:set-done-tasks params]))

(defn error-done [params]
  (log "@## error" params))

(defn nav-link [uri title page collapsed?]
  (let [selected-page (rf/subscribe [:page])]
    (when (= :home @selected-page)
      (GET (str server "tasks")
           {:format :json
            :response-format :json
            :keywords? true
            :handler show-current
            :error-handler error-current}))
    (when (= :done @selected-page)
      (GET (str server "finished-task")
           {:format :json
            :response-format :json
            :keywords? true
            :handler show-done
            :error-handler error-done}))
    [:li.nav-item
     {:class (when (= page @selected-page) "active")}
     [:a.nav-link
      {:href uri
       :on-click #(reset! collapsed? true)} title]]))

(defn navbar []
  (r/with-let [collapsed? (r/atom true)]
    [:nav.navbar.navbar-dark.bg-primary.navbar-fixed-top
     [:button.navbar-toggler.hidden-sm-up
      {:on-click #(swap! collapsed? not)} "☰"]
     [:div.collapse.navbar-toggleable-xs.flex-endd
      (when-not @collapsed? {:class "in"})
      [:a.navbar-brand {:href "#/"} "Rated Pinned Notes"]
      [:ul.nav.navbar-nav
       [nav-link "#/" "Your Notes" :home collapsed?]
       [nav-link "#/add-notes" "Add a Notes" :add collapsed?]
       [nav-link "#/done-notes" "Completed Notes" :see-done collapsed?]
       [:div {:class "col-sm-3 col-md-3" :id "custom-search-input"}
        [:form {:class "navbar-form" :role "search"}
         [:div {:class "input-group col-md-12"}
          [:input {:type "search" :class "form-control input-lg" :placeholder "search author"}]
          [:span {:class "input-group-btn"}
           [:button {:class "btn btn-default btn-sm" :type "submit" }
            [:img {:src (str js/context "/img/magnify.png")}]]]]]]]]]))

(defn show-output [params]
  (log "!!!!!" params)
  (rf/dispatch [:set-tasks params]))

(defn error-output [params]
  (log "@@@ error" params))

(defn add-notes-page []
  [:div.container
   [:div.row
    [:div.col-md-12
     [:form {:action "#"
             :on-submit (fn [e]
                          (let [task (.-value (get-by-id "task"))
                                author (.-value (get-by-id "author"))
                                task-desc (.-value (get-by-id "description"))]
                            (GET (str server "task")
                                 {:params {:task-name task
                                           :desc task-desc
                                           :author author}
                                  :format :json
                                  :response-format :json
                                  :keywords? true
                                  :handler show-output
                                  :error-handler error-output})
                            (rf/dispatch [:set-active-page :home])))}
      [:div [:input {:type "text"
                     :id "author"
                     :placeholder "Author of Notes"
                     :class "form-control"}]]
      [:div [:input {:type "text"
                     :id "task"
                     :placeholder "Task Heading"
                     :class "form-control"}]]
      [:div [:textarea {:rows "10"
                        :cols "10"
                        :id "description"
                        :placeholder "Task Description"
                        :class "form-control"}]]
      [:div [:input {:type "submit"
                     :value "Submit"
                     :class "btn btn-primary"}]]]]]])

(defn done-notes-page []
  [:div.container
   [:div.row
    [:div.col-md-12
     [:div {:class "flex-class"}
      (doall (for [i (range (count @(rf/subscribe [:done-tasks])))]
               ^{:key i} [:div {:class "card"}
                          (let [task ((nth @(rf/subscribe [:done-tasks]) i)
                                      :task-name)
                                task-desc ((nth @(rf/subscribe [:done-tasks]) i)
                                           :task-desc)
                                author ((nth @(rf/subscribe [:done-tasks]) i)
                                        :author)]
                            [:div {:class "card-block"}
                             [:h4 {:class "card-title white-color"} task]
                             [:p {:class "card-text white-color"} task-desc]
                             [:p {:class "card-text white-color"} author]])]))]]]])

(defn home-page []
  [:div.container
   [:div.flex-class
    (doall (for [i (range (count @(rf/subscribe [:tasks])))]
             ^{:key i} [:div {:class "card bg-info"}
                        (let [task ((nth @(rf/subscribe [:tasks]) i) :task-name)
                              task-desc ((nth @(rf/subscribe [:tasks]) i) :task-desc)
                              author ((nth @(rf/subscribe [:tasks]) i) :author)]
                          [:div {:class "card-block"}
                           [:div {:class "my-flex"}
                            [:div [:h4 {:class "card-title"} task]
                             [:p {:class "card-text"} task-desc]
                             [:p {:class "card-text"} author]]
                            [:div.options
                             [:img {:src (str js/context "/img/check.png")
                                    :on-click (fn [e]
                                                (GET (str server "done-task")
                                                     {:params {:task-name task
                                                               :author author}
                                                      :format :json
                                                      :response-format :json
                                                      :keywords? true
                                                      :handler show-current
                                                      :error-handler error-current}))}]
                             [:img {:src (str js/context "/img/close.png")
                                    :on-click (fn [e]
                                                (GET (str server "del-task")
                                                     {:params {:task-name task
                                                               :author author}
                                                      :format :json
                                                      :response-format :json
                                                      :keywords? true
                                                      :handler show-current
                                                      :error-handler error-current}))}]
                             [:img {:src (str js/context "/img/pencil.png")
                                    :on-click (fn [e]
                                                (rf/dispatch [:set-update-data {:task-name task :author author :desc task-desc}])
                                                (rf/dispatch [:set-active-page :update]))}]]]])]))]])

(defn update-page []
  [:div.container
   [:div.row
    [:div.col-md-12
     (let [task (:task-name @(rf/subscribe [:get-update-data]))
           author (:author @(rf/subscribe [:get-update-data]))]
       [:form {:action "#"
               :on-submit (fn [e]
                            (let [task-desc (.-value (get-by-id "description"))]
                              (GET (str server "update-task")
                                   {:params {:task-name task
                                             :desc task-desc
                                             :author author}
                                    :format :json
                                    :response-format :json
                                    :keywords? true
                                    :handler show-output
                                    :error-handler error-output})
                              (rf/dispatch [:set-active-page :home])))}
        [:div [:input {:type "text"
                       :id "author"
                       :disabled true
                       :placeholder author
                       :class "form-control"}]]
        [:div [:input {:type "text"
                       :id "task"
                       :placeholder task
                       :disabled true
                       :class "form-control"}]]
        [:div [:textarea {:rows "10"
                          :cols "10"
                          :id "description"
                          :placeholder (:desc @(rf/subscribe [:get-update-data]))
                          :class "form-control"}]]
        [:div [:input {:type "submit"
                       :value "Submit"
                       :class "btn btn-primary"}]]])]]])

(def pages
  {:home #'home-page
   :add #'add-notes-page
   :done #'done-notes-page
   :update #'update-page})

(defn page []
  [:div
   [navbar]
   [(pages @(rf/subscribe [:page]))]])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (rf/dispatch [:set-active-page :home]))

(secretary/defroute "/add-notes" []
  (rf/dispatch [:set-active-page :add]))

(secretary/defroute "/done-notes" []
  (rf/dispatch [:set-active-page :done]))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
      HistoryEventType/NAVIGATE
      (fn [event]
        (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

;; -------------------------
;; Initialize app

(defn mount-components []
  (rf/clear-subscription-cache!)
  (r/render [#'page] (.getElementById js/document "app")))

(defn init! []
  (rf/dispatch-sync [:initialize-db])
  (load-interceptors!)
  (hook-browser-navigation!)
  (mount-components))
