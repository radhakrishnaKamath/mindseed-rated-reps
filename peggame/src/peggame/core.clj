(ns peggame.core
  (:gen-class))
;so lets plan now how to work and then start working according to it.

;taking input
(defn triangle
  ([] (triangle 0 1))
  ([sum n] (let [new-sum (+ sum n)]
             (cons new-sum (lazy-seq (triangle new-sum (inc n)))))))
(defn input [x] (take x (triangle)))

(defn parse-int [s] (Integer. (re-find #"\d+" s)))


(defn -main
  "I don't do a whole lot ... yet."
  []
  (println "enter the number of rows")
  (input (parse-int (read-line))))




(defn char-as-str [x] (filter (comp not empty?) (clojure.string/split x #" ")))

(defn convert-int [str] (- (int (let [gb (.getBytes str)] (char (first gb)))) 96))

(defn my-game-map [] {1  {:pegged true, :connections {6 3, 4 2}},
                      2  {:pegged true, :connections {9 5, 7 4}},
                      3  {:pegged true, :connections {10 6, 8 5}},
                      4  {:pegged true, :connections {13 8, 11 7, 6 5, 1 2}},
                      5  {:pegged true, :connections {14 9, 12 8}},
                      6  {:pegged true, :connections {15 10, 13 9, 4 5, 1 3}},
                      7  {:pegged true, :connections {9 8, 2 4}},
                      8  {:pegged true, :connections {10 9, 3 5}},
                      9  {:pegged true, :connections {7 8, 2 5}},
                      10 {:pegged true, :connections {8 9, 3 6}},
                      11 {:pegged true, :connections {13 12, 4 7}},
                      12 {:pegged true, :connections {14 13, 5 8}},
                      13 {:pegged true, :connections {15 14, 11 12, 6 9, 4 8}},
                      14 {:pegged true, :connections {12 13, 5 9}},
                      15 {:pegged true, :connections {13 14, 6 10}},
                      :rows 5})


(defn take-input []
  (println "enter the source and destination points")
  (map convert-int  (char-as-str (read-line))))

;(take-input)

                                        ;taking move
;(char-as-str "a   b")

;is peg
(defn peg? [board pos] (if (get-in board [pos :pegged])
                         true
                         false))

;valid move

;remove peg
(defn remove-peg [board pos]
  (assoc-in board
            [pos :pegged]
            false))

;add peg
(defn add-peg [board pos]
  (assoc-in board
            [pos :pegged]
            true))

;move peg
(defn move-peg [board pos dest]
  (let [neigh (second (first (filter #(= dest (first %))
                                     (get-in (my-game-map)
                                             [pos :connections]))))]
    (println neigh)
    (add-peg (remove-peg (remove-peg board
                                 neigh)
                     pos)
             dest)))

;is valid
(defn valid? [board pos dest]
  (let [neigh (second (first (filter #(= dest (first %))
                                     (get-in (my-game-map)
                                             [pos :connections]))))]
    (if (and (peg? board pos)
             (peg? board neigh)
             (not (peg? board dest)))
      (move-peg board pos dest)
      board)))
(defn mod-game-map [] {1  {:pegged true, :connections {6 3, 4 2}},
                      2  {:pegged true, :connections {9 5, 7 4}},
                      3  {:pegged true, :connections {10 6, 8 5}},
                      4  {:pegged true, :connections {13 8, 11 7, 6 5, 1 2}},
                      5  {:pegged true, :connections {14 9, 12 8}},
                      6  {:pegged false, :connections {15 10, 13 9, 4 5, 1 3}},
                      7  {:pegged true, :connections {9 8, 2 4}},
                      8  {:pegged true, :connections {10 9, 3 5}},
                      9  {:pegged true, :connections {7 8, 2 5}},
                      10 {:pegged true, :connections {8 9, 3 6}},
                      11 {:pegged true, :connections {13 12, 4 7}},
                      12 {:pegged true, :connections {14 13, 5 8}},
                      13 {:pegged true, :connections {15 14, 11 12, 6 9, 4 8}},
                      14 {:pegged true, :connections {12 13, 5 9}},
                      15 {:pegged true, :connections {13 14, 6 10}},
                      :rows 5})
