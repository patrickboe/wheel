(ns wheel.test
  (:require
    [cljs.test :refer-macros [deftest is testing run-tests]]
    [wheel.core]
    ))

(deftest test-wheel-one
  (is (= wheel.core/one 1)))
