(ns fourinarow.game)

;switch-axis
(defn switch-axis [board] (apply map vector board))

;get position of pattern in vector
(defn position [x coll]
  (let [colls (partition 4 1 coll)
        all-idxs (keep-indexed
                  (fn [idx val] (when (= val x) idx)) colls)]
    (first all-idxs)))

;check if player wins in row
(defn checkrow [row player]
  (let [pattern (vec (repeat 4 player))]
    (position pattern row)))

;check if player wins in rows
(defn checkrows [rows player]
  (first (keep-indexed (fn [rowx row]
                         (when-let [rowy (checkrow row player)]
                           [rowx rowy]))
                       rows)))

;get all combinations of numbers up to ncount
(defn all-combinations [ncount]
  (let [r (range ncount)]
    (for [x r y r] [x y])))

;get diagonal coordinates
(defn diag-rows [rowcount colcount]
  (let [allcount (dec (+ rowcount colcount))]
    (mapv
     (fn [r]
      (->> allcount
           all-combinations
           (filter (fn [[x y]] (and (< x rowcount)
                                    (< y colcount)
                                    (= r (+ x y)))))
           vec))
     (range allcount))))

;board transformed in diagonal rows
(defn diags [board]
  (let [rows (diag-rows (count board) (count (first board)))]
    (mapv (fn [row]
            (mapv (fn [[x y]] (get-in board [x y])) row))
          rows)))


;board transformed in reverse diagonal rows
(defn diags-rev [board]
  (diags (vec (reverse board))))

;get all win coordinats from winning point (row)
(defn win-coords-row [[x y]]
  (mapv vector (repeat 4 x) (range y (+ y 4))))

;get all win coordinats from winning point (column)
(defn win-coords-col [[x y]]
  (mapv vector (range y (+ y 4)) (repeat 4 x)))

;get all win coordinats from winning point (diagonal)
(defn win-coords-diag [[x y] boardwidth boardheight]
  (let [d-rows (diag-rows boardwidth boardheight)
        wrow (get d-rows x)]
    (subvec wrow y (+ y 4))))

;get all win coordinats from winning point (reverse-diagonal)
(defn win-coords-diag-r [[x y] boardwidth boardheight]
  (let [d-rows (diag-rows boardwidth boardheight)
        wrow (get d-rows x)]
    (mapv
     (fn [[nx ny]]
       [(- boardwidth nx 1) ny])
     (subvec wrow y (+ y 4)))))

(defn checkwin-player [board player]
  (when-let
    [win (or
          (when-let [winrow (checkrows board player)]
            {:wintype :row :at (win-coords-row winrow)})
          (when-let [wincol (checkrows (switch-axis board) player)]
            {:wintype :col :at (win-coords-col wincol)})
          (when-let [windiag (checkrows (diags board) player)]
            {:wintype :diag :at (win-coords-diag
                                 windiag
                                 (count board)
                                 (count (first board)))})
          (when-let [windiag-r (checkrows (diags-rev board) player)]
            {:wintype :diag-r :at (win-coords-diag-r
                                   windiag-r
                                   (count board)
                                   (count (first board)))}))]
    (merge win {:winner player})))

(defn checkwin [board]
  (or (checkwin-player board :x)
      (checkwin-player board :o)))

#_ (map vector [9 9 9 9] [3 4 5 6])
(diag-rows 10 2)

(defn player-won-hl [player]
  (case player
    :x :wx
    :o :wo))

(defn highlight-winner [board {:keys [winner wintype at] :as has-winner?}]
  (if has-winner?
    (reduce (fn [board [x y]]
              (assoc-in board [x y] (player-won-hl winner)))
            board
            at)
    board))

; (defn get-drop-pos
;   [board piece [x y]]
;   (let [clear? #(piece-fits? board piece [x %]))
;         cy (first (remove clear? (iterate inc y)))]
;     (max y (dec cy))))

(defn set-point [board [x y] value]
  (assoc-in board [y x] value))

(defn piece-at-point [board [x y]]
  (get (get board y) x))

(defn get-drop-row
  [board x]
  (let [empty-cell? #(not (piece-at-point board [x %]))
        cy (first (remove empty-cell? (range 0 (count board))))]
    (dec (or cy (count board)))))

;(highlight-winner lbrett0 {:winner :o :wintype :row :at [[0 0][0 1][1 0][1 1]]})

; (checkwin lbrett0)
; (checkwin wbrettr)
; (checkwin wbrettc)
; (checkwin wbrettd)
; (checkwin wbrettdr)

; (highlight-winner lbrett0 (checkwin lbrett0))
; (highlight-winner wbrettr (checkwin wbrettr))
; (highlight-winner wbrettc (checkwin wbrettc))
; (highlight-winner wbrettd (checkwin wbrettd))
; (highlight-winner wbrettdr (checkwin wbrettdr))

; (highlight-winner wbrettr (checkwin wbrettr :x))
; (highlight-winner wbrettc (checkwin wbrettc :o))
