(ns wheel.test
  (:require
    [cljs.test :refer-macros [deftest is testing run-tests]]
    [wheel.fifth]
    ))

(enable-console-print!)

(deftest test-wheel-five
  (is (= wheel.fifth/five 6)))

(defn ^:export run []
  (run-tests))
