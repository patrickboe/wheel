(ns wheel.auth
  (:require
    [cljs.core.match :refer-macros [match]]
    [cljs.pprint :as pp]))

(defonce ^:private lock
  (js/Auth0Lock.
    "aC17miYcAZM4eBV4N1slaTaPiCazJN6o"
    "patrickboe.auth0.com"))

(defn ^:private read-user []
  (or
    (js->clj (.parseHash lock (aget (aget js/window "location") "hash")))
    :anonymous))

(defn ^:private login-dialog []
  (.show lock #js {:authParams #js {:scope "openid"}}))

(defn remote [{:keys [auth]} cb]
  (match auth
         [:user] (cb {:user (read-user)})
         [(['user/login] :seq)] (login-dialog)
         _ nil))
