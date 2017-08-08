(ns my-spiral-game.core
    (:require [reagent.core :as r]))

;; -------------------------
;; Views

(defn log [& msg]
  (.log js/console (apply str msg)))

(def game (r/atom {
                 :a-curr [0 0]
                 :b-curr [7 7]
                 :a-dir :right
                 :b-dir :left
                 :board [[00  10  20  30  40  50 60 70]
                         [01  11  21  31  41  51 61 71]
                         [02  12  22  32  42  52 62 72]
                         [03  13  23  33  43  53 63 73]
                         [04  14  24  34  44  54 64 74]
                         [05  15  25  35  45  55 65 75]
                         [06  16  26  36  46  56 66 76]
                         [07  17  27  37  47  57 67 77]
                         ]
                 :table [[00  10  20  30  40  50 60 70]
                         [01  11  21  31  41  51 61 71]
                         [02  12  22  32  42  52 62 72]
                         [03  13  23  33  43  53 63 73]
                         [04  14  24  34  44  54 64 74]
                         [05  15  25  35  45  55 65 75]
                         [06  16  26  36  46  56 66 76]
                         [07  17  27  37  47  57 67 77]
                         ]
                 :a-path []
                 :b-path []
                 :chance 0}))

(defn traverse-* [x y x1 dir color]
  (let [row (doall (for [i (range x1)]
                     (condp = dir
                       :right (do (swap! game assoc-in [:table x (+ y i)]
                                         {:val "" :color color})
                                  (get-in @game [:board x (+ y i)]))
                       :down (do (swap! game assoc-in [:table (+ y i) x]
                                        {:val "" :color color})
                                 (get-in @game [:board (+ y i) x]))
                       :left (do (swap! game assoc-in [:table y (- y i)]
                                        {:val "" :color color})
                                 (get-in @game [:board y (- y i)]))
                       :up (do (swap! game assoc-in [:table (- y i) x]
                                      {:val "" :color color})
                               (get-in @game [:board (- y i) x])))))]
    row))

(defn traverse-right [[x y] x1 dir curr color]
  (swap! game assoc-in [curr] [(+ x (dec x1)) (inc y)])
  (swap! game assoc-in [dir] :down)
  (traverse-* x y x1 :right color))

(defn traverse-down [[x y] x1 dir curr color]
  (swap! game assoc-in [curr] [(dec x) (+ y (dec x1))])
  (swap! game assoc-in [dir] :left)
  (traverse-* x y x1 :down color))

(defn traverse-left [[x y] x1 dir curr color]
  (swap! game assoc-in [curr] [(- y (dec x1))  (dec x)])
  (swap! game assoc-in [dir] :up)
  (traverse-* x y x1 :left color))

(defn traverse-up [[x y] x1 dir curr color]
  (swap! game assoc-in [curr] [(inc x) (- y (dec x1))])
  (swap! game assoc-in [dir] :right)
  (traverse-* x y x1 :up color))

(defn traverse [dir curr color]
  (doall (for [i [8 6 6 4 4 2 2]]
           (let [current (get-in @game [curr])]
             (condp = (get-in @game [dir])
               :right (traverse-right current i dir curr color)
               :down (traverse-down current i dir curr color)
               :left (traverse-left current i dir curr color)
               :up (traverse-up current i dir curr color))))))

(defn make-board []
  (swap! game assoc :a-path (flatten (traverse :a-dir :a-curr "lime"))
         :b-path (flatten (traverse :b-dir :b-curr "yellow"))))

(defn dice []
  (let [value (rand-nth [1 2 3 4 5 6 7])]
    (log value)
    value))

(defn seperate-coordinate [no]
  [(quot no 10) (rem no 10)])

(defn win? [no]
  (= 1 no))

(defn make-move [path steps chance]
  (let [main-path (path @game)
        count-path (count main-path)]
    (when (> count-path steps)
      (let [[i j] (seperate-coordinate (first main-path))
            new-path (doall (drop steps main-path))
            [x y] (seperate-coordinate (first new-path))]
        (swap! game assoc-in [:table j i :val] " ")
        (swap! game assoc-in [:table y x :val] chance)
        (swap! game assoc path new-path)
        (when (win? (count new-path))
          (js/alert (str chance " won"))
          (.reload js/location true))))))

(defn home-page []
  [:div
   [:table
    [:tbody
     (doall (for [i (range 8)]
              ^{:key i} [:tr
                         (doall (for [j (range 8)]
                                  ^{:key j} [:td
                                            {:style {:background-color
                                                     (get-in @game [:table i j :color])}}
                                             (get-in @game [:table i j :val])]))]))]]
   [:button {:on-click #(make-board)} "start"]
   [:button {:on-click #(if (zero? (mod (:chance @game) 2))
                          (do
                            (swap! game update-in [:chance] inc)
                            (make-move :a-path (dec (dice)) "A"))
                          (do
                            (swap! game update-in [:chance] inc)
                            (make-move :b-path (dec (dice)) "B")))} "Dice"]
   #_[:button {:on-click #(make-move :b-path (dec (dice)) "B")} "B dice"]])

;; -------------------------
;; Initialize app


(defn mount-root []
  (r/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))
