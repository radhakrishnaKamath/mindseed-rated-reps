(ns rated.core
    (:require [reagent.core :as r]))

;; -------------------------
;; Views

(def board (r/atom {11 {:val ""}
                    12 {:val ""}
                    13 {:val ""}
                    21 {:val ""}
                    22 {:val ""}
                    23 {:val ""}
                    31 {:val ""}
                    32 {:val ""}
                    33 {:val ""}}))

(def counter (r/atom 0))

(defn log [& msg]
  (.log js/console (str msg)))

(defn xorzero []
  (swap! counter inc)
  (if (zero? (mod @counter 2))
    "O"
    "X"))

(defn filled []
  (count (remove empty?
                 (flatten (map vals
                               (vals @board))))))

(defn put-data [pos]
  (swap! board assoc-in [pos :val] (xorzero)))

(defn my-val [a b]
  (+ a (* b 10)))

(defn hori? [x]
  (let [val (for [a (range 1 4)]
              (get-in @board
                      [(my-val a x) :val]))]
    (and (apply = val)
         (apply not= "" val))))

(defn vert? [x]
  (let [val (for [a (range 1 4)]
              (get-in @board
                      [(my-val x a) :val]))]
    (and (apply = val)
         (apply not= "" val))))

(defn digo1? []
  (let [val (for [a (range 1 4)]
              (get-in @board
                      [(my-val a a) :val]))]
    (and (apply = val)
         (apply not= "" val))))

(defn digo2? []
  (let [val (map (fn [a b]
                   (get-in @board
                           [(my-val b a) :val]))
                 (range 1 4)
                 (reverse (range 1 4)))])
  (and (apply = val)
       (apply not= "" val)))

(defn win? []
  (let [size 4]
    (some true?
          (flatten [(map hori? (range 1 size))
                    (map vert? (range 1 size))
                    (digo1?)
                    (digo2?)]))))

(defn make-move [pos]
  (put-data pos)
  (if (win?)
    (do (js/alert (str
                   (if (zero? (mod @counter 2))
                     "O"
                     "X") "win"))
        (.reload js/location))
    (if (= 9 (filled))
      (do (js/alert "game draw")
          (.reload js/location)))))

(defn print-board []
  [:div
   (let [size 4]
     (doall
      (for [i (range 1 size)]
        ^{:key i} [:div
                   (doall
                    (for [j (range 1 size)]
                      (let [ij (my-val j i)]
                        ^{:key j} [:span
                                   [:input {:type "button"
                                            :on-click #(make-move ij)
                                            :value (get-in @board [ij :val])}]])))])))])

(defn home-page []
  [:div [print-board]])

;; -------------------------
;; Initialize app

(defn mount-root []
  (r/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))
