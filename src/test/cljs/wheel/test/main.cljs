(ns wheel.test.main
    (:require [cljs.test :as test]
              [doo.runner :refer-macros [doo-all-tests doo-tests]]
              [wheel.test.queries]
              [wheel.test.auth]
              [wheel.test.transformations]
              [wheel.test.commands]))

(doo-tests 'wheel.test.queries
           'wheel.test.commands
           'wheel.test.auth
           'wheel.test.transformations)
