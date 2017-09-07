(ns new-re-natal.android.core
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [new-re-natal.events]
            [new-re-natal.subs]))

(def ReactNative (js/require "react-native"))

(def app-registry (.-AppRegistry ReactNative))
(def text (r/adapt-react-class (.-Text ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def image (r/adapt-react-class (.-Image ReactNative)))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight ReactNative)))


(def logo-img (js/require "./images/cljs.png"))

#_(def sample-video (js/require "./images/tx.mp4"))

(def rnas (js/require "react-native-audio-stream"))

(def rnasing (.-ReactNativeAudioStreaming rnas))

(def player (r/adapt-react-class (.-Player rnas)))

(def v-url "http://www.sample-videos.com/video/mp4/720/big_buck_bunny_720p_10mb.mp4")

(def m-url "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3")

(def rnsl (js/require "react-native-share"))

(def ss (.-shareSingle rnsl))

(def rnfs (js/require "react-native-fs"))

(def download-file (.-downloadFile rnfs))

(defn alert [title]
  (.alert (.-Alert ReactNative) title))

(defn share-picture []
  (-> (ss (clj->js {:url "http://www.pngmart.com/files/2/Mario-PNG-File.png"
                    :type "image/jpeg"
                    :message "my mag"
                    :social "facebook"}))
      (.then #(alert %))
      (.catch #(alert %))))

(defn download-share []

  (share-picture))

(defn pause []
  (.pause rnasing))

(defn resume []
  (.resume rnasing))

(defn stop []
  (.stop rnasing))

(defn play [url]
  (.play rnasing url (clj->js {:showIniOSMediaCenter true
                               :showInAndroidNotifications true})))

(defn app-root []
  (let [greeting (subscribe [:get-greeting])]
    (fn []
      [view {:style {:flex-direction "column" :align-items "center"}}

       [image {:source logo-img
               :style  {:width 80 :height 80 :margin-bottom 30}}]
       [touchable-highlight {:style {:background-color "#999" :padding 10 :border-radius 5}
                             :on-press #(download-share)}
        [text {:style {:color "white" :text-align "center" :font-weight "bold"}} "press me"]]

       #_[view {:height 100}]
       #_[player {:url m-url}]])))

(defn init []
      (dispatch-sync [:initialize-db])
      (.registerComponent app-registry "NewReNatal" #(r/reactify-component app-root)))
