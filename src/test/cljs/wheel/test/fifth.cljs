(ns wheel.test.fifth
  (:require
    [cljs.test :as test
     :refer-macros [deftest is testing]]
    [wheel.fifth]))

(deftest test-wheel-five
  (is (= wheel.fifth/five 5)))
