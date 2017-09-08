(ns demo-expo.core
    (:require [reagent.core :as r :refer [atom]]
              [re-frame.core :refer [subscribe dispatch dispatch-sync]]
              [demo-expo.handlers]
              [demo-expo.subs]
              [cljs-exponent.reagent :refer [text view image touchable-highlight] :as rn]
              [cljs-exponent.components :as components]))

#_(def v (js/require  "expo"))

#_(def vp (js/require "abi-expo-videoplayer"))

#_(def vplayer (r/adapt-react-class (.-default vp)))

#_(def v (js/require "react-native-video-player"))

#_(def vp (js/require "react-native-fullscreen-media-kit"))

#_(def vplayer (r/adapt-react-class (.-default v)))

(def v-url "http://www.sample-videos.com/video/mp4/720/big_buck_bunny_720p_1mb.mp4")

(def url (clj->js "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"))

#_(defn pause []
  (.pause RNAS))

#_(defn setUrl []
  (.setUrl RNAS url))

#_(defn stop []
  (.stop reactNativeAudioStreaming))

#_(defn play []
  (.play RNAS))

(defn alert [title]
  (.alert rn/alert title))

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
        [text {:style {:color "white" :text-align "center" :font-weight "bold"}} "press"]]

       #_[vplayer {:video {:uri v-url}
                 :autoplay false
                 :loop false
                 :muted false
                 :thumbnail "http://static.yoaicdn.com/shoppc/images/cover_img_e1e9e6b.jpg"}]

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
