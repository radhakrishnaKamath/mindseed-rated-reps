(ns demo-app.android.core
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [demo-app.events]
            [demo-app.subs]))

(def ReactNative (js/require "react-native"))

(def app-registry (.-AppRegistry ReactNative))
(def text (r/adapt-react-class (.-Text ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def image (r/adapt-react-class (.-Image ReactNative)))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight ReactNative)))

(def logo-img (js/require "./images/cljs.png"))

;; Material design / vector icons
;; ==================================================================
(def MaterialIcon      (js/require "react-native-vector-icons"))
(def icon              (r/adapt-react-class MaterialIcon))

;; Material design
;; ==================================================================
(set! js/MaterialDesign (js/require "react-native-material-design"))
(def COLOR              (js->clj (.-COLOR js/MaterialDesign)   :keywordize-keys true))

(def card              (r/adapt-react-class (.-Card            js/MaterialDesign)))
(def card-body         (r/adapt-react-class (.-Card.Body       js/MaterialDesign)))
(def card-media        (r/adapt-react-class (.-Card.Media      js/MaterialDesign)))
(def card-actions      (r/adapt-react-class (.-Card.Actions    js/MaterialDesign)))

(def avatar            (r/adapt-react-class (.-Avatar          js/MaterialDesign)))

(def button            (r/adapt-react-class (.-Button          js/MaterialDesign)))
(def drawer-layout     (r/adapt-react-class (.-Drawer          js/MaterialDesign)))
(def drawer-header     (r/adapt-react-class (.-Drawer.Header   js/MaterialDesign)))
(def drawer-section    (r/adapt-react-class (.-Drawer.Section  js/MaterialDesign)))
(def Toolbar           (r/adapt-react-class (.-Toolbar         js/MaterialDesign)))

(defn alert [title]
  (.alert (.-Alert ReactNative) title))

(defn app-next []
  [view {:style {:flex-direction "column"
                 :margin 40
                 :align-items "center"}}
   [text {:style {:font-size 30
                  :font-weight "100"
                  :margin-bottom 20
                  :text-align "center"}}
    "yash and dhiren are best friends"]

   [card {:style {:background-color "red"}}
    [card-body
     [text {:style {:font-weight "bold"
                    :font-size 16}}
      "hello yash"]]]

   [touchable-highlight {:style {:background-color "#999"
                                 :padding 10
                                 :border-radius 5}
                         :on-press #(dispatch [:set-page :app-root])}
    [text {:style {:color "white"
                   :text-align "center"
                   :font-weight "bold"}}
     "back"]]])

(defn app-root []
  (let [current-page (subscribe [:get-page])]
    (fn []
      (condp = @current-page
        :app-root [app-pages]
        :app-next [app-next]))))

(defn app-pages []
  (let [greeting (subscribe [:get-greeting])]
    (fn []
      [view {:style {:flex-direction "column"
                     :margin 40
                     :align-items "center"}}
       [text {:style {:font-size 30
                      :font-weight "100"
                      :margin-bottom 20
                      :text-align "center"}}
        "yash and kartik are best friends"]
       [image {:source logo-img
               :style  {:width 80
                        :height 80
                        :margin-bottom 30}}]

       [touchable-highlight {:style {:background-color "#999"
                                     :padding 10
                                     :border-radius 5}
                             :on-press #(dispatch [:set-page :app-next])}
        [text {:style {:color "white"
                       :text-align "center"
                       :font-weight "bold"}}
         "front"]]])))

(defn init []
  (dispatch-sync [:initialize-db])
  (.registerComponent app-registry "DemoApp" #(r/reactify-component app-root)))
