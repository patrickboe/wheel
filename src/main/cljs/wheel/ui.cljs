(ns wheel.ui
  (:require
    [cljs.pprint :as pp]
    [om.next :as om :refer-macros [defui]]
    [cljs.core.match :refer-macros [match]]
    [om.dom :as dom]
    [wheel.transformations :refer [to-assignments]]))

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
  static om/IQuery
  (query [this] '[:chores])
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
  static om/IQuery
  (query [this] '[:peeps])
  Object
  (render [this]
      (render-list this :peeps renderPerson)))

(def renderPeeps (om/factory PeepsView))

(defui WheelView
  static om/IQuery
  (query [this] '[:peeps :chores :iteration])
  Object
  (render [this]
          (let [assignments (to-assignments (om/props this))]
            (apply dom/dl nil
                   (map apply (cycle [dom/dt dom/dd]) (repeat nil) assignments)))))

(def renderWheel (om/factory WheelView))

(defui LoginView
  static om/IQuery
  (query [this] '[])
  Object
  (render [this]
     (dom/button #js {:onClick (command this 'user/login)} "login")))

(def renderLogin (om/factory LoginView))

(defui AuthenticatingView
  Object
  (render [this] (dom/h1 "authenticating")))

(def renderAuthenticating (om/factory AuthenticatingView))

(defui ChoreSpaceView
  static om/IQuery
  (query [this] (vec (distinct (concat (om/get-query WheelView)
                                       (om/get-query ChoresView)
                                       (om/get-query PeepsView)))))
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

(def renderChoreSpace (om/factory ChoreSpaceView))

(defui RootView
  static om/IQuery
  (query [this] (into [:user] (om/get-query ChoreSpaceView)))
  Object
  (render [this]
    (let [ps (om/props this)]
      (match (:user ps)
        {} (renderChoreSpace ps)
        :anonymous (renderLogin)
        :else (renderAuthenticating)))))
