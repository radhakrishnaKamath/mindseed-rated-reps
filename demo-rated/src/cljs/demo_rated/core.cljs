(ns demo-rated.core
  (:require [reagent.core :as r]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            [markdown.core :refer [md->html]]
            [demo-rated.ajax :refer [load-interceptors!]]
            [ajax.core :refer [GET POST]]
            [cljsjs.dragula :as dragula]
            [reagent.validation :as utils]
            [cljs-http.client :as http]
            [cljs.core.async :refer (<!)]
            [goog.events :as events]
            [reagent-material-ui.core :as ui])
  (:import  [goog.events EventType]))

(enable-console-print!)

;; some helpers
(def el reagent/as-element)
(defn icon [nme] [ui/FontIcon {:className "material-icons"} nme])
(defn color [nme] (aget ui/colors nme))

;; create a new theme based on the dark theme from Material UI
(defonce theme-defaults {:muiTheme (ui/getMuiTheme
                                    (-> ui/darkBaseTheme
                                        (js->clj :keywordize-keys true)
                                        (update :palette merge {:primary1Color (color "amber500")
                                                                :primary2Color (color "amber700")})
                                        clj->js))})

(defn simple-nav []
  (let [is-open? (atom false)
        close #(reset! is-open? false)]
    (fn []
      [:div
       [ui/AppBar {:title "yipgo" :onLeftIconButtonTouchTap #(reset! is-open? true)}]
       [ui/Drawer {:open @is-open? :docked false}
        [ui/List
         [ui/ListItem {:leftIcon (el [:i.material-icons "home"])
                       :on-click (fn []
                                   (accountant/navigate! "/")
                                   (close))}
          "Home"]
         [ui/Divider]
         (for [[doc details] @(rf/subscribe [:docs.list.by-name])]
           ^{:key doc} [ui/ListItem {:secondaryText "Something something"
                                     :rightIconButton (el [ui/IconMenu {:iconButtonElement (el [ui/IconButton {:touch true} [icon "more_vert"]])}
                                                           [ui/MenuItem "Delete"]])
                                     :onTouchTap (fn []
                                                   ;; some action or another, then close the menu
                                                   (close))}
                        doc])]
        [new-doc-modal close]]])))

(defn home-page []
  [ui/MuiThemeProvider theme-defaults
   [:div
    [simple-nav]
    [:div
     [:h2 "Welcome to a simple, example application."]]]])

;; Constants and references

(defn drag-move-fn [on-drag]
  (fn [evt]
    (.preventDefault evt) ;; otherwise we select text while resizing
    (on-drag (.-clientX evt) (.-clientY evt))))

(defn drag-end-fn [drag-move drag-end]
  (fn [evt]
    (events/unlisten js/window EventType.MOUSEMOVE drag-move)
    (events/unlisten js/window EventType.MOUSEUP @drag-end)))

(defn dragging [on-drag]
  (let [drag-move (drag-move-fn on-drag)
        drag-end-atom (atom nil)
        drag-end (drag-end-fn drag-move drag-end-atom)]
    (reset! drag-end-atom drag-end)
    (events/listen js/window EventType.MOUSEMOVE drag-move)
    (events/listen js/window EventType.MOUSEUP drag-end)))

(defn recursive-merge
  "Recursively merge hash maps."
  [a b]
  (if (and (map? a) (map? b))
    (merge-with recursive-merge a b)
    b))


(defn sort-fn
  [rows column ascending?]
  (let [sorted (sort-by (fn [r]
                          (let [cell (nth r column)]
                            (get (meta cell) :data cell))) rows)]
    (if ascending? sorted (rseq sorted))))

(def default-configs {:table
                      {:style {:width nil}
                       :thead
                       {:tr
                        {
                         :th
                         {:style {;:transition "all 0.2s ease-in-out;"
                                  ;; :-moz-user-select "none"
                                  ;; :-webkit-user-select "none"
                                  ;; :-ms-user-select "none"
                                  }}}}}})

(defn reorder-column! [source-atom c1 c2]
  (let [swap-vec (fn [v i1 i2]
                   (let [v (vec v)]
                     (assoc v i2 (nth v i1) i1 (nth v i2))))
        source @source-atom
        updated-source (-> source
                           (update-in [:headers] swap-vec c1 c2)
                           (update-in [:rows] (fn [s] (mapv #(swap-vec % c1 c2) s))))]
    (reset! source-atom updated-source)))

(defn resize-widget [cell-container]
  [:span {:style {:display "inline-block"
                  :width "8"
                  :position "absolute"
                  :cursor "ew-resize"
                  :height "100%"
                  :top 0
                  :right 0
                  ;:background-color "black" ;; for debug
                  }
          :on-click #(.stopPropagation %)
          :on-mouse-down #(let [cell-node (r/dom-node cell-container)
                                init-x (.-clientX %)
                                init-width (.-clientWidth cell-node)]
                            (dragging
                             (fn [x _]
                               (aset cell-node "width" (- init-width (- init-x x)))))
                            (.preventDefault %))}])


(defn header-cell-fn [i n configs source-atom state-atom]
  (let [
        state @state-atom
        col-hidden (:col-hidden state)
        col-state-a (r/cursor state-atom [:col-state n])
        sort-click-fn (fn []
                        (let [sorting (-> (swap! state-atom update-in [:sorting]
                                                 #(if-not (= [n :asc] %)
                                                    [n :asc]
                                                    [n :desc]))
                                          (get-in [:sorting 1]))]
                          (swap! source-atom update-in [:rows]
                                 sort-fn n (= sorting :asc))))]
    [:th
     (recursive-merge
      (get configs :th)
      {;:width (str (get @col-state-a :width) "px") ;; <--------
       :draggable true
       :on-drag-start #(do (doto (.-dataTransfer %)
                             (.setData "text/plain" "")) ;; for Firefox
                           (swap! state-atom assoc :col-reordering true))
       :on-drag-over #(swap! state-atom assoc :col-hover n)
       :on-drag-end #(let [col-hover (:col-hover @state-atom)
                           col-sorting (first (get @state-atom :sorting))]
                       (when-not (= n col-hover) ;; if dropped on another column
                         (reorder-column! source-atom n col-hover)
                         (when (some #{col-sorting} [n col-hover])
                           ;; if any of them is currently sorted
                           (swap! state-atom update-in
                                  [:sorting]
                                  (fn [[col-n sort-type]]
                                    [(if (= col-n n) col-hover n) sort-type]))))
                       (swap! state-atom assoc
                              :col-hover nil
                              :col-reordering nil))
       :style (merge {:position "relative"
                      :cursor "move"
                      :display (when (get col-hidden n) "none")}
                     (when (and (:col-reordering state)
                                (= n (:col-hover state)))
                       {:border-right "6px solid #3366CC"}))})
     [:span {:style {:padding-right 50}} i]
     [:span {:style {:position "absolute"
                     :text-align "center"
                     :height "1.5em"
                     :width "1.5em"
                     :right "15px"
                     :cursor "pointer"}
             :on-click sort-click-fn}
      (condp = (get state :sorting)
        [n :asc] " ▲"
        [n :desc] " ▼"
        ;; and to occupy the same space...
        [:span {:style {:opacity "0.3"}}
         " ▼"])]
     [resize-widget (r/current-component)]]))


(defn header-row-fn [items configs source-atom state-atom]
  (let [state @state-atom]
    [:tr
     (for [[n i] (map-indexed (fn [& args] args) items)]
       ^{:key n}
       [header-cell-fn i n configs source-atom state-atom])]))


(defn row-fn [item state-atom]
  (let [comp (r/current-component)
        state @state-atom
        col-hidden (:col-hidden state)]
  [:tr ;; {:draggable true
       ;;  :on-drag-start #(do (doto (.-dataTransfer %)
       ;;                        (.setData "text/plain" "") ;; for Firefox
       ;;                        ))}
   (doall
    (map-indexed (fn [n i]
                   ^{:key n}
                   [:td {:on-drag-over #(swap! state-atom assoc :col-hover n)
                         :style {:border-right (when (and (:col-reordering state)
                                                          (= n (:col-hover state)))
                                                 "2px solid #3366CC")
                                 :display (when (get col-hidden n) "none")}}
                    i]) item))]))

