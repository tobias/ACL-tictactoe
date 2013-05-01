(ns tictactoe.play
  (:require [tictactoe.core :refer [mark detect-win detect-tie third]]))

(defn- new-board
  "Returns a new (empty) board."
  []
   [[nil nil nil]
    [nil nil nil]
    [nil nil nil]])

(defn- parse-coord
  "Helper fn to parse a single coord"
  [rowcol re f]
  (let [rowcol (.toUpperCase rowcol)]
    (if-let [coord (re-find re rowcol)]
      (f coord)
      nil)))

(defn- parse-rowcol
  "Turns a0-style coordinates into a vector of numeric [x y] coordinates"
  [rowcol]
  [(parse-coord rowcol #"[0-2]"
                read-string)
   (parse-coord rowcol #"[A-C]"
                #(.indexOf ["A" "B" "C"] %))])

(defn- make-move
  "Attempts to make a move for the given player. If the move was
  valid, the new board is returned, nil otherwise"
  [b player [row col]]
  (if (and row col)
    (let [new-b (mark b player row col)]
      (if (not= new-b b)
        new-b))))

(defn- get-move
  "Reads input from the player, and returns the move coordinates as [x y]"
  []
  (print "select square by letter and number: ")
  (flush)
  (parse-rowcol (read-line)))

(defn- display-row
  "Displays a row in the board"
  [r]
  (println
   (apply format "%s|%s|%s"
          (map #(if % (name %) " ") r))))

(defn- display-board
  "Displays the board with row/col headers"
  [board]
  (println)
  (println "  A B C")
  (println)
  (print "0 ")
  (display-row (first board))
  (println "  -----")
  (print "1 ")
  (display-row (second board))
  (println "  -----")
  (print "2 ")
  (display-row (third board))
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
     :default           true)))

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
     (play (new-board) :x))
  ([board current-player]
     (display-board board)
     (when (continue-game? board)
       (display-turn-banner current-player)
       (let [next-board (make-move board current-player (get-move))]
         (if-not next-board
           (display-invalid-move))
         (recur
          (or next-board board) 
          (if next-board
            (swap-player current-player)
            current-player))))))
