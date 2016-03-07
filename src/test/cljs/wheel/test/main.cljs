(ns wheel.test.main
    (:require [cljs.test :as test]
              [doo.runner :refer-macros [doo-all-tests doo-tests]]
              [wheel.test.data-views]
              [wheel.test.commands]))

(doo-tests 'wheel.test.data-views
           'wheel.test.commands)
