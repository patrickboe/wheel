(ns wheel.ui
  (:require
    [om.next :as om :refer-macros [defui]]
    [om.dom :as dom]))

(def ENTER_KEY 13)

(defui PeepView
    static om/IQuery
    (query [this] '[:name])
    Object
    (render [this]
            (let [{name :name} (om/props this)]
              (dom/ul nil name))))

(def peep (om/factory PeepView))

(defui WheelView
  static om/IQuery
  (query [this] '[:peeps])
  Object
  (render [this]
          (let [{peeps :peeps} (om/props this)]
            (apply dom/ul nil
                   (map peep peeps)))))

(def wheel (om/factory WheelView))

(defn add-peep [control]
  (fn [event]
    (when
      (== (.-which event) ENTER_KEY)
      (let [target (.-target event)
            addition {:name (.-value target)}]
        (om/transact! control `[(add-peep ~addition)])
        (set! (.-value target) "")))))

(defui RootView
  static om/IQuery
  (query [this] '[:peeps])
  Object
  (render [this]
    (let [props (om/props this)
          add-my-peep (add-peep this)]
      (dom/div nil
               (wheel props)
               (dom/input #js
                          {:placeholder "add name"
                           :type "email"
                           :onKeyDown add-my-peep})))))
