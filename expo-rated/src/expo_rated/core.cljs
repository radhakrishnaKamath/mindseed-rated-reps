(ns expo-rated.core
    (:require [reagent.core :as r :refer [atom]]
              [re-frame.core :refer [subscribe dispatch dispatch-sync]]
              [expo-rated.handlers]
              [expo-rated.subs]))

(def ReactNative (js/require "react-native"))

(def app-registry (.-AppRegistry ReactNative))
(def text (r/adapt-react-class (.-Text ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def image (r/adapt-react-class (.-Image ReactNative)))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight ReactNative)))
(def Alert (.-Alert ReactNative))
(def platform (.-Platform ReactNative))

(def expo (js/require "expo"))
(def camera (r/adapt-react-class (.-Camera expo)))
(def bar-code-scanner (r/adapt-react-class (.-BarCodeScanner expo)))
(def secure-store (.-SecureStore expo))

(def overlay (js/require "react-native-modal-overlay"))
(def modal (r/adapt-react-class (.-default overlay)))

(def rnls (js/require "react-native-loading-spinner-overlay"))
(def spinner (r/adapt-react-class (.-default rnls)))

(def show (r/atom {:show-modal false}))

(defn call-modal []
  (swap! show assoc :show-modal true))

(defn close-modal []
  (swap! show assoc :show-modal false))

(defn alert [title]
  (.alert Alert title))

(defn app-root []
  (let [a (r/atom 0) #_(subscribe [:get-a])]
    (fn []
      (let [b @a]
        [view {:style {:flex-direction "column" :margin 40 :align-items "center"}}
         [image {:source (js/require "./assets/images/cljs.png")
                 :style {:width 200
                         :height 200}}]

         [touchable-highlight {:style {:background-color "#999" :padding 10 :border-radius 5}
                               :on-press #(swap! a inc) #_(dispatch [:set-a (inc b)])}
          [text {:style {:color "white" :text-align "center" :font-weight "bold"}} "increment"]]

         [text {:style {:font-size 30 :font-weight "200" :margin-top 100 :text-align "center"}} b]]))))

(defn init []
  (dispatch-sync [:initialize-db])
  (.registerComponent app-registry "main" #(r/reactify-component app-root)))

#_[view {:flex 1}
   [bar-code-scanner {:onBarCodeRead #(alert ((js->clj %) "data"))'
                      :style {:height "100%"
                              :width "100%"}}]]

#_[view
   [modal {:closeOnTouchOutside true
           :animationType "fade"
           :visible (:show-modal @show)
           :onClose #(close-modal)
           }
    [text "hey"]]]

                               #_(call-modal) #_(fn [a]
                                                  (.catch
                                                   (.then
                                                    (.setItemAsync secure-store
                                                                   "expo" "exxpo-id" (clj->js {}))
                                                    #(println %))
                                                   #(println "Caugth mc")))

#_[view [spinner {:visible (:show-modal @show) :size "small" :color "#000000"}]]

         #_[touchable-highlight {:style {:background-color "#999" :padding 10 :border-radius 5}
                                 :on-press #_(close-modal) (fn [a]
                                                             (.catch
                                                              (.then
                                                               (.getItemAsync secure-store
                                                                              "expo" (clj->js {}))
                                                               #(println "gkufhgff" %))
                                                              #(println "Caugth mc")))}
            [text {:style {:color "white" :text-align "center" :font-weight "bold"}} "get me"]]
         #_[touchable-highlight {:style {:background-color "#999" :padding 10 :border-radius 5}
                                 :on-press (fn [] nil)}
            [text {:style {:color "white" :text-align "center" :font-weight "bold"}} "show me"]]
