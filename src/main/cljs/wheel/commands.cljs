(ns wheel.commands
  (:require
    [om.next :as om]))

(defn remove-matches [x]
  (fn [xs] (vec (remove #(= x %) xs))))

(defn add [k state addition]
  {:action
   (fn [] (swap! state update k #(conj % addition)))})

(defn del [k state deletion]
  {:action
    #(swap! state update k (remove-matches deletion))})

(defmulti command om/dispatch)

(defmethod command 'peeps/add
  [{:keys [state]} k x]
  (add :peeps state x))

(defmethod command 'peeps/drop
  [{:keys [state]} k x]
  (del :peeps state x))

(defmethod command 'chores/add
  [{:keys [state]} k x]
  (add :chores state x))

(defmethod command 'chores/drop
  [{:keys [state]} k x]
  (del :chores state x))

(defmethod command 'wheel/turn
  [{:keys [state]}]
  {:action
   #(swap! state update :iteration inc)})
