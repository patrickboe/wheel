(ns wheel.commands
  (:require
    [om.next :as om]))

(defn remove-matches [x]
  (fn [xs] (vec (remove #(= x %) xs))))

(defmulti command om/dispatch)

(defmethod command 'peeps/add
  [{:keys [state]} k addition]
  {:action
   (fn []
     (swap! state update :peeps #(conj % addition)))})

(defmethod command 'peeps/drop
  [{:keys [state]} k deletion]
  {:action
   (fn []
     (swap! state update :peeps (remove-matches deletion)))})
