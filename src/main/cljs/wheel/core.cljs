(ns wheel.core
  (:require
    [goog.dom :as gdom]
    [om.next :as om :refer-macros [defui]]
    [om.dom :as dom]
    [wheel.data-views :refer [read]]))

(enable-console-print!)

(defonce app-state
  (atom { :peeps [] :chores [] }))

(defui PeepView
    static om/IQuery
    (query [this] '[:name])
    Object
    (render [this]
            (let [{name :name} (om/props this)]
              (dom/ul nil name))))

(def peep (om/factory PeepView))

(defui Wheel
  static om/IQuery
  (query [this] '[:peeps])
  Object
  (render [this]
          (println "hey")
          (println (om/props this))
          (let [{peeps :peeps} (om/props this)]
            (apply dom/ul nil
                   (map peep peeps)))))

(om/add-root!
  (om/reconciler
    {:state app-state
     :parser (om/parser {:read read}) })
  Wheel
  (gdom/getElement "app")) 
