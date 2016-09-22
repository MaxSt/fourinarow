(ns fourinarow_devcards.testboards)

(def lbrett [[nil nil nil nil nil nil nil]
             [nil nil nil nil nil nil nil]
             [nil nil nil nil nil nil nil]
             [nil nil nil :x  nil nil nil]
             [nil nil nil :o  nil nil :o ]
             [nil nil nil :o  nil :o  :x ]
             [nil nil nil :o  :x  :x  :x ]
             [nil nil nil :x  :o  :o  :o ]
             [nil nil :o  :x  :o  :o  :o ]
             [nil :o  :x  :x  :o  :x  :x]])

(def wbrettc [[nil nil nil nil nil nil nil]
              [nil nil nil nil nil nil nil]
              [nil nil nil nil nil nil nil]
              [nil nil nil nil nil nil nil]
              [nil nil nil nil nil nil nil]
              [nil nil nil nil nil nil nil]
              [nil nil nil :o  :o  nil nil]
              [nil nil nil :x  :o  nil nil]
              [nil nil nil :x  :o  nil nil]
              [nil nil :x  :x  :o  :x  nil]])

(def wbrettr [[nil nil nil nil nil nil nil]
              [nil nil nil nil nil nil nil]
              [nil nil nil nil nil nil nil]
              [nil nil nil nil nil nil nil]
              [nil nil nil nil nil nil nil]
              [nil nil nil nil nil nil nil]
              [nil nil nil :o  :x  nil nil]
              [nil nil nil :x  :o  nil nil]
              [nil nil nil :x  :o  nil nil]
              [nil nil :x  :x  :x  :x  nil]])

(def wbrettd [[nil nil nil nil nil nil nil]
              [nil nil nil nil nil nil nil]
              [nil nil nil nil nil nil nil]
              [nil nil nil nil nil nil nil]
              [nil nil nil nil nil nil nil]
              [nil nil nil nil nil nil nil]
              [nil nil nil :o  :x  nil nil]
              [nil nil nil :x  :o  nil nil]
              [nil nil :x  :o  :x  nil nil]
              [nil :x  :x  :o  :x  :x  nil]])

(def wbrettdr [[nil nil nil nil nil nil nil]
               [nil nil nil nil nil nil nil]
               [nil nil nil nil nil nil nil]
               [nil nil nil nil nil nil nil]
               [nil nil nil nil nil nil nil]
               [nil nil nil nil nil nil nil]
               [nil nil nil :o  :x  nil nil]
               [nil nil nil :x  :o  nil nil]
               [nil nil :o  :o  :x  :o  nil]
               [nil :x  :x  :o  :x  :x  :o]])

(def wbrettdr1 [[:o nil nil nil ]
               [nil :o nil nil ]
               [nil nil :o  nil ]
               [nil nil nil :o ] ])
