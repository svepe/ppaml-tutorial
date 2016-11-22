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

;; **
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
      (recur ...complete-this...
             ...complete-this...)
      result)))
;; @@

;; **
;;; You can test your function using the command
;; **

;; @@
(sum [1 2 3])
; => 6.0
;; @@

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
        (recur ...complete-this...
               ...complete-this...)
        (recur ...complete-this...
               ...complete-this...))
      result)))
;; @@

;; **
;;; Now test that the sum function preserves input types
;; **

;; @@
(sum [1.0 2.0 3.0])
; => 6.0


(sum [1 2 3])
; => 6
;; @@

;; **
;;; Rewrite the `sum` function using the `reduce` command
;; **

;; @@
(defn sum [values]
  ...complete-this...)
;; @@

;; @@
(sum [1.0 2.0 3.0])
; => 6.0


(sum [1 2 3])
; => 6
;; @@

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
        (recur ...complete-this...
               ...complete-this...)
        (recur ...complete-this...
               ...complete-this...))
      results)))
;; @@

;; **
;;; Test your function
;; **

;; @@
(cumsum (list 1 2 3 4))
; => [1 3 6 10]
;; @@

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
    ...complete-this...))
;; @@

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
    ...complete-this...))
;; @@

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

;; **
;;; Now let's generalize this function to accept a variable number of arguments. The following code defines a function with an `args` that accepts a variable number of arguments
;; **

;; @@
(let [f (fn [& args]
          (prn args))]
  (f 1)
  (f 2 3 4))
;; @@

;; **
;;; In order to pass a variable length set of arguments to a function, we will need the `apply` function, which can be called `(apply f args)` to call a function with a (variable length) sequence of arguments `args`.  
;; **

;; @@
(let [args [1 2 3]]
  ;; this is equivalent to (+ 1 2 3)
  (apply + args))
;; @@

;; **
;;; Use the `apply` function to define a `comp` function that is agnostic of the number of input arguments
;; **

;; @@
(defn my-comp [f g]
  ...complete-this...)
;; @@

;; **
;;; Test your function
;; **

;; @@
(let [f (my-comp abs *)]
  (f -1 2 -3 4 -5))
; => 120
;; @@

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
   ...complete-this...)
  ([f values]
   ...complete-this...))
;; @@

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
