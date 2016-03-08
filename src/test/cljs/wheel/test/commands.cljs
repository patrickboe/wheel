(ns wheel.test.commands
  (:require
    [cljs.test :as test
     :refer-macros [deftest is testing]]
    [wheel.commands :refer [command]]))

(deftest drop-drops-person
  (let [s (atom {:peeps
                 [{:name "fido"}
                  {:name "spike"}]})]

    ((:action
       (command {:state s}
                 'peeps/drop
                 {:name "fido"})))

    (is (= {:peeps
            [{:name "spike"}]}
           @s))))

(deftest add-adds-person
  (let [s (atom {:peeps
                 [{:name "fido"}
                  {:name "spike"}]})]

    ((:action
       (command {:state s}
                 'peeps/add
                 {:name "clifford"})))

    (is (= {:peeps
             [{:name "fido"}
              {:name "spike"}
              {:name "clifford"}]}
           @s))))
