(ns peggame.core-test
  (:require #_[clojure.test :refer :all]
            [peggame.core :refer :all]
            [expectations :as expect]))

#_(deftest a-test
  (testing "FIXME, I fail."
    (is (= 0 1))))

;(expect/expect ["1" "3"] (char-as-str "1       3"))

(expect/expect true (peg? (my-game-map) 7))

(expect/expect true (remove-peg (my-game-map) 7))

(expect/expect false (add-peg (remove-peg (remove-peg (my-game-map) 7) 5) 5))

(expect/expect false (move-peg (mod-game-map) 1 6))
