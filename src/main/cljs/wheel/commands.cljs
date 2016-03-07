(ns wheel.commands)

(defn command [{:keys [state]} _ addition]
  {:action
   (fn []
     (swap! state update :peeps #(conj % addition)))})
