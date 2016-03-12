(ns wheel.test.commands
  (:require
    [cljs.test :as test
     :refer-macros [deftest is testing]]
    [wheel.commands :refer [command]]))

(deftest drop-drops-chore
  (let [s (atom {:chores
                 [{:name "dishes"}
                  {:name "sweeping"}]})]

    ((:action
       (command {:state s}
                 'chores/drop
                 {:name "sweeping"})))

    (is (= {:chores
            [{:name "dishes"}]}
           @s))))

(deftest add-adds-chore
  (let [s (atom {:chores
                 [{:name "dishes"}
                  {:name "shopping"} ] })]
    ((:action
       (command {:state s}
                'chores/add
                {:name "sweeping"})))
    (is (= {:chores
            [{:name "dishes"}
             {:name "shopping"}
             {:name "sweeping"} ]}
           @s))))

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

(deftest turn-increments-counter
  (let [s (atom {:iteration 0})]

    ((:action
       (command {:state s}
                 'wheel/turn)))

    (is (= {:iteration 1} @s))))
