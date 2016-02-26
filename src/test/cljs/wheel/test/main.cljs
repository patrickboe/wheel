(ns wheel.test.main
    (:require [cljs.test :as test]
              [doo.runner :refer-macros [doo-all-tests doo-tests]]
              [wheel.test.fifth]
              [wheel.test.first]))

(doo-tests 'wheel.test.fifth
           'wheel.test.first)
