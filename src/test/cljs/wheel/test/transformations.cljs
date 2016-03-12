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

(deftest first-iteration-has-shifted-assignment
  (is (= ["Dave" "Dishes" "Mandy" "Coffee"]
         (to-assignments
           {:chores [{:name "Coffee"} {:name "Dishes"}]
            :peeps [{:name "Dave"} {:name "Mandy"}]
            :iteration 1 }))))

(deftest iteration-wraps
  (is (= ["Dave" "Dishes" "Mandy" "Coffee"]
         (to-assignments
           {:chores [{:name "Coffee"} {:name "Dishes"}]
            :peeps [{:name "Dave"} {:name "Mandy"}]
            :iteration 3 }))))

(deftest sparse-tasks-are-assigned-to-some
  (is (= ["Dave" "Dishes" "Mandy" nil]
         (to-assignments
           {:chores [{:name "Dishes"}]
            :peeps [{:name "Dave"} {:name "Mandy"}]
            :iteration 0 }))))

(deftest sparse-tasks-are-assigned-intermittently
  (is (= ["Dave" "Dishes" "Mandy" nil "Ralph" "Coffee" "Judy" nil]
         (to-assignments
           {:chores [{:name "Dishes"}
                     {:name "Coffee"}]
            :peeps [{:name "Dave"}
                    {:name "Mandy"}
                    {:name "Ralph"}
                    {:name "Judy"}]
            :iteration 0 }))))

(deftest fractional-tasks-are-distributed-evenly
  (is (= ["Dave" "Dishes" "Mandy" nil "Ralph" nil "Judy" "Coffee" "Bob" nil "Julie" nil]
         (to-assignments
           {:chores [{:name "Dishes"} {:name "Coffee"}]
            :peeps [{:name "Dave"}
                    {:name "Mandy"}
                    {:name "Ralph"}
                    {:name "Judy"}
                    {:name "Bob"}
                    {:name "Julie"}]
            :iteration 0 }))))

(deftest fractional-tasks-progress-stepwise
  (is (= ["Dave" nil "Mandy" nil "Ralph" "Coffee" "Judy" nil "Bob" nil "Julie" "Dishes"]
         (to-assignments
           {:chores [{:name "Dishes"} {:name "Coffee"}]
            :peeps [{:name "Dave"}
                    {:name "Mandy"}
                    {:name "Ralph"}
                    {:name "Judy"}
                    {:name "Bob"}
                    {:name "Julie"}]
            :iteration 1 }))))

(deftest uneven-divisions-have-distributed-padding
  (is (= ["Dave" "Dishes" "Mandy" nil "Ralph" nil "Judy" "Coffee" "Bob" nil "Julie" "Sweeping" "Patrick" nil "Anna" nil]
         (to-assignments
           {:chores [{:name "Dishes"} {:name "Coffee"} {:name "Sweeping"}]
            :peeps [{:name "Dave"}
                    {:name "Mandy"}
                    {:name "Ralph"}
                    {:name "Judy"}
                    {:name "Bob"}
                    {:name "Julie"}
                    {:name "Patrick"}
                    {:name "Anna"}]
            :iteration 0}))))
