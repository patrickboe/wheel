(ns wheel.ui
  (:require
    [om.next :as om :refer-macros [defui]]
    [om.dom :as dom]
    [wheel.transformations :refer [to-assignments]]
    ))

(def ENTER_KEY 13)

(defn drop-click-handler [component name cmd]
    (fn [event]
      (om/transact! component `[(~cmd ~{:name name})])))

(defn command [component cmd]
  (fn [event]
    (om/transact! component `[(~cmd)])))

(defn command-on-enter [component cmd]
  (fn [event]
    (when
      (== (.-which event) ENTER_KEY)
      (let [target (.-target event)
            addition {:name (.-value target)}]
        (om/transact! component `[(~cmd ~addition)])
        (set! (.-value target) "")))))

(defn render-item [this drop-cmd]
  (let [{name :name} (om/props this)]
    (dom/li nil
      (dom/span nil name)
      (dom/button #js
                  {:onClick (drop-click-handler this name drop-cmd)}
                  "-"))))

(defn render-list [this selector render-i]
  (let [{items selector} (om/props this)]
    (apply dom/ul nil
           (map render-i items))))

(defui ChoreView
    static om/IQuery
    (query [this] '[:name])
    Object
    (render [this] (render-item this 'chores/drop)))

(def renderChore (om/factory ChoreView))

(defui ChoresView
  Object
  (render [this]
      (render-list this :chores renderChore)))

(def renderChores (om/factory ChoresView))

(defui PersonView
    static om/IQuery
    (query [this] '[:name])
    Object
    (render [this] (render-item this 'peeps/drop)))

(def renderPerson (om/factory PersonView))

(defui PeepsView
  Object
  (render [this]
      (render-list this :peeps renderPerson)))

(def renderPeeps (om/factory PeepsView))

(defui WheelView
  Object
  (render [this]
          (let [assignments (to-assignments (om/props this))]
            (apply dom/dl nil
                   (map apply (cycle [dom/dt dom/dd]) (repeat nil) assignments)))))

(def renderWheel (om/factory WheelView))

(defui RootView
  static om/IQuery
  (query [this] '[:peeps :chores :iteration])
  Object
  (render [this]
    (dom/div nil
             (dom/div nil
                      (renderWheel (om/props this) )
                      (dom/button #js
                                 {:onClick (command this 'wheel/turn)}
                                 "turn"))
             (dom/div nil
                      (renderChores (om/props this))
                      (dom/input #js
                                 {:placeholder "add chore"
                                  :type "text"
                                  :onKeyDown (command-on-enter this 'chores/add)}))
             (dom/div nil
                      (renderPeeps (om/props this))
                      (dom/input #js
                                 {:placeholder "add name"
                                  :type "text"
                                  :onKeyDown (command-on-enter this 'peeps/add)})))))
