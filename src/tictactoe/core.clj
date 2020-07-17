(ns tictactoe.core)


(comment
  ;; a board looks like
  [[:x  nil :o]
   [nil :o  :x]
   [:x  :o  nil]])


(defn- extract-lines
  "Creates a vector containing all 8 possible win lines (which are
  themselves vectors) from the board."
  [board]
  (conj board
        (map #(nth % 0) board)
        (map #(nth % 1) board)
        (map #(nth % 2) board)
        (for [path [[0 0] [1 1] [2 2]]]
          (get-in board path))
        (for [path [[0 2] [1 1] [2 0]]]
          (get-in board path))))


(defn- check-line
  "Checks to see if the given line is a winning line. Returns the
  winning player if so, nil otherwise."
  [line]
  (when (apply = line)
    (first line)))


(defn mark
  "Marks a board position for the given player, returning the new
  board."
  [board player row col]
  (if-not (get-in board [row col])
    (assoc-in board [row col] player)
    board))


(defn detect-win
  "Examines the given board for a winner, returning the winning player
  if found, nil otherwise."
  [board]
  (->> board
       extract-lines
       (map check-line)
       (remove nil?)
       first))

(defn full-board?
  "Examines the given board, returning true if the board is full."
  [board]
  (not
   (->> board
        (apply concat)
        (some nil?))))

(defn detect-tie
  "Examines the given board for a tie. A tie is a full board with no
  winner."
  [board]
  (and (not (detect-win board))
       (full-board? board)))
