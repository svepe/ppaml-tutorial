;; gorilla-repl.fileformat = 1

;; **
;;; # Clojure Exercises
;; **

;; @@
(ns clojure-exercises
   (:require [clojure.repl :as repl])
   (:use [anglican.runtime :exclude [sum]]))

(def ...complete-this... nil)
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;clojure-exercises/...complete-this...</span>","value":"#'clojure-exercises/...complete-this..."}
;; <=

;; **
;;; 
;;; ## Exercise 1: Summing values
;;; 
;;; Complete the function below by replacing `...complete-this...` with the correct expressions
;; **

;; @@
(defn sum 
  "returns the sum of values in a collection"
  [values]
  (loop [result 0.0
         values values]
    (if (seq values)
      (recur (+ result (first values))
             (rest values))
      result)))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;clojure-exercises/sum</span>","value":"#'clojure-exercises/sum"}
;; <=

;; **
;;; You can test your function using the command
;; **

;; @@
(sum [1 2 3])
; => 6.0
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-double'>6.0</span>","value":"6.0"}
;; <=

;; **
;;; Note that the sum function returns a double, whereas the inputs are longs. Can you explain why this happens?
;;; 
;;; Rewrite the sum function so it preserves the type of the input
;; **

;; @@
(defn sum 
  "returns the sum of values in a collection"
  [values]
  (loop [result nil
         values values]
    (if (seq values)
      (if result
        (recur (+ result (first values))
               (rest values))
        (recur (first values)
               (rest values)))
      result)))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;clojure-exercises/sum</span>","value":"#'clojure-exercises/sum"}
;; <=

;; **
;;; Now test that the sum function preserves input types
;; **

;; @@
(sum [1.0 2.0 3.0])
; => 6.0


(sum [1 2 3])
; => 6
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-long'>6</span>","value":"6"}
;; <=

;; **
;;; Rewrite the `sum` function using the `reduce` command
;; **

;; @@
(defn sum [values]
  (reduce + values))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;clojure-exercises/sum</span>","value":"#'clojure-exercises/sum"}
;; <=

;; @@
(sum [1.0 2.0 3.0])
; => 6.0


(sum [1 2 3])
; => 6
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-long'>6</span>","value":"6"}
;; <=

;; **
;;; Now let's write a cumulative sum function that maps a collection of numbers `[1 2 3 4]` onto the partial sums `[1 3 6 10]`
;; **

;; @@
(defn cumsum 
  "returns a vector of partial sums"
  [values]
  (loop [results nil
         values values]
    (if (seq values)
      (if results
        (recur (conj results 
                     (+ (last results) 
                        (first values)))
               (rest values))
        (recur [(first values)]
               (rest values)))
      results)))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;clojure-exercises/cumsum</span>","value":"#'clojure-exercises/cumsum"}
;; <=

;; **
;;; Test your function
;; **

;; @@
(cumsum (list 1 2 3 4))
; => [1 3 6 10]
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>1</span>","value":"1"},{"type":"html","content":"<span class='clj-long'>3</span>","value":"3"},{"type":"html","content":"<span class='clj-long'>6</span>","value":"6"},{"type":"html","content":"<span class='clj-long'>10</span>","value":"10"}],"value":"[1 3 6 10]"}
;; <=

;; **
;;; ## Exercise 2: Higher-order functions
;; **

;; **
;;; The `map` function is an example of a higher-order function, which is to say that it is a function that accepts a function as an argument. In this exercise we will look at some of Clojure's higher-order functions, and write implementations of our own.
;; **

;; **
;;; ### Exercise 2a: Map
;;; 
;;; Let's start with `mapv`, which is a variant of `map` that returns a vector. 
;; **

;; @@
(defn my-mapv 
  [f values]
  (loop [results []
         values values]
    (if (seq values)
      (recur (conj results
                   (f (first values)))
             (rest values))
      results)))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;clojure-exercises/my-mapv</span>","value":"#'clojure-exercises/my-mapv"}
;; <=

;; **
;;; Test your `mapv` function
;; **

;; @@
(my-mapv #(* % %) 
         (range 5))
; => [0 1 4 9 16]

(my-mapv #(* % %) nil)
; => []
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[],"value":"[]"}
;; <=

;; **
;;; ### Exercise 2b: Comp
;;; 
;;; We will now define a function `comp`, which takes two functions `f` and `g` as an argument and returns a function `h` such that `(h x) -> (f (g x))`. 
;;; 
;;; Let's start by considering the case where `f` and `g` accept a single argument. Complete the following code
;; **

;; @@
(defn my-comp [f g]
  (fn [x]
    (f (g x))))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;clojure-exercises/my-comp</span>","value":"#'clojure-exercises/my-comp"}
;; <=

;; **
;;; Test this code by composing `sqrt` and `sqr`:
;; **

;; @@
(let [f sqrt
      g (fn [x] 
         (* x x))
      h (my-comp f g)]
  (h -10))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-double'>10.0</span>","value":"10.0"}
;; <=

;; **
;;; Now let's generalize this function to accept a variable number of arguments. The following code defines a function with an `args` that accepts a variable number of arguments
;; **

;; @@
(let [f (fn [& args]
          (prn args))]
  (f 1)
  (f 2 3 4))
;; @@
;; ->
;;; (1)
;;; (2 3 4)
;;; 
;; <-
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; **
;;; In order to pass a variable length set of arguments to a function, we will need the `apply` function, which can be called `(apply f args)` to call a function with a (variable length) sequence of arguments `args`.  
;; **

;; @@
(let [args [1 2 3]]
  ;; this is equivalent to (+ 1 2 3)
  (apply + args))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-long'>6</span>","value":"6"}
;; <=

;; **
;;; Use the `apply` function to define a `comp` function that is agnostic of the number of input arguments
;; **

;; @@
(defn my-comp [f g]
  (fn [& args]
  	(f (apply g args))))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;clojure-exercises/my-comp</span>","value":"#'clojure-exercises/my-comp"}
;; <=

;; **
;;; Test your function
;; **

;; @@
(let [f (my-comp abs *)]
  (f -1 2 -3 4 -5))
; => 120
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-unkown'>120</span>","value":"120"}
;; <=

;; **
;;; ### Exercise 2c Reduce
;;; 
;;; As a final exercise, let's implement the `reduce` function. This function has two signatures:
;;; 
;;; 1. `(reduce f init values)`: repeatedly call `(f result value)` for each `value` in `values` where `result` is the result of the previous function call, and is initialized to `init`.
;;; 2. `(reduce f values)`: perform the above operation, initializing `init` to `(first values)` and replacing `values` with `(rest values)`. 
;;; 
;;; Complete the following code (*hint*: look at the `loop` and `recur` patterns from Exercise 1)
;; **

;; @@
(defn my-reduce 
  ([f init values]
   (loop [result init
          values values]
     (if (seq values)
       (recur (f result (first values))
              (rest values))
       result)))
  ([f values]
   (loop [result (first values)
          values (rest values)]
     (if (seq values)
       (recur (f result (first values))
              (rest values))
       result))))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;clojure-exercises/my-reduce</span>","value":"#'clojure-exercises/my-reduce"}
;; <=

;; **
;;; Test your code
;; **

;; @@
(my-reduce + [1 2 3 4])

; => 10

(my-reduce (fn [sums v]
             (conj sums
                   (if (seq sums)
                     (+ (peek sums) v)
                     v)))
           []
           [1 2 3 4])
; => [1 3 6 10]

(my-reduce + nil)
; => 0
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; @@

;; @@
