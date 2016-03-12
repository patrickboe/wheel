(ns wheel.test.transformations
  (:require
    [cljs.test :as test
     :refer-macros [deftest is testing]]
    [wheel.transformations :refer [to-assignments]]))

(deftest zeroth-iteration-has-equal-assignment
  (is (= ["Dave" "Coffee" "Mandy" "Dishes"]
         (to-assignments
           {:chores [{:name "Coffee"} {:name "Dishes"}]
            :peeps [{:name "Dave"} {:name "Mandy"}]
            :iteration 0 }))))
