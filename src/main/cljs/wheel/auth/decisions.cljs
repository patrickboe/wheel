(ns wheel.auth.decisions
  (:require-macros
    [cljs.core.async.macros :refer [go go-loop]])
  (:require
    [cljs.pprint :as pp]
    [cljs.core.async :as a
                     :refer [>! <! chan promise-chan close!]]
    [cljs.core.match :refer-macros [match]]))

(defn handle-parse-result [f err parsed-hash]
  (match parsed-hash
         {:id_token x} (f x)
         {:error x :error-description y} (err {:error x :description y})))

(defn to-registration-request [profile] profile)

(defn attach-workflow [[in out] input callback]
  (go (>! in input))
  (go (loop [x (<! out)]
        (when x
          (callback x)
          (recur (<! out))))))

(defn login-workflow-for
  ([profile-service registration-service]
   (login-workflow-for profile-service registration-service to-registration-request))

  ([profile-service registration-service prof-to-reg]
   (let [updates (chan)
         in (promise-chan)
         reg-err (promise-chan)
         prof-err (promise-chan)
         err (a/merge [reg-err prof-err])
         to-reg (promise-chan (map prof-to-reg))
         profiled (profile-service in prof-err)
         registered (registration-service to-reg reg-err)]

     (go (let [p (<! profiled)]
           (>! updates p)
           (>! to-reg p)))

     (go (>! updates (<! registered))
         (close! updates))

     (go (>! updates (<! err))
         (close! updates))

     [in updates])))
