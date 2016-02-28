(ns user
  (:require [cemerick.piggieback]
            [cljs.repl.rhino]
            [cljs.repl.nashorn]
            [cljs.repl.node]
            [cemerick.austin])
  (:use [figwheel-sidecar.repl-api :as ra]))

(def rhino-env cljs.repl.rhino/repl-env)

(def node-env cljs.repl.node/repl-env)

(def nashorn-env cljs.repl.nashorn/repl-env)

(def austin-env cemerick.austin/exec-env)

(defn pig []
  (cemerick.piggieback/cljs-repl
    (cljs.repl.rhino/repl-env)))

(defn fig [] (ra/start-figwheel!))

(defn unfig [] (ra/stop-figwheel!))

(defn fig-repl [] (ra/cljs-repl "dev"))
