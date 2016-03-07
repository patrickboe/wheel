(ns wheel.core
  (:require
    [goog.dom :as gdom]
    [om.next :as om :refer-macros [defui]]
    [wheel.ui :as ui]
    [wheel.commands :refer [command]]
    [wheel.data-views :refer [read]]))

(enable-console-print!)

(defonce app-state
  (atom { :peeps [] :chores [] }))

(om/add-root!
  (om/reconciler
    {:state app-state
     :parser (om/parser {:read read :mutate command}) })
  ui/RootView
  (gdom/getElement "app"))
