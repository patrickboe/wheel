(ns wheel.transformations
  (:require
    [clojure.string :refer [join]]))

(defn rotate [l i]
  (let [[p1 p2] (split-at (mod i (count l)) l)]
    (concat p2 p1)))

(defn div [n d]
  [(quot n d) (rem n d)])

(defn nils [n] (take n (repeat nil)))

(defn odd-spacers [n fill-per fill-rem]
  (let [boost-interval (quot n fill-rem)]
    (cycle (cons
             (nils (inc fill-per))
             (take boost-interval (repeat (nils fill-per)))))))

(defn regular-spacers [fill-per]
  (repeat (take fill-per (repeat nil))))

(defn spacers [n fill-per fill-rem]
  (if (= fill-rem 0)
    (regular-spacers fill-per)
    (odd-spacers n fill-per fill-rem)))

(defn fill-populated [l pad-to n]
  (let [fill-needed (- pad-to n)
        [fill-per fill-rem] (div fill-needed n)]
        (flatten (map cons l (spacers n fill-per fill-rem)))))

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

(defn parcel [l [size & sizes]]
  (lazy-seq
    (if (empty? l)
      nil
      (let [[p r] (split-at size l)]
        (cons p (parcel r sizes))))))

(defn fill [l avail]
  (let [need (count l)]
    (cond
      (or (= need 0) (= avail 0)) (take avail (repeat nil))
      (> need avail) (map #(join ", " %) (parcel l (parcel-sizes need avail)))
      :else (fill-populated l avail need))))

(defn to-assignments [props]
  (let [ps (:peeps props)
        workloads (fill (map :name (:chores props)) (count ps))]
    (interleave
      (map :name ps)
      (rotate workloads (:iteration props)))))
