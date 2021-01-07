(def input
  {19 1
   20 2
   14 3
   0  4
   9  5
   1  6})

(defn calculateNumber [indexes, i, prevnum, isfirst nextNumber]
  (let [index   (get indexes prevnum -1)
        current (if isfirst 0 (- i index 1))]

    (nextNumber (conj indexes {prevnum (- i 1)})
              i
              current
              (not (contains? indexes current)))))

(defn nextNumber [target, indexes, i, current, isfirst]
  (let [nextNumber (partial nextNumber target)]
    #(if (= i target)
      current
      (calculateNumber indexes (+ i 1) current isfirst nextNumber))))

(println "part1")
(println (trampoline nextNumber 2020 input 6 1 true))

(println "part2")
(println (trampoline nextNumber 30000000 input 6 1 true))
