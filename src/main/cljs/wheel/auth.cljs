(ns wheel.auth
  (:require [cljs.pprint :as pp]))

(defonce ^:private lock
  (js/Auth0Lock.
    "aC17miYcAZM4eBV4N1slaTaPiCazJN6o"
    "patrickboe.auth0.com"))

(defn authenticate [{:keys [auth]} cb]
  (when auth
    (.show lock #js {:authParams #js {:scope "openid"}})))

(defn load-user [] nil)

(defn access-token []
  (js->clj (.parseHash lock (aget (aget js/window "location") "hash"))))
