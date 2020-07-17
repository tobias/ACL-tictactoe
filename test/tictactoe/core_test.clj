(ns tictactoe.core-test
  (:require
    [clojure.test :refer [are deftest is]]
    [tictactoe.core :as nut]))


(deftest mark-should-update-the-board
  (is (= [[nil nil nil]
          [nil :x  nil]
          [nil nil nil]]
         (nut/mark [[nil nil nil]
                    [nil nil nil]
                    [nil nil nil]] :x 1 1))))


(deftest mark-should-ignore-requests-to-replace
  (is (= [[nil nil nil]
          [nil :o  nil]
          [nil nil nil]]
         (nut/mark [[nil nil nil]
                    [nil :o  nil]
                    [nil nil nil]] :x 1 1))))


(deftest detect-win-should-work
  (are [exp given] (= exp (nut/detect-win given))
    nil [[nil nil nil]
         [nil nil nil]
         [nil nil nil]]

    :x  [[:x  :x  :x]
         [nil nil nil]
         [nil nil nil]]

    :x  [[nil nil nil]
         [:x  :x  :x]
         [nil nil nil]]

    :x  [[nil nil nil]
         [nil nil nil]
         [:x  :x  :x]]

    :x  [[:x  nil nil]
         [:x  nil nil]
         [:x  nil nil]]

    :x  [[nil :x  nil]
         [nil :x  nil]
         [nil :x  nil]]

    :x  [[nil nil :x]
         [nil nil :x]
         [nil nil :x]]

    :x  [[:x  nil nil]
         [nil :x  nil]
         [nil nil :x]]

    :x  [[nil nil :x]
         [nil :x  nil]
         [:x  nil nil]]

    nil [[:x  :x  :o]
         [nil nil nil]
         [nil nil nil]]))


(deftest detect-tie-should-work
  (are [exp given] (= exp (nut/detect-tie given))
    true  [[:x :o :x]
           [:o :x :o]
           [:o :x :o]]

    false [[:x :o :x]
           [:o :x :o]
           [:o :x nil]]

    false [[:x :x :x]
           [:o :x :o]
           [:o :x :o]]))