(defn rows-fn [rows state-atom]
  (let [comp (r/current-component)]
    (doall (map-indexed
            (fn [n i]
              ^{:key n}
              [row-fn i state-atom]) rows))))



(defn rows-selector [data-atom state-atom configs]
  (let [headers (:headers @data-atom)
        hidden-rows (r/cursor state-atom [:col-hidden])]
    [:ul (:ul configs)
     (doall
      (for [[col-n i] (map-indexed #(vector %1 %2) headers)
            :let [hidden-a (r/cursor hidden-rows [col-n])
                  li-config (get-in configs [:ul :li])]]
        ^{:key col-n}
        [:li (recursive-merge
              {:style {:margin 8
                       :cursor "pointer"}
               :on-click #(do (swap! hidden-a not) nil)}
              li-config)
         i " "(if @hidden-a "☐" "☑")]))]))



(defn reagent-table
  "Optional properties map include :table :thead and :tbody.
  For example:
  (reagent-table ... {:table {:class \"table-striped\"}})"
  ([data-or-data-atom] (reagent-table data-or-data-atom {}))
  ([data-or-data-atom table-configs]
   (let [table-configs (recursive-merge default-configs table-configs)
         data-atom (if-not (satisfies? IAtom data-or-data-atom)
                     (r/atom data-or-data-atom)
                     data-or-data-atom)
         state-atom (r/atom {})] ;; a place for the table local state
     (assert (let [data @data-atom]
               (and (:headers data)
                    (:rows data)))
             "Missing :headers or :rows in the provided data.")
     (fn []
       (let [data @data-atom]
         [:div
          [:style (str ".reagent-table * table {table-layout:fixed;}"
                       ".reagent-table * td { max-width: 3px;"
                       "overflow: hidden;text-overflow: ellipsis;white-space: nowrap;}")]
          (when-let [selector-config (:rows-selection table-configs)]
            ;;(js/console.log (str selector-config))
            [rows-selector data-atom state-atom selector-config])
          [:table.reagent-table (:table table-configs)
           (when-let [caption (:caption table-configs)]
             caption)
           [:thead (:thead table-configs)
            (header-row-fn (:headers data)
                           (get-in table-configs [:table :thead :tr])
                           data-atom
                           state-atom)]
           [:tbody (:tbody table-configs)
            (rows-fn (:rows data) state-atom)]]])))))

(def black-hole-pos {:x 400 :y 400})

(def intial-pos {:x 200 :y 200})

(def final-pos {:x 600 :y 600})

(def draggable (r/atom {:x 100 :y 100 :alive? true}))

(defn nav-link [uri title page collapsed?]
  [:li.nav-item
   {:class (when (= page (session/get :page)) "active")}
   [:a.nav-link
    {:href uri
     :on-click #(reset! collapsed? true)} title]])

(defn navbar []
  (let [collapsed? (r/atom true)]
    (fn []
      [:nav.navbar.navbar-dark.bg-primary
       [:button.navbar-toggler.hidden-sm-up
        {:on-click #(swap! collapsed? not)} "☰"]
       [:div.collapse.navbar-toggleable-xs
        (when-not @collapsed? {:class "in"})
        [:a.navbar-brand {:href "#/"} "demo-rated"]
        [:ul.nav.navbar-nav
         [nav-link "#/" "Home" :home collapsed?]
         [nav-link "#/about" "About" :about collapsed?]]]])))

;; Utility functions

(defn close? [x y]
  (and (< (Math/abs (- x (:x black-hole-pos))) 50)
       (< (Math/abs (- y (:y black-hole-pos))) 50)))

(defn show-drag-down [y]
  (> y (:y black-hole-pos)))

(defn show-drag-up [y]
  (< y (:y black-hole-pos)))

(defn get-client-rect [evt]
  (let [r (.getBoundingClientRect (.-target evt))]
    {:left (.-left r), :top (.-top r)}))


;; Event handlers

(defn mouse-move-handler [offset]
  (fn [evt]
    (let [x (- (.-clientX evt) (:x offset))
          y (- (.-clientY evt) (:y offset))]
      (if (close? x y)
        (reset! draggable {:alive? false})
        (if (show-drag-down y)
          (reset! draggable {:x      x
                             :y      y
                             :alive? true})
          (if (show-drag-up y)
            (reset! draggable {:x      x
                               :y      y
                               :alive? true})))))))


(defn mouse-up-handler [on-move]
  (fn me [evt]
    (events/unlisten js/window EventType.MOUSEMOVE
                     on-move)))


(defn mouse-down-handler [e]
  (let [{:keys [left top]} (get-client-rect e)
        offset             {:x (- (.-clientX e) left)
                            :y (- (.-clientY e) top)}
        on-move            (mouse-move-handler offset)]
    (events/listen js/window EventType.MOUSEMOVE
                   on-move)
    (events/listen js/window EventType.MOUSEUP
                   (mouse-up-handler on-move))))

;; Rea[ct|gent] components

(defn draggable-button []
  [:div
   [:h1 (pr-str @draggable)]
   [:div {:class "left1" :on-mouse-down mouse-down-handler}
    #_[:button.btn.btn-default
     {:style         {:position "absolute"
                      :left     (str (:x @draggable) "px") ;
                      :top      (str (:y @draggable) "px")}
      :on-mouse-down mouse-down-handler
      :onDrop (fn [e]
                (.log js/console "@@@@@")
                (after-drag (:y @draggable)))}
       "Drag me"]
    [:p {:on-drag-start mouse-down-handler :draggable true :id "dragtarget"} "drag me"]]
   [:div {:class "center1"}]
   [:div {:class "right1"}]])

(defn about-page []
  [:div.container
   [:div.row {:id "left"}
    [:div.col-md-12
     [:img {:src (str js/context "/img/warning_clojure.png")}]]]
   [:div.row {:id "right"}
    [:div.col-md-12
     [:input {:type "text" :value (utils/is-email? "rkkamath95@gmail.com")}]]]])

(defn side-nav []
  [:ul {:id "slide-out" :class "side-nav"}
   [:li [:div {:class "user-view"}
         [:div {:class "background"}
          [:img {:src (str js/context "/img/warning_clojure.png")}]]]]
   [:li [:a {:href "#!"} [:i {:class "material-icons"} "cloud"] "first link"]]
   [:li [:a {:href "#!"} "second link"]]
   [:li [:div {:class "divider"}]]
   [:li [:a {:class "subheader"} "sub-header"]]
   [:li [:a {:class "waves-effect" :href "#!"} "waves-effect"]]])

(def pages
  {:home #'draggable-button
   :about #'about-page})

(defn page []
  (println " trying to print home" (session/get :page))
  [(pages (session/get :page))])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (session/put! :page :home))

(secretary/defroute "/about" []
  (session/put! :page :about))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  #_(doto (History.)
      (events/listen
       HistoryEventType/NAVIGATE
       (fn [event]
         (secretary/dispatch! (.-token event))))
      (.setEnabled true)))

;; -------------------------
;; Initialize app

(defn mount-components []
  (r/render [#'navbar] (.getElementById js/document "navbar"))
  (r/render [side-nav] (.getElementById js/document "app")))

(defn init! []
  (load-interceptors!)
  (hook-browser-navigation!)
  (mount-components))
