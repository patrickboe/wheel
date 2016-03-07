(ns wheel.test.commands
  (:require
    [cljs.test :as test
     :refer-macros [deftest is testing]]
    [wheel.commands :refer [command]]))

(deftest add-adds-person
  (let [s (atom {:peeps
                 [{:name "fido"}
                  {:name "spike"}]})]

    ((:action
       (command {:state s}
                 :add-peep
                 {:name "clifford"})))

    (is (= {:peeps
             [{:name "fido"}
              {:name "spike"}
              {:name "clifford"}]}
           @s))))
