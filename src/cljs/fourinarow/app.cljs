(ns fourinarow.app
  (:require
   fourinarow.mutations
   [untangled.client.mutations :as mut]
   [garden.color :as color]
   [sablono.core :as sab]
   [untangled.client.core :as uc :refer [InitialAppState initial-state]]
   [om.next :as om :include-macros true]))

; (defmulti read (fn [env key params] key))

; (defmethod read :board
;   [{:keys [state] :as env} key params]
;   (let [st @state]
;       {:value (:board st)}))

; (defmethod read :title
;   [{:keys [state] :as env} key params]
;   (let [st @state]
;       {:value (:title st)}))

(def cellsize "9vmin")

(def empty-board (->> nil (repeat 10) vec (repeat 8) vec))

(defn darken
  ([colorstr] (darken colorstr 12))
  ([colorstr amount]
   (-> colorstr
       color/hex->hsl
       (color/darken amount)
       color/hsl->hex)))

(defn lighten
  ([colorstr] (lighten colorstr 12))
  ([colorstr amount]
   (-> colorstr
       color/hex->hsl
       (color/lighten amount)
       color/hsl->hex)))

(def color {:x "#e74c3c"
            :gx "#7D4447"
            :o "#3498db"
            :go "#2F658C"
            :wx "#ffff00"
            :wo "#ffff00"
            :board "#2c3e50"})

(defn cellstyle [celltype]
  (merge
    {:width cellsize
     :height cellsize}
    (if celltype
      (let [c (celltype color)]
        {:backgroundColor c
         :borderRadius cellsize
         :border "solid 0.7vmin"
         :boxSizing "border-box"
         :borderColor (darken c)}
        )
      {})))

(defn cell [celltype]
  [:div {:style (cellstyle celltype)}])

(defn row [r]
  (apply
   conj
   [:div {:style {:display "inline-flex"
                  :flexGrow 0
                  :flexShrink 1 }}]
   (mapv cell r)))

(def boardstyle
  (let [c (:board color)]
    {:backgroundColor c
     :display "inline-flex"
     :flex-direction "column"
     :flexShrink 1
     :borderRadius "1vmin"
     :border "solid 0.3em"
     :borderColor (darken c)}))

(om/defui Board
  static InitialAppState
  (initial-state [clz params] empty-board)
  static om/IQuery
  (query [this] [:board])
  Object
  (render [this]
    (let [{:keys [board]} (om/props this)]
      (sab/html
         (apply
          conj
          [:div {:style boardstyle}]
          (mapv row board))))))

(def board-ui (om/factory Board))

(om/defui ^:once Root
  static InitialAppState
  (initial-state [clz params] {:title "Vier Gewinnt!"
                               :winner nil
                               :turn :x
                               :board (uc/initial-state Board {})})
  ;; static om/Ident
  ;; (ident [_ props] [:root 0])
  static om/IQuery
  (query [this] (into [:ui/react-key :title :turn :winner] (om/get-query Board)))
  Object
  (render [this]
          (let [{:keys [ui/react-key title board turn winner]
                 :or {ui/react-key "ROOT"} :as props}
                (om/props this)]
            (sab/html
             [:div {:key react-key}
              [:h1
               {:style {:cursor "pointer"}
                :onClick
                #(om/transact! this '[(app/add-bang-to-title
                                       {:title title})])}
               title]
              [:div {:style {:display "flex"
                             :justifyContent "center"}}
               [:div {:style {:display "flex"
                              :flexDirection "column"}}
                [:div {:style {:display "flex"}}
                 (if winner
                   [:div {:style {:display "flex"
                                  :height cellsize
                                  :fontSize "7vmin"}}
                    [:div {:style (cellstyle winner)}]
                    [:div {:style {:marginLeft "2vmin"
                                   :marginRight "5vmin"}}
                     "gewinnt!"]
                    [:div {:style {:backgroundColor "#2b2b2b"
                                   :borderColor "#1b1b1b"
                                   :paddingLeft "2vmin"
                                   :paddingRight "2vmin"
                                   :marginBottom "1vmin"
                                   :fontSize "6vmin"
                                   :cursor "pointer"
                                   :color "white"
                                   :borderRadius "2vmin" }
                           :onClick #(om/transact!
                                      this
                                      `[(app/reset
                                         {:new-state
                                          ~(uc/initial-state this {})})])
                           }
                     "Neu Starten"]]
                   (vec (map-indexed
                         (fn [i _]
                           [:div {:style (cellstyle turn)
                                  :onMouseOver
                                  #(om/transact!
                                    this
                                    `[(app/highlight-coin {:column ~i})])
                                  :onClick
                                  #(om/transact!
                                    this
                                    `[(app/drop-coin {:column ~i})])}])
                         (first board)))) ]
                (board-ui {:board board})]]]))))

; (def reconciler
;   (om/reconciler
;     {:state app-state
;      :parser (om/parser {:read read})}))

(defonce app (atom (uc/new-untangled-client)))
;(defonce mounted-app (reset! app (uc/mount @app Root "container")))

(defn init []
  (reset! app (uc/mount @app Root "container"))
  (js/console.log "Hallo!!!!"))
