(ns fourinarow.mutations
  (:require [untangled.client.mutations :as m]
            [untangled.client.data-fetch :as df]
            [fourinarow.game :as g]
            [om.next :as om]))

;; This is all you need to "change tabs"
(defmethod m/mutate 'app/add-bang-to-title [{:keys [state]} k {:keys [title]}]
  {:action
   (fn []
     (swap! state update-in [:title] (fn [t] (str t "!"))))})

(defn remove-ghosts [board]
  (mapv (fn [row]
          (mapv
           (fn [cell]
             (if (or (= cell :gx) (= cell :go))
               nil
               cell))
           row)) board))

(defmethod m/mutate 'app/highlight-coin
  [{:keys [state]} k {:keys [column]}]

    {:action
     (fn []
       (let [{:keys [turn]} @state
             board (remove-ghosts (:board @state))
             highlight (if (= turn :x) :gx :go)]
         (swap!
          state
          (fn [s]
            (-> s
                (update-in [:board] remove-ghosts)
                (update-in [:board]
                           g/set-point
                           [column (g/get-drop-row board column)]
                           highlight))))))})

(defmethod m/mutate 'app/drop-coin [{:keys [state]} k {:keys [column]}]
    {:action
     (fn []
       (let [{:keys [turn]} @state
             board (remove-ghosts (:board @state))]
         (swap!
          state
          (fn [s]
            (-> s
                (update-in [:board] remove-ghosts)
                (update-in [:board]
                           g/set-point
                           [column (g/get-drop-row board column)]
                           turn)
                (assoc-in [:turn] (if (= turn :x) :o :x))
                )))
         (when-let [win-map (g/checkwin (:board @state))]
           (swap!
            state
            (fn [s]
              (-> s
                  (assoc :winner (:winner win-map))
                  (assoc :board (g/highlight-winner (:board @state) win-map))))))))})

(defmethod m/mutate 'app/reset [{:keys [state]} k {:keys [new-state]}]
  {:action
   (fn []
     (reset! state new-state))})
