(def input
  {19 1
   20 2
   14 3
   0  4
   9  5
   1  6})

(declare nextNumber)

(defn calculateNumber [indexes, i, prevnum, isfirst, target]
  (let [index   (get indexes prevnum -1)
        current (if isfirst 0 (- i index 1))]

    (nextNumber (conj indexes {prevnum (- i 1)})
                   i
                   current
                   (not (contains? indexes current))
                   target)))

(defn nextNumber [indexes, i, current, isfirst, target]
  #(if (= i target) current (calculateNumber indexes (+ i 1) current isfirst target)))


(println "part1")
(println (trampoline nextNumber input 6 1 true 2020))


(println "part2")
(println (trampoline nextNumber input 6 1 true 30000000))

