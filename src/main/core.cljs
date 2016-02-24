(ns wheel.core
  (:require[cljs.test :refer-macros [deftest is testing run-tests]]))

(.log js/console "Hey Seymore what")

(deftest test-numbers
  (is (= 1 1)))
