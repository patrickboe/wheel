(ns wheel.auth
  (:require
    [cljs.core.match :refer-macros [match]]
    [cljs.pprint :as pp]))

(defonce ^:private lock
  (js/Auth0Lock.
    "aC17miYcAZM4eBV4N1slaTaPiCazJN6o"
    "patrickboe.auth0.com"))

(defn ^:private read-token []
  (js->clj (.parseHash lock (aget js/window "location" "hash"))))

(defn ^:private read-user []
  (or (read-token) :anonymous))

(defn ^:private login-dialog []
  (.show lock #js {:authParams #js {:scope "openid"}}))

(defn remote [msg cb]
  (match msg
         {:auth [:user]} (cb {:user (read-user)})
         {:auth [(['user/login] :seq)]} (login-dialog)
         _ nil))
