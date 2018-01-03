(ns demo-expo.core
    (:require [reagent.core :as r :refer [atom]]
              [re-frame.core :refer [subscribe dispatch dispatch-sync]]
              [demo-expo.handlers]
              [demo-expo.subs]
              [cljs-exponent.reagent :refer [text view image touchable-highlight] :as rn]
              [cljs-exponent.components :as components]))

(def expo (js/require  "expo"))

(def expo-player (js/require "@expo/videoplayer"))

(def video (r/adapt-react-class (.-Video expo)))

(def video-player (r/adapt-react-class (.-default expo-player)))

(def fs (.-FileSystem expo))



(def v-url "http://www.sample-videos.com/video/mp4/720/big_buck_bunny_720p_1mb.mp4")

(def url (clj->js "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"))

(defn alert [title]
  (.alert rn/alert title))

(defn download-async [url local-uri]
  (->(.downloadAsync fs url local-uri)
     (.then (fn [uri] (println ((js->clj uri) "uri"))))
     (.catch (fn [error] (println (str "error: " error))))))

(defn my-share [content opts]
  (.share (.-Share (js/require "react-native")) content opts))

(defn app-root []
  (let [greeting (subscribe [:get-greeting])]
    (fn []
      [view {:style {:flex-direction "column" :margin 40 :align-items "center"}}
       [touchable-highlight {:style {:background-color "#999" :padding 10 :border-radius 5}
                             :on-press #(my-share (clj->js {:message "https%3A%2F%2Ffarm5.staticflickr.com%2F4434%2F36582664612_bab0916d27_b.jpg"
                                                            :title "message"
                                                            :url "dbcskdbchsdb"})
                                                  (clj->js {:dialogTitle "welcome"})) #_(show-video)}
        [text {:style {:color "white" :text-align "center" :font-weight "bold"}} "share"]]

       [image {:source {:uri "https://facebook.github.io/react/img/logo_og.png"}
               :style {:width 200
                       :height 200}}
        [image {:source {:uri "https://facebook.github.io/react/img/logo_og.png"}
                :style {:width 20
                        :height 20}
                :zIndex 2
                :margin 90}]]

       #_[touchable-highlight {:style {:background-color "#999" :padding 10 :border-radius 5}
                             :on-press #(download-async v-url (str (.-documentDirectory fs) "bunny.mp4") (clj->js {:md5 false}))}
        [text {:style {:color "white" :text-align "center" :font-weight "bold"}} "download"]]

       [video-player {:videoProps {:source {:uri v-url}}
                      :isPortrait true
                      :shouldPlay false
                      :resizeMode "cover"
                      :playFromPositionMillis 0}
        ]

       #_[components/video {:source (js/require "./assets/images/tx.mp4")
                            :rate 1.0
                            :volume 1.0
                            :muted false
                            :paused false
                            :repeat false
                            :resizeMode "cover"
                            :style {:width 300
                                    :height 300}}]])))


(defn init []
  (dispatch-sync [:initialize-db])
  (.registerComponent rn/app-registry "main" #(r/reactify-component app-root)))
