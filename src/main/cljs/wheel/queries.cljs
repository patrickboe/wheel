(ns wheel.queries)

(defn read [{:keys [state]} key _]
  (let [st @state]
    {:value (key st)}))
