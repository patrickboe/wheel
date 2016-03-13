(ns wheel.transformations
  (:require
    [clojure.string :refer [join]]))

(defn rotate [l i]
  (let [[p1 p2] (split-at (mod i (count l)) l)]
    (concat p2 p1)))

(defn gcd [a b]
  (let [gcd-rec
        (fn gcdr [a b]
          (let [r (rem a b)]
            (if (= 0 r) b (gcdr b r))))]
    (if (< a b) (gcd-rec b a) (gcd-rec a b))))

(defn round [x]
  (.round js/Math x))

(defn interval [need avail]
  (inc (quot avail (gcd need avail))))

(defn parcel-sizes [need avail]
  (let [size (/ need avail)
        eq-shares (iterate #(+ size %) 0)
        portions (take (interval need avail) (map round eq-shares))]
    (cycle (map (fn [[x y]] (- y x)) (partition 2 1 portions)))))

(defn desc [l] (join ", " l))

(defn parcel [l [size & sizes]]
  (lazy-seq
    (if (empty? l)
      nil
      (let [[p r] (split-at size l)]
        (cons (desc p) (parcel r sizes))))))

(defn pad [l [size & sizes]]
  (lazy-seq
    (if (empty? l)
      nil
      (concat
        (cons (first l) (take (dec size) (repeat nil)))
        (pad (rest l) sizes)))))

(defn fill [chores population]
  (let [load (count chores)]
    (cond
      (or (= load 0) (= population 0)) (take population (repeat nil))
      (> load population) (parcel chores (parcel-sizes load population))
      :else (pad chores (parcel-sizes population load)))))

(defn to-assignments [props]
  (let [ps (:peeps props)
        workloads (fill (map :name (:chores props)) (count ps))]
    (interleave
      (map :name ps)
      (rotate workloads (:iteration props)))))
