(ns wheel.test.main
  (:require
    [cljs.test :refer-macros [deftest is testing run-tests]]
    [wheel.fifth]))

(enable-console-print!)

(deftest test-wheel-five
  (is (= wheel.fifth/five 5)))

(run-tests)
