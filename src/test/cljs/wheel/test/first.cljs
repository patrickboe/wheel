(ns wheel.test.first
  (:require
    [cljs.test :as test
     :refer-macros [deftest is testing]]
    [wheel.fifth]))

(deftest test-wheel-one
  (is (= wheel.fifth/five 5)))
