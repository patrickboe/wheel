(ns wheel.ui
  (:require
    [om.next :as om :refer-macros [defui]]
    [om.dom :as dom]))

(def ENTER_KEY 13)

(defn drop-click-handler [component name cmd]
    (fn [event]
      (om/transact! component `[(~cmd ~{:name name})])))

(defn command-on-enter [component cmd]
  (fn [event]
    (when
      (== (.-which event) ENTER_KEY)
      (let [target (.-target event)
            addition {:name (.-value target)}]
        (om/transact! component `[(~cmd ~addition)])
        (set! (.-value target) "")))))

(defui ChoreView
    static om/IQuery
    (query [this] '[:name])
    Object
    (render [this]
      (let [{name :name} (om/props this)]
        (dom/li nil
          (dom/span nil name)
          (dom/button #js
                      {:onClick (drop-click-handler this name 'chores/drop)}
                      "-")))))

(def renderChore (om/factory ChoreView))

(defui ChoresView
  Object
  (render [this]
          (let [{chores :chores} (om/props this)]
            (apply dom/ul nil
                   (map renderChore chores)))))

(def renderChores (om/factory ChoresView))

(defui PersonView
    static om/IQuery
    (query [this] '[:name])
    Object
    (render [this]
      (let [{name :name} (om/props this)]
        (dom/li nil
          (dom/span nil name)
          (dom/button #js
                      {:onClick (drop-click-handler this name 'peeps/drop)}
                      "-")))))

(def renderPerson (om/factory PersonView))

(defui PeepsView
  Object
  (render [this]
          (let [{peeps :peeps} (om/props this)]
            (apply dom/ul nil
                   (map renderPerson peeps)))))

(def renderPeeps (om/factory PeepsView))

(defui RootView
  static om/IQuery
  (query [this] '[:peeps :chores])
  Object
  (render [this]
    (dom/div nil
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
