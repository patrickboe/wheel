(ns wheel.test.queries
  (:require
    [cljs.test :as test
     :refer-macros [deftest is testing]]
    [wheel.queries :refer [read]]))

(deftest read-peeps-gets-peeps
  (let [s
        (atom {:peeps [{:name "fido"}
                       {:name "spike"}]})]
    (is (= {:value [{:name "fido"}
                    {:name "spike"}]}
           (read {:state s} :peeps nil)))))
