(ns wheel.ui
  (:require
    [om.next :as om :refer-macros [defui]]
    [om.dom :as dom]))

(def ENTER_KEY 13)

(defn drop-peep-click [component person-name]
  (fn [event]
    (om/transact! component `[(peeps/drop ~{:name person-name})])))

(defui PeepView
    static om/IQuery
    (query [this] '[:name])
    Object
    (render [this]
            (let [{name :name} (om/props this)]
              (dom/li nil
                      (dom/span nil name)
                      (dom/button #js {:onClick (drop-peep-click this name)} "-")))))

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

(defn new-peep-keydown [component]
  (fn [event]
    (when
      (== (.-which event) ENTER_KEY)
      (let [target (.-target event)
            addition {:name (.-value target)}]
        (om/transact! component `[(peeps/add ~addition)])
        (set! (.-value target) "")))))

(defui RootView
  static om/IQuery
  (query [this] '[:peeps])
  Object
  (render [this]
    (dom/div nil
             (wheel (om/props this))
             (dom/input #js
                        {:placeholder "add name"
                         :type "email"
                         :onKeyDown (new-peep-keydown this)}))))
