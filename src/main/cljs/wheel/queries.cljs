(ns wheel.queries
  (:require [cljs.pprint :as pp]))

(defmulti read (fn [env key params] key))

(defmethod read :user [{:keys [state ast]} _ _]
  (let [st @state]
    {:value (:user st)
     :auth true }))

(defmethod read :default [{:keys [state]} key _]
  (let [st @state]
    {:value (key st)}))
