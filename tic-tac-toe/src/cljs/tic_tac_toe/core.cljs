(ns tic-tac-toe.core)

(def a (atom {11 {:val ""}
              12 {:val ""}
              13 {:val ""}
              21 {:val ""}
              22 {:val ""}
              23 {:val ""}
              31 {:val ""}
              32 {:val ""}
              33 {:val ""}}))

(def value-to-put (atom "X"))

(defn swap-complement
  "Switches the value of atom as X or O"
  [myvalue]
  (if (= myvalue "X")
    (reset! value-to-put "O")
    (reset! value-to-put "X")))

(defn read-int [a b]

  #_(+ (* a 10) b)
  (.log js/console (js->clj (js/parseInt (str a b))))
  (js->clj (js/parseInt (str a b)))

  )

(defn check-row?
  "Checks in row for win"
  [ab]
  (and (= (get-in @a [(read-int ab 1) :val])
          (get-in @a [(read-int ab 2) :val])
          (get-in @a [(read-int ab 3) :val]))
       (not= "" (get-in @a [(read-int ab 2) :val]))))


(defn check-col?
  "Checks in col for win"
  [ab]
  (and (= (get-in @a [(read-int 1 ab) :val])
          (get-in @a [(read-int 2 ab) :val])
          (get-in @a [(read-int 3 ab) :val]))
       (not= "" (get-in @a [(read-int 2 ab) :val]))))


(defn check-diag?
  "Checks for win by diag"
  []
  (and (or (= (get-in @a [11 :val])
              (get-in @a [22 :val])
              (get-in @a [33 :val]))
           (= (get-in @a [13 :val])
              (get-in @a [22 :val])
              (get-in @a [31 :val])))
       (not= "" (get-in @a [22 :val]))))



(defn check
  "Checks who won"
  []
  (some true? (for [i (range 1 4)]
                (or (check-row? i)
                    (check-col? i)
                    (check-diag?)))))



(defn check-finish?
  "Checks of the game is finish or not"
  []
  (= 9 (count (remove empty? (map #(get-in @a [% :val]) (keys @a))))))



(defn my-main
  "It takes id of the selected tile and updates it with X or O"
  [id]
  (set! (.-value (.getElementById js/document id)) @value-to-put)
  (set! (.-disabled (.getElementById js/document id)) true)
  (swap! a assoc-in [id :val] @value-to-put)
  (if (check)
    (do (js/alert (str @value-to-put " Won"))
        (.reload js/location true))
    (if (check-finish?)
      (do (js/alert "GAME SAMAPT! KRPIYA DOBARA SHURU KARE")
          (.reload js/location true))
      (swap-complement @value-to-put))))
