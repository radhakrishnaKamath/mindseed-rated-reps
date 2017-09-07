(ns new-expo.core
    (:require [reagent.core :as r :refer [atom]]
              [re-frame.core :refer [subscribe dispatch dispatch-sync]]
              [new-expo.handlers]
              [new-expo.subs]
              ))

(def ReactNative (js/require "react-native"))

(def app-registry (.-AppRegistry ReactNative))
(def text (r/adapt-react-class (.-Text ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def image (r/adapt-react-class (.-Image ReactNative)))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight ReactNative)))
(def Alert (.-Alert ReactNative))

(def expo (js/require "expo"))

(def expo-player (js/require "@expo/videoplayer"))

(def video (r/adapt-react-class (.-Video expo)))

(def video-player (r/adapt-react-class (.-default expo-player)))

(def fs (.-FileSystem expo))

(def v-url "http://www.sample-videos.com/video/mp4/720/big_buck_bunny_720p_1mb.mp4")

(defn alert [title]
  (.alert Alert title))

(defn download-async [url local-uri]
  (->(.downloadAsync fs url local-uri)
     (.then (fn [uri] (println ((js->clj uri) "uri"))))
     (.catch (fn [error] (println (str "error: " error))))))

(defn app-root []
  (let [greeting (subscribe [:get-greeting])]
    (fn []
      [view {:style {:flex-direction "column" :margin 40 :align-items "center"}}
       [image {:source (js/require "./assets/images/cljs.png")
               :style {:width 200
                       :height 200}}]

       [touchable-highlight {:style {:background-color "#999" :padding 10 :border-radius 5}
                             :on-press #(download-async v-url (str (.-documentDirectory fs) "bunny.mp4"))}
        [text {:style {:color "white" :text-align "center" :font-weight "bold"}} "press me"]]
       [video-player {:videoProps {:source {:uri (str (.-documentDirectory fs) "bunny.mp4")}}
                      :isPortrait false
                      :shouldPlay true
                      :resizeMode "cover"
                      :playFromPositionMillis 0}]
       [touchable-highlight {:style {:background-color "#999" :padding 10 :border-radius 5}
                             :on-press #(download-async v-url (str (.-documentDirectory fs) "bunny.mp4"))}
        [text {:style {:color "white" :text-align "center" :font-weight "bold"}} "play/pause"]]])))

(defn init []
  (dispatch-sync [:initialize-db])
  (.registerComponent app-registry "main" #(r/reactify-component app-root)))
