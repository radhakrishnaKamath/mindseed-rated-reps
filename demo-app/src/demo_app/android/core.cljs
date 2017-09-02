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
(def input       (r/adapt-react-class (.-TextInput ReactNative)))
(def logo-img (js/require "./images/cljs.png"))

;; Material design / vector icons
;; ==================================================================
(def MaterialIcon      (js/require "react-native-vector-icons"))
(def icon              (r/adapt-react-class MaterialIcon))

(def p (js/require "react-native-transformable-image"))
(def photo (r/adapt-react-class (.-default p)))


;; Material design
;; ==================================================================
(set! js/MaterialDesign (js/require "react-native-material-ui"))
(def COLOR              (js->clj (.-COLOR js/MaterialDesign)   :keywordize-keys true))
(def PRIMARY_COLORS    (vec (map keyword (js->clj (.-PRIMARY_COLORS js/MaterialDesign)))))
(def ThemeProvider (r/adapt-react-class (.-ThemeProvider js/MaterialDesign)))
(def uiTheme {:palette {:primaryColor (.-green500 COLOR) :accentColor (.-pink500 COLOR)}})

;;(def card              (r/adapt-react-class (.-Card            js/MaterialDesign)))
;;(def card-body         (r/adapt-react-class (.-Card.Body       js/MaterialDesign)))
;;(def card-media        (r/adapt-react-class (.-Card.Media      js/MaterialDesign)))
;;(def card-actions      (r/adapt-react-class (.-Card.Actions    js/MaterialDesign)))

;;(def avatar            (r/adapt-react-class (.-Avatar          js/MaterialDesign)))

(def button              (r/adapt-react-class (.-Button          js/MaterialDesign)))
;;(def drawer-layout     (r/adapt-react-class (.-Drawer          js/MaterialDesign)))
;;(def drawer-header     (r/adapt-react-class (.-Drawer.Header   js/MaterialDesign)))
;;(def drawer-section    (r/adapt-react-class (.-Drawer.Section  js/MaterialDesign)))
;;(def Toolbar           (r/adapt-react-class (.-Toolbar         js/MaterialDesign)))

(defonce fields (r/atom {:user nil :pwd nil}))

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

  #_ [card {:style {:background-color "red"}}
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

(defn get-ref [component key]
  ((js->clj (.-refs component)) key))

(defn app-pages []
  (let [greeting (subscribe [:get-greeting])]
    (fn []

      [ThemeProvider {:uiTheme uiTheme}
       [view {:style {:flex-direction "column"
                      :margin 40
                      :align-items "center"}}
        [photo {:source {:uri "https://raw.githubusercontent.com/yoaicom/resources/master/images/game_of_thrones_1.jpg"}
                :style {:width 300
                        :height 300}}]
        [button {:primary true
                 :text "login"}]]])))


#_(alert (str @fields))

(defn app-root []
  (let [current-page (subscribe [:get-page])]
    (fn []
      (condp = @current-page
        :app-root [app-pages]
        :app-next [app-next]))))

(defn init []
  (dispatch-sync [:initialize-db])
  (.registerComponent app-registry "DemoApp" #(r/reactify-component app-root)))

#_(.focus (get-ref page "password"))
#_[input {
                    :ref                    "user"
                    :placeholder            "User name"
                    :placeholder-text-color "gray"
                    :return-key-type        "next"
                    :on-end-editing         (fn [value]
                                              (swap! fields assoc :user (-> value
                                                                            .-nativeEvent
                                                                            .-text)))
                    :on-submit-editing      (fn [value]
                                              (swap! fields assoc :user (-> value
                                                                            .-nativeEvent
                                                                            .-text))
                                              ())
                                        ; For some reason this is in android avd painfully slow solution
                                        ;:on-change-text  (fn [value] (swap! fields assoc :user value))
                                        ;:value         (:user @fields)
                    :style {:width 200}} ]
         #_[input {
                    :ref               "password"
                    :placeholder       "Password"
                    :return-key-type   (if (= (count (:user @fields)) 0) "next" "done")
                    :secure-text-entry true
                    :on-end-editing    (fn [value]
                                         (swap! fields assoc :pwd (-> value
                                                                      .-nativeEvent
                                                                      .-text)))
                    :on-submit-editing (fn [value]
                                         (swap! fields assoc :pwd (-> value
                                                                      .-nativeEvent
                                                                      .-text))
                                         #_(dispatch [:auth/login @fields]))
                                        ;:on-change-text  (fn [value] (swap! fields assoc :pwd value))
                                        ;:value           (:pwd @fields)
                   :style             {:width 200}} ]
