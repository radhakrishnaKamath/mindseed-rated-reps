(ns tic-tac-toe.core)

(defn get-map
  "Get all the values from board"
  []
  (for [i (range 1 4)
        j (range 1 4)
        :let [prod (.-value (.getElementById js/document (str i j)))]]
    prod))

(defn reset
  "Resets the board"
  []
  (js/alert "play again")
  (doall (for [i (range 1 4)
               j (range 1 4)]
           (do (set! (.-value (.getElementById js/document (str i j))) "")
               (set! (.-disabled (.getElementById js/document (str i j))) false)))))



(defn get-val
  "Get value of block from given position"
  [x y]
  (.-value (.getElementById js/document (str x y))))



(defn check-diag?
  "Checks for win by diag"
  [a]
  (if (or (= (get-val 1 1)
             (get-val 2 2)
             (get-val 3 3))

          (= (get-val 1 3)
             (get-val 2 2)
             (get-val 3 1)))
    (if (= (get-val 2 2) "X")
      1
      (if (= (get-val 2 2) "O")
        2))))

(defn check-col?
  "Checks for win by column"
  [a]
  (if (= (get-val 1 a)
         (get-val 2 a)
         (get-val 3 a))
    (if (= (get-val 1 a) "X")
      1
      (if (= (get-val 1 a) "O")
        2))))

(defn check-row?
  "Checks for win by row"
  [a]
  (if (= (get-val a 1)
         (get-val a 2)
         (get-val a 3))
    (if (= (get-val a 1) "X")
      1
      (if (= (get-val a 1) "O")
        2))))



(defn win?
  "Checks for each row column and diag"
  [a]
  (or (check-row? a)
      (check-diag? a)
      (check-col? a)))


(defn check
  "Checks if player won"
  []
  (first (remove nil? (doall (for [i (range 1 4)]
                          (win? i))))))

(defn mark
  "Takes the block pos from user"
  [no]
  (if (= (check) 1)
    (do (js/alert "X won")
        (reset))
    (if (= (check) 2)
      (do (js/alert "O won")
          (reset))
      (if (= 9 (count (filter #(not (empty? %)) (get-map))))
        (reset)
        (if (even? (count (filter #(not (empty? %)) (get-map))))
          (do (set! (.-value (.getElementById js/document no)) "X ")
              (set! (.-disabled (.getElementById js/document no)) true))
          (do (set! (.-value (.getElementById js/document no)) "O")
              (set! (.-disabled (.getElementById js/document no)) true)))))))
