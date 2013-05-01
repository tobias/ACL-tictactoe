(ns tictactoe.core)

(comment
  ;; a board looks like
  [[:x  nil :o]
   [nil :o  :x]
   [:x  :o  nil]])

(defn third
  "Returns the 3rd item from a sequence. Complements first and
  second."
  [s]
  (nth s 2))

(defn- extract-lines
  "Creates a vector containing all 9 possible win lines (which are
  themselves vectors) from the board."
  [board]
  (conj board
        (map first board)
        (map second board)
        (map third board)
        [(first (first board))
         (second (second board))
         (third (third board))]
        [(third (first board))
         (second (second board))
         (first (third board))]))

(defn- check-line
  "Checks to see if the given line is a winning line. Returns the
  winning player if so, nil otherwise."
  [line]
  (if-let [player (first line)]
    (if (every? #(= player %) line)
      player)))

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

(defn detect-tie
  "Examines the given board for a tie. A tie is a full board with no
  winner."
  [board]
  (and (not (detect-win board))
       (->> board
            (apply concat)
            (some nil?)
            not)))
