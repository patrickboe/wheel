(defproject wheel "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.170"]]

  :hooks [leiningen.cljsbuild]

  :plugins [[lein-cljsbuild "1.1.2"]
            [lein-figwheel "0.5.0-1"]]

  :clean-targets ^{:protect false} [:target-path "out" "resources/public/cljs" "resources/test/cljs"]

  :profiles {:dev {:dependencies [[com.cemerick/piggieback "0.2.1"]
                                  [figwheel-sidecar "0.5.0-1"]
                                  [org.clojure/tools.nrepl "0.2.10"]]
                   :source-paths ["src/main/cljs" "src/test/cljs" "src/dev/clj"]
                   :repl-options {:nrepl-middleware
                                  [cemerick.piggieback/wrap-cljs-repl]}}}

  :source-paths ["src/main/clj"]

  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src/main/cljs" "src/main/cljs"]
                        :figwheel true
                        :compiler {:main "wheel.core"
                                   :asset-path "cljs/out"
                                   :output-to "resources/public/cljs/main.js"
                                   :output-dir "resources/public/cljs/out"}}
                       {:id "test"
                        :source-paths ["src/main/cljs" "src/test/cljs"]
                        :notify-command ["phantomjs"
                                         :cljs.test/runner
                                         "resources/test/cljs/main.js"]
                        :compiler {:main "wheel.test.main"
                                   :output-to "resources/test/cljs/main.js"
                                   :pretty-print true
                                   :optimizations :whitespace}}]
              :test-commands {"unit" ["phantomjs"
                                      "src/dev/js/phantom/unit-test.js"
                                      "resources/test/index.html"]}})
