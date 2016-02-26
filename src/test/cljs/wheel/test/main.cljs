(ns wheel.test.main
  (:require
    [cljs.test :as test
     :refer-macros [deftest is testing run-tests]
     :refer [report]]
    [wheel.test.fifth]
    [wheel.test.first]))

(enable-console-print!)

(defmethod report [::test/default :summary] [m]
  (println "\nRan" (:test m) "tests containing"
           (+ (:pass m) (:fail m) (:error m)) "assertions.")
  (println (:fail m) "failures," (:error m) "errors.")
  (aset js/window "test-failures" (+ (:fail m) (:error m))))

(defn runner []
  (test/run-tests
   (test/empty-env ::test/default)
   'wheel.test.fifth
   'wheel.test.first))
