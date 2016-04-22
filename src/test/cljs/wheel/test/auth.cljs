(ns wheel.test.auth
  (:require-macros
    [cljs.core.async.macros :refer [go go-loop]])
  (:require
    [cljs.pprint :as pp]
    [cljs.test :as test
               :refer-macros [deftest is testing async]]
    [cljs.core.async :as a
                     :refer [>! <! chan promise-chan close! onto-chan pipe]]
    [wheel.auth.decisions :refer [attach-workflow handle-parse-result login-workflow-for]]))

(defn ignore [x])

(deftest token-is-extracted-from-hash
  (is (= "x" (handle-parse-result identity ignore {:id_token "x"}))))

(deftest hash-parse-error-is-identified
  (is (= {:error "x" :description "y"}
         (handle-parse-result ignore identity
           {:error "x" :error-description "y" :bleh "z" :foo "a" }))))

(defn doubling-service [in err]
  (let [out (promise-chan)]
    (go (>! out (* 2 (<! in))))
    out))

(defn stringifier [in err]
  (let [out (promise-chan)]
    (go (>! out (str (<! in))))
    out))

(def dud-err {:error "bad" :message "a message"})

(defn dud [in err]
  (let [out (promise-chan)]
    (go (<! in) (>! err dud-err))
    out))

(deftest service-outputs-are-passed-through-and-updated
  (let [[wf-in wf-updates] (login-workflow-for doubling-service stringifier identity)]
    (async done
      (go (>! wf-in 1))
      (go
        (is (= [2 "2"] (<! (a/into [] wf-updates))))
        (done)))))

(deftest profile-errors-are-propagated
  (let [[wf-in wf-updates] (login-workflow-for dud stringifier identity)]
    (async done
      (go (>! wf-in 1))
      (go
        (is (= [dud-err] (<! (a/into [] wf-updates))))
        (done)))))

(deftest reg-errors-are-propagated
  (let [[wf-in wf-updates] (login-workflow-for doubling-service dud identity)]
    (async done
      (go (>! wf-in 1))
      (go
        (is (= [2 dud-err] (<! (a/into [] wf-updates))))
        (done)))))

(deftest attach-workflow-exhausts-the-workflow
  (let [in (chan)
        out (chan)
        results (chan)
        cb (fn [x] (go (>! results x)))
        ticking-workflow [in out]
        first-3-results (a/into [] (a/take 3 results))]
   (go (pipe (a/to-chan (repeat (<! in) 'tick)) out))
   (attach-workflow ticking-workflow 3 cb)
   (async done
     (go (do
           (<! (a/timeout 1000))
           (is false "timeout expired")
           (done)))
     (go
       (is (= ['tick 'tick 'tick] (<! first-3-results)))
       (done)))))
