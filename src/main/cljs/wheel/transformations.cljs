(ns wheel.transformations)

(defn rotate [l i]
  (let [[p1 p2] (split-at (mod i (count l)) l)]
    (concat p2 p1)))

(defn div [n d]
  [(quot n d) (rem n d)])

(defn odd-spacers [n fill-per fill-rem]
  (let [boost-interval (quot n fill-rem)]
    (cycle (cons
             (take (inc fill-per) (repeat nil))
             (take boost-interval
                       (repeat (take fill-per (repeat nil))))))))

(defn regular-spacers [fill-per]
  (repeat (take fill-per (repeat nil))))

(defn fill [l pad-to]
  (let [n (count l)
        fill-needed (- pad-to n)
        [fill-per fill-rem] (div fill-needed n)
        spacers (if (= fill-rem 0)
                  (regular-spacers fill-per)
                  (odd-spacers n fill-per fill-rem))]
    (flatten (map cons l spacers))))

(defn to-assignments [props]
  (let [ps (:peeps props)
        workloads (fill (:chores props) (count ps))]
    (interleave
      (map :name ps)
      (rotate (map :name workloads) (:iteration props)))))
