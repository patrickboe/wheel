(ns wheel.test.data-views
  (:require
    [cljs.test :as test
     :refer-macros [deftest is testing]]
    [wheel.data-views :refer [read]]))

(deftest read-peeps-gets-peeps
  (let [s
        (atom {:peeps [{:name "fido"}
                       {:name "spike"}]})]
    (is (= {:value [{:name "fido"}
                    {:name "spike"}]}
           (read {:state s} :peeps nil)))))
