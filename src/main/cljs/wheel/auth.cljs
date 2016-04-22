(ns wheel.auth
  (:require-macros
    [cljs.core.async.macros :refer [go go-loop]])
  (:require
    [cljs.core.match :refer-macros [match]]
    [wheel.auth.decisions :refer [login-workflow-for attach-workflow]]
    [cljs.core.async :as a
                     :refer [>! <! chan promise-chan]]
    [cljs.pprint :as pp]))

(defonce ^:private lock
  (js/Auth0Lock.
    "aC17miYcAZM4eBV4N1slaTaPiCazJN6o"
    "patrickboe.auth0.com"))

(defn ^:private parse-session [url-hash]
  (js->clj (.parseHash lock url-hash) ))

(defn ^:private attempt-login [sesh cb])

(defn ^:private login-dialog []
  (.show lock #js {:authParams #js {:scope "openid"}}))

(defn ^:private with-profile-from [sesh cb] 1)

(defn ^:private register [req cb] 1)

(defn profile-service [in err]
  (let [out (promise-chan)
        cb (fn [e profile] (go (if e (>! err e) (>! out profile))))]
    (go (.getProfile lock (<! in) cb))
    out))

(defn registration-service [in err]
  (let [out (promise-chan)]
    (go (<! in) (>! out "some user"))
    out))

(defn remote [msg cb]
  (match msg
         {:auth [(['user/login] :seq)]} (login-dialog)
         {:auth [(['location/route {:url-hash h}] :seq)]}
           (if-let [sesh (parse-session h)]
             (attach-workflow (login-workflow-for profile-service registration-service)
                              sesh
                              cb))
         _ nil))
