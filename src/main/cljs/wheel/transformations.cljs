(ns wheel.transformations)

(defn to-assignments [props]
  (interleave (map :name (:peeps props))
              (map :name (:chores props))))
