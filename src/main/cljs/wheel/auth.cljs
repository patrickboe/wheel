(ns wheel.auth)

(defonce lock
  (js/Auth0Lock.
    "aC17miYcAZM4eBV4N1slaTaPiCazJN6o"
    "patrickboe.auth0.com"))

(defn authenticate [{:keys [auth]} cb]
  (when auth
    (.show lock #js {:authParams #js {:scope "openid"}})))
