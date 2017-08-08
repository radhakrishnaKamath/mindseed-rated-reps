(ns first-cljs.core
  (:require [clojure.string :as string]))

#_(defn caps [inp]
  (string/join " "
               (mapv string/capitalize
                     (string/split inp #" "))))

(defn crypt-js [stri k]
  (let [sep-str (partition (/ (count stri)
                              (count k)) (js->clj (.split (clj->js stri) "")))
        sep-key (js->clj (.split (clj->js k) ""))]
    (apply str (flatten (vals (sort (zipmap sep-key
                                            sep-str)))))))

(defn crypt [stri k]
  (let [sep-str (partition (/ (count stri)
                              (count k)) (string/split stri ""))
        sep-key (string/split k "")]
    (apply str (flatten (vals (sort (zipmap sep-key
                                            sep-str)))))))

(defn main
  "I don't do a whole lot ... yet."
  [& args]
  #_(.write js/document "Hello, World!")
  (let [input (.-value (.getElementById js/document "input"))
        input1 (.-value (.getElementById js/document "input1"))
        output (crypt-js input input1)]
    #_(.write js/document input)
    #_(.write js/document input1)
    (set! (.-value (.getElementById js/document "output"))
          output)))

#_(-main)
