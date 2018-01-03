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
(def modal (r/adapt-react-class (.-Modal ReactNative)))
(def dimension (.-Dimensions ReactNative))

(def expo (js/require "expo"))
(def video (r/adapt-react-class (.-Video expo)))

;;(def audio (.Audio expo))
(def sound (.-Sound expo.Audio))

(def sound-obj (new expo.Audio.Sound))
(def sound-obj1 (new expo.Audio.Sound))

(def permission (.-Permission expo))

(def notifications (.-Notifications expo))

(def screen-orientation (.-ScreenOrientation expo))

(def camera (r/adapt-react-class (.-Camera expo)))

(def bar-code-scanner (r/adapt-react-class (.-BarCodeScanner expo)))

(def speech (.-speak (.-Speech expo)))

(def fs (.-FileSystem expo))

(def expo-player (js/require "@expo/videoplayer"))
(def video-player (r/adapt-react-class (.-default expo-player)))

(def device-info (js/require "react-native-device-info"))

(def dev (.-DeviceInfo device-info))

(def flexi-radio (js/require "react-native-flexi-radio-button"))

(def radio-group (r/adapt-react-class (.-RadioGroup flexi-radio)))

(def radio-button (r/adapt-react-class (.-RadioButton flexi-radio)))

(def v-url "http://www.sample-videos.com/video/mp4/720/big_buck_bunny_720p_10mb.mp4")

(def mp3 (js/require "./assets/mp3/good_girls_5sos.mp3"))
(def mp3-1 (js/require "./assets/mp3/tenchi_oyooooo.mp3"))

(def m-url "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3")

(def show (r/atom {:show-modal false
                   :win-dim {:height 100
                             :width 100}}))

(defn call-modal []
  (.allow screen-orientation
          (.. screen-orientation -Orientation -LANDSCAPE))
  (swap! show assoc :show-modal true))

(defn close-modal []
  (.allow screen-orientation
          (.. screen-orientation -Orientation -PORTRAIT))
  (swap! show assoc :show-modal false))

(defn alert [title]
  (.alert Alert title))

(defn get-token []
  (-> (.getExpoPushTokenAsync notifications)
      (.then #(println %))
      (.catch #(println %))))

(defn get-dim []
  (println (js->clj (.get dimension "window")))
  (swap! show assoc :win-dim (js->clj (.get dimension "window"))))

(defn handler [notification]
  (println (js->clj notification)))

(defn recieve-notification []
  (.addListener notifications #(handler %)))

(defn download-async [url local-uri]
  (-> (.downloadAsync fs url local-uri)
      (.then (fn [uri] (println ((js->clj uri) "uri"))))
      (.catch (fn [error] (println (str "error: " error))))))

(defn my-share [content opts]
  (.share (.-Share (js/require "react-native")) content opts))

(defn play [obj]
  (-> (.playAsync obj)
      (.then #(println (.getStatusAsync sound-obj)))
      (.catch #(println % "error"))))

(defn load-audio [obj mp3]
  (.catch (.then (.loadAsync obj mp3)
                 (fn [x]
                   (println "Loaded " x)))
          #(println % " error")))

(defn load-bg-audio []
  (.catch (.then (.loadAsync sound-obj mp3-1)
                 (fn [x]
                   (.then (.setIsLoopingAsync sound-obj true)
                          #(play sound-obj)
                          #(println "error " %))
                   (println x)))
          #(println % " error")))

(defn speak []
  (speech "hello how are you? i am fine thanks"))

(defn vid []
  [video-player {:video-props {:source {:uri "http://www.sample-videos.com/video/mp4/720/big_buck_bunny_720p_10mb.mp4"}
                               :resizeMode "cover"}
                 :playFromPositionMillis 0}]  )

(defn app-root []
  (let [greeting (subscribe [:get-greeting])]
    (fn []
      (recieve-notification)
      (load-bg-audio)
      (load-audio sound-obj1 mp3)
      (println "@@@@@@@@@@@@@"(.getStatusAsync sound-obj))
      [view {:style {:margin 40}}
       [touchable-highlight {:style {:background-color "#999" :padding 5 :border-radius 5 :margin 5}
                             :on-press (fn []
                                         (.then (.setPositionAsync sound-obj1 0)
                                                #(play sound-obj1)
                                                #(println "cannot play again dude")))}
        [text {:style {:color "white" :text-align "center" :font-weight "bold"}} "play sound"]]])))



(defn init []
  (dispatch-sync [:initialize-db])
  (.registerComponent app-registry "main" #(r/reactify-component app-root)))

#_[view {:flex 1}
   [bar-code-scanner {:onBarCodeRead #(println (js->clj %))'
                      :style {:height "100%"
                              :width "100%"}}]]

#_ ([radio-group {:on-select #(println %)}
     [radio-button {:value "item1"} [text "item1"]]
     [radio-button {:value "item2"} [text "item1"]]
     [radio-button {:value "item3"} [text "item1"]]
     [radio-button {:value "item4"} [text "item1"]]
     [radio-button {:value "item5"} [text "item1"]]
     [radio-button {:value "item6"} [text "item1"]]]

    [video-player {:videoProps {:source {:uri (str (.-documentDirectory fs) "bunny.mp4")}}
                      :isPortrait false
                      :shouldPlay true
                      :resizeMode "cover"
                      :playFromPositionMillis 0}])


;;;;;8NJw1WLFTvktXZL7AhRZG_
