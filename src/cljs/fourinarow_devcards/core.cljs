(ns fourinarow_devcards.core
  (:require
   [devcards.core :as dc :refer-macros [start-devcard-ui! defcard deftest]]
   [sablono.core :as sab]
   [cljs.test :as t :include-macros true :refer-macros [testing is]]
   [devcards-om-next.core :refer-macros [defcard-om-next om-next-root]]
   [fourinarow_devcards.testboards :as tb]
   [fourinarow.app :as app]
   [fourinarow.game :as game]))

(enable-console-print!)

(defcard my-first-card
  (sab/html [:h1 "Devcards is freaking awesome!"]))

(defcard darken-and-lighten
  (let [block (fn [x]
                [:div {:style {:width "10vmin"
                               :height "10vmin"
                               :display "inline-block"
                               :backgroundColor x}}])]
    (sab/html
     [:div
      (block "#ffff00")
      (block (app/darken "#00ff00"))
      (block (app/darken "#ff0000" 40))
      (block (app/lighten "#ff0000"))
      (block (app/lighten "#ff0000" 40))])))

(defcard test-cells
  (sab/html
   [:div {:style {:display "flex"}}
    (app/cell :x)
    (app/cell nil)
    (app/cell :o)]))

(defcard row
  (sab/html
   (app/row [:x :o nil :o])))


(defcard-om-next board
  app/Board
  {:board [[:x :o nil :x nil :x]
           [nil :x :o :x :x  :o]]})

(defcard switch-axis
  (sab/html
   [:div
    (app/board-ui {:board tb/lbrett})
    [:div "switch-axis ->"]
    (app/board-ui {:board (game/switch-axis tb/lbrett)})
    ]))


(deftest game-logic-tests
  (testing "game/position"
    (is (= (game/position [1 1 1 1] [0 0 0 1 1 0 1 0 0]) nil))
    (is (= (game/position [1 1 1 1] [0 0 1 1 1 1 1 0 0]) 2))
    (is (= (game/position [1 1 1 1] [0 0 0 0 0 1 1 1 1 1 0 0]) 5))
    (is (= (game/position [:x :x :x :x]
                          [nil nil :x :x :o :x :x :x :x :o nil nil]) 5)))
  (testing "game/checkrow"
    (is (= (game/checkrow [nil nil :x :x :o :x :x :x :x :o nil nil] :x) 5))
    (is (= (game/checkrow [nil nil :x :x :o :x :o :x :x :o nil :x] :x) nil)))

  (testing "game/checkrows"
    (is (= (game/checkrows tb/lbrett :x) nil))
    (is (= (game/checkrows tb/lbrett :o) nil))
    (is (= (game/checkrows tb/wbrettr :o) nil))
    (is (= (game/checkrows tb/wbrettr :x) [9 2]))
    (is (= (game/checkrows tb/wbrettr :o) nil)))

  (testing "game/all-combinations"
    (is (= (game/all-combinations 1) [[0 0]]))
    (is (= (game/all-combinations 2) [[0 0][0 1][1 0][1 1]]))
    (is (= (game/all-combinations 3) [[0 0][0 1][0 2]
                                      [1 0][1 1][1 2]
                                      [2 0][2 1][2 2]])))

  (testing "game/win-coords-row"
    (is (= (game/win-coords-row [3 4]) [[3 4][3 5][3 6][3 7]]))
    (is (= (game/win-coords-col [3 4]) [[3 4][4 4][5 4][6 4]]))
    (is (= (game/win-coords-diag [3 0] 5 5) [[0 3][1 2][2 1][3 0]]))
    (is (= (game/win-coords-diag-r [3 0] 4 4) [[0 0][1 1][2 2][3 3]]))
    ))

(defcard (game/checkrows (game/diags-rev tb/wbrettdr1) :o))

(defcard (game/checkwin tb/wbrettdr1))


(defn init []
  (start-devcard-ui!))

