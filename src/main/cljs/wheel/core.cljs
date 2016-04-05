(ns wheel.core
  (:require
    [goog.dom :as gdom]
    [om.next :as om :refer-macros [defui]]
    [wheel.ui :as ui]
    [wheel.auth :as auth]
    [wheel.commands :refer [command]]
    [wheel.queries :refer [read]]))

(enable-console-print!)

(defn logged-in [user]
  { :peeps []
    :chores []
    :iteration 0
    :user user })

(defn authenticating [token] { :user token })

(defn green [] { :user :anonymous })

(defn logged-out []
  (let [t (auth/access-token)]
    (if t
      (authenticating t)
      (green))))

(defn load-initial-state []
  (let [u (auth/load-user)]
    (if u
     (logged-in u)
     (logged-out))))

(defonce app-state
  (atom (load-initial-state)))

(om/add-root!
  (om/reconciler
    {:state app-state
     :normalize true
     :send auth/authenticate
     :remotes [:auth]
     :parser (om/parser {:read read :mutate command})})
  ui/RootView
  (gdom/getElement "app"))
