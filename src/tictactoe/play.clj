(ns tictactoe.play
  (:require
    [clojure.edn :as edn]
    [tictactoe.core :refer [mark detect-win detect-tie]]))


(def ^:private new-board
  "A new (empty) board."
  [[nil nil nil]
   [nil nil nil]
   [nil nil nil]])


(defn- parse-coord
  "Helper fn to parse a single coord"
  [rowcol re f]
  (let [rowcol (.toUpperCase rowcol)]
    (when-let [coord (re-find re rowcol)]
      (f coord))))


(defn- parse-rowcol
  "Turns a0-style coordinates into a vector of numeric [x y] coordinates"
  [rowcol]
  [(parse-coord rowcol #"[0-2]"
                edn/read-string)
   (parse-coord rowcol #"[A-C]"
                #(.indexOf ["A" "B" "C"] %))])


(defn- make-move
  "Attempts to make a move for the given player. If the move was
  valid, the new board is returned, nil otherwise"
  [b player [row col]]
  (when (and row col)
    (let [new-b (mark b player row col)]
      (when (not= new-b b)
        new-b))))


(defn- get-move
  "Reads input from the player, and returns the move coordinates as [x y]"
  []
  (print "select square by letter and number: ")
  (flush)
  (parse-rowcol (read-line)))


(defn- safe-name
  "Like name, but returns \"\" on nil instead of throwing."
  [v]
  (if v (name v) ""))

(defn- display-row
  "Displays a row in the board"
  [r]
  (println
    (apply format "%1s|%1s|%1s"
           (map safe-name r))))


(defn- display-board
  "Displays the board with row/col headers"
  [[row-0 row-1 row-2]]
  (println)
  (println "  A B C")
  (println)
  (print "0 ")
  (display-row row-0)
  (println "  -----")
  (print "1 ")
  (display-row row-1)
  (println "  -----")
  (print "2 ")
  (display-row row-2)
  (println))


(defn- swap-player
  "Given a player, returns the other player"
  [p]
  (if (= p :x) :o :x))


(defn- continue-game?
  "Returns truthy if the game should continue. Checks for a winner or
  a tie, and prints the appropriate messages in those cases."
  [board]
  (let [winner (detect-win board)]
    (cond
      winner             (println (name winner) "wins!\n")
      (detect-tie board) (println "Tie game!\n")
      :else              true)))


(defn- display-turn-banner
  "Prints the banner stating the current player"
  [current-player]
  (println (format "** it's %s's turn **\n" (name current-player))))


(defn- display-invalid-move
  "Prints the invalid move message"
  []
  (println (format "\n** Invalid move! **")))


(defn play
  "When called with no args, starts a new game.
  When called with a board and current player, it handles a move and
  recurses to continue the game."
  ([]
   (play new-board :x))
  ([board current-player]
   (display-board board)
   (when (continue-game? board)
     (display-turn-banner current-player)
     (let [next-board (make-move board current-player (get-move))]
       (when-not next-board
         (display-invalid-move))
       (recur
         (or next-board board)
         (if next-board
           (swap-player current-player)
           current-player))))))
