;; gorilla-repl.fileformat = 1

;; **
;;; # Intro to probabilistic programming
;;; 
;;; ## Bayesian Inference
;;; 
;;; Probabilistic models are often based on the idea of Bayesian 'inversion':
;;; 
;;; 
;;; $$ 
;;; \color{#409FD6}{p(\theta | \mathcal{D})} = 
;;; \frac
;;; {\color{#81AF3B}{p(\mathcal{D} | \theta)} \color{#B2A728}{p(\theta)}} 
;;; {\color{#E06A13}{p(\mathcal{D}})} = 
;;; \frac
;;; {\color{#81AF3B}{p(\mathcal{D} | \theta)} \color{#B2A728}{p(\theta)}} 
;;; {\color{#E06A13}{\int p(\mathcal{D} | \theta) p(\theta)d\theta}}
;;; $$ 
;;; 
;;; We are interested in the <font color="#409FD6">posterior over the unknown variables given the data</font> which is expressed as the <font color="#81AF3B">likelihood of the data given the unknown variables</font> times the <font color="#B2A728">prior over the unknown variables</font> divided by the <font color="#E06A13">evidence (marginal likelihood) of the data</font>. Often the evidence is an intractable high dimensional integral in the latent space which does not have a closed form solution. 
;;; 
;;; There are several methods to cope with this issue in order to perform Bayesian inference and estimate the posterior:
;;; * **Conjugate priors**
;;; * **Sampling**
;;;   * Importance sampling (IS)
;;;   * Markov Chain Monte Carlo (MCMC)
;;;   * Hamiltonian Monte Carlo (HMC)
;;; * **Approximate inference**
;;;   * Laplace approximation
;;;   * Variational inference
;;;   
;;; Probabilistic modelling decouples the process of designing the model from the infernce method to be used. In other words, as long as we define a valid probabilistic model (essentially the joint distribution of all variables that we are interested in) we can use any inference method suitable for our model. Implementing the model and the corresponding inference algorithm is a task not to be taken lightly as there are multiple sources of potential errors:
;;; 
;;; * Translating maths to code
;;; * Understanding the algorithms back to front
;;; * Avoiding numerical errors
;;; 
;;; Typically, you would spend a few days to sort everything out and you will be extremely happy about your results until you have to implement the next model...
;;; 
;;; ## Probabilistic Programming
;;;   
;;; The main goal of probabilistic programming (PP) is to bridge the gap between the theoretical desing and the actual implementation of a probabilistic model. PP enables fast design-verify cycles which allows you to spend significantly more time thinking about the actual problem you want to solve rather than fighting with code.
;;; 
;;; 
;;; <div style="text-align:center">
;;; 	<img src="https://raw.github.com/svepe/radpp/master/resources/intro/pp-venn.png" style="width: 30%"/>
;;; </div>
;;; 
;;; There are numerous probabilistic programming languages ([extensive list](http://probabilistic-programming.org/wiki/Home)) which can be classified by several features:
;;; 
;;; * Supported probabilistic models
;;; * Inference engine
;;; * Dynamic model structure
;;; 
;;; Probabilistic programming intuitively fits to the paradigm of generative modelling which we are going to explore throughout the seminar series.
;;; 
;;; ## Anglican 
;;; 
;;; During the seminars we will use [Anglican](http://www.robots.ox.ac.uk/~fwood/anglican/) <img src="http://www.robots.ox.ac.uk/~fwood/anglican/assets/images/anglican_logo.png" style="width: 2%"/> which is a high-order probobailistic language implemented as a domain specific language (DSL) in Clojure. It supports 10+ inference algorithms, all based on sampling, and a large number of probabilisitic primitives. Additionally, it can cope with models that change over time and have a varying number of unknown variables. 
;;; 
;;; ## Clojure
;;; 
;;; [Clojure](http://clojure.org/>) <img src="http://clojure.org/images/clojure-logo-120b.png" style="width: 3%"/> is a functional language which runs on the Java virtual machine; in other words it is a modern LISP. Why should you care about it - it is simply yet another language? Actually, Clojure is a well ballanced mixture between extremely powerful abstract concepts such as metaprogramming and down-to-Earth practical aspects such as compatibility with the entire Java world. My personal opinion is that functional languages have been surprisingly underused by the machine learning community, mainly due to the simplicity of Python. Having Clojure particularly in mind, here are the reasons why I think so.
;;; 
;;; * Write mainly critical code, focus on the problem:
;;;     ```
;;;     // C++                                            ;; Clojure
;;;     for (size_t i = 0; i < array.size(); ++i)         (map #(* 2 %) array)
;;;   	  array[i] = array[i] * 2
;;;     ```
;;; * ML is essentially applying function after function to your data. Why not use a language where functions are first-class citizens?
;;; * Functional programming advocates code with no side affects (fewer bugs!):
;;;   * Immutable data
;;;   * State and identity
;;;   * Parallelisation is often natural
;;; * Verbs vs. Nouns
;;; * Clojure is designed for working with data and provides extremely efficient data structures.
;;; * The filter-map-reduce mantra
;;; * ***Metaprogramming***
;;;   * Anglican is a metaprogram!
;;; 
;;;   
;;; This session is mainly an introduction to Clojure, so let's start.
;;; 
;;; ## Clojure Basics
;;; 
;;; The following sections are based on the materials for the [Probabilistic Programming for Advanced Machine Learning 2016 (PPAML16) summerschool](http://ppaml.galois.com/wiki/wiki/SummerSchools/2016/LectureMaterials), which you can find [here](https://bitbucket.org/probprog/ppaml-summer-school-2016). In order to evaluate a code cell simply use `shift+enter` and its output will appear bellow the code.
;;; 
;;; 
;;; ### Namespaces
;;; 
;;; Namespaces are similar to modules in Python and it is a good practice to define a new namespace for each worksheet.
;; **

;; @@
;; Create new `intro-clojure` namespace
(ns intro-clojure)
;; @@

;; **
;;; ### Arithmetics
;; **

;; @@
;; Add two numbers
(+ 1 1)
;; @@

;; @@
;; Subtract: "10 - 3"
(- 10 3)
;; @@

;; @@
;; Multiply, divide
(* 2 5)
(/ 10.0 3.3)
;; @@

;; @@
;; Compound arithmetic expressions: "(10 * (2.1 + 4.3) / 2)"
(/ (* 10 (+ 2.1 4.3)) 2)
;; @@

;; **
;;; When using a new function, it can be helpful to view the Clojure documentation and source code.
;; **

;; @@
;; This returns the documentation for the `+` operator:
(clojure.repl/doc +)
;; @@

;; @@
;; and this returns the source code:
(clojure.repl/source +)
;; @@

;; **
;;; ### Logic operators
;;; 
;;; Comparison operators `<`, `>`, `=`, `<=`, `>=` behave as one would expect, and can be used within an `if` statement.
;; **

;; @@
;; this evaluates to true
(< 4 10)
;; @@

;; @@
;; this evaluates to 1
(if (> 3 2) 1 -1)
;; @@

;; @@
;; this evaluates to 20
(if (<= 3 3) (+ 10 10) 0)
;; @@

;; @@
;; this evaluates to 4
(+ (if (< 4 5) 1 2) 3)
;; @@

;; @@
;; nil is equivalent to a logical false
(if nil true false)

;; all other values are equivalent to a logical true
(if [] true false)
(if "" true false)
(if 0 true false)
;; @@

;; **
;;; ### Scoped variables
;; **

;; @@
;; evaluates to 12
(let [x 10
      y 2]
  (+ x y))
;; @@

;; @@
;; also evaluates to 12!
(let [x 10
      y 2]
  (* x 3)
  (+ x y))
;; @@

;; @@
;; ... but this evaluates to 32
(let [x 10
      y 2]
  (+ (* x 3) y))
;; @@

;; @@
;; ... and so does this
(let [x 10
      y 2
      x (* x 3)]
  (+ x y))
;; @@

;; @@
;; this has a side-effect, printing to the console,
;; which is carried out within the let block
(let [x 10
      y 2]
  (println "x times 3 =" (* x 3))
  (+ x y))
;; @@

;; @@
;; there is also the `do` block, which is like let, but has no bindings
(do 
  (println "10 =" 10)
  (println "1 + 1 ="  (+ 1 1))
  (+ (* 10 3) 2))
;; @@

;; **
;;; ### Functions 
;; **

;; @@
;; define a function which takes x, y as inputs, then returns 2x + y + 3
;; then call that function on values x=5 and y=10, and return the result
(let [my-fn (fn [x y] 
              (+ (* 2 x) y 3))]
  (my-fn 5 10))
;; @@

;; @@
;; this defines a function as a global variable
(defn my-fn [x y]
  (+ (* 2 x) y 3))

(my-fn 5 10)
;; @@

;; @@
;; for short functions, you can use the # macro
(let [f #(+ (* 2 %1) %2 3)]
  (f 5 10))
;; @@

;; @@
;; For functions taking only 1 argument you could use just %
(let [f #(* % %)]
  (f 4))
;; @@

;; **
;;; Let's implement a function that calculates @@n!@@, which in Python would look like
;;; 
;;;         
;;;         def factorial(n):	
;;; 	        '''computes	n * (n - 1) * ... * 1'''	
;;; 	        if n == 1:	
;;; 		        return 1	
;;;             else:
;;;     	        return n * factorial(n - 1)
;;;         
;; **

;; @@
(defn factorial
  "computes n * (n - 1) * ... * 1"
  [n]
  (if (= n 1)
    1
    (* n (factorial (- n 1)))))

(factorial 5)
;; @@

;; **
;;; Let's try something more ambitious:
;; **

;; @@
(factorial 21)
;; @@

;; **
;;; What can we do? We could use `long` instead of `int`.
;; **

;; @@
(defn factorial-long
  "computes n * (n - 1) * ... * 1"
  [n]
  (if (= n 1)
    1N
    (*' n (factorial (- n 1)))))

(factorial-long 21)
;; @@

;; **
;;; How about even more ambitious:
;; **

;; @@
(factorial-long 10000)
;; @@

;; **
;;; Now what? Use a loop?
;; **

;; @@
(defn factorial-loop
  "computes n * (n - 1) * ... * 1"
  [n]
  (loop [result 1
         nvals (range 2 (+ n 1))]
    (if (seq nvals)
      (recur (*' result (first nvals))
             (rest nvals))
      result)))

(factorial-loop 10000)
;; @@

;; **
;;; We can do even better:
;; **

;; @@
(defn factorial-tail
  "computes n * (n - 1) * ... * 1"
  [result nvals]
  (if (seq nvals)
    (recur (*' result (first nvals))
           (rest nvals))
    result))

(defn factorial-wrapper
  [n]
  (factorial-tail 1 (range 2 (+ n 1))))

(factorial-wrapper 10000)

;; @@

;; **
;;; ### Data structures
;;; 
;;; The core data structures in Clojure are
;;; 
;;; - lists: `(1 2 3)`
;;; - vectors: `[1 2 3]`
;;; - hashmaps: `{:a 1 :b 2}`
;;; - sets: `#{1 2 3}`
;;; 
;;; Each of these data structures is a collection, which means that it supports the operations
;;; 
;;; - `(count collection)`: returns the number of elements
;;; - `(conj collection element)`: inserts an element into the collection
;;; - `(seq collection)`: converts the collection into an iterable sequence
;;; 
;;; Functions defined on sequences also apply to collections
;;; 
;;; - `(first sequence)`: returns the first element in a collection
;;; - `(rest sequence)`: returns a sequence with everything but the first element
;;; - `(cons element sequence)`: prepends an element to a sequence
;;; 
;;; #### Path copying 
;;; <div align="middle">
;;; 	<img src="http://2.bp.blogspot.com/_r-NJO1NMiu4/TRA69XdCU8I/AAAAAAAAAnM/Re0VElAeLc4/s1600/ds_2_new.gif"/>
;;; </div>
;; **

;; **
;;; ### Sequences and lists
;;; 
;; **

;; @@
;; Create a list, explicitly
(list 1 2 3)
;; @@

;; @@
;; all lists are sequences
(seq? (list 1 2 3))

;; but not all sequences are lists
(list? (seq [1 2 3]))
;; @@

;; @@
;; first extracts the first element of sequence
(first (list 1 2 3))

;; rest returns the remainder of the sequence
(rest (list 1 2 3))

;; cons prepends an item to a list
(cons 0 (list 1 2 3))
;; @@

;; @@
;; for lists conj prepends an item 
(conj (list 1 2 3) 0)

;; peek extracts the first element
(peek (list 1 2 3))

;; pop removes the first element
(pop (list 1 2 3))
;; @@

;; @@
;; Check the length of a list using `count`
(count (list 1 2 3 4))
;; @@

;; @@
;; Create a list of 5 elements, all of which are the output of "1 + 1"
(repeat 5 (+ 1 1))
;; @@

;; @@
;; Create a list of integers in a certain range
(range 5)
(range 2 8)
;; @@

;; @@
;; Create a list by repeatedly calling a function
(repeatedly 3 (fn [] (+ 10 20)))
;; @@

;; @@
;; Looking up an element in a list requires linear time
(let [numbers (doall (range 0 20000002 2))]
  (time (nth numbers 1000000))
  (time (nth numbers 10000000)))
;; @@

;; **
;;; ### Vectors
;; **

;; @@
;; Create a vector by using square brackets
[1 2 3]
;; @@

;; @@
;; conj appends to the end for vectors
(conj [1 2 3] 4)

;; peek extracts the last element
(peek [1 2 3])

;; pop removes the last element
(pop [1 2 3])
;; @@

;; @@
;; seq casts the vector as a sequence 
(seq [1 2 3])

;; you can use first on vectors
;; (as well as any other collections)
(first [1 2 3])

;; rest returns the remainder of the
;; vector as a sequence
(rest [1 2 3])

;; cons prepends an item, again turning
;; the vector into a sequence
(cons 0 [1 2 3])
;; @@

;; @@
;; Looking up an element in a vector requires constant time
(let [numbers (vec (range 0 20000002 2))]
  (time (nth numbers 1000000))
  (time (nth numbers 10000000)))
;; @@

;; @@
;; vectors can be used as functions
(let [v [1 2 3]]
  (v 2))
;; @@

;; **
;;; ### Hash maps
;; **

;; @@
;; this is a hash map literal
{:a 1 :b 2 :c 3}
;; @@

;; @@
;; entries in hash maps are customarily labeled with keywords
(keyword "a")
(class :a)
;; @@

;; @@
;; however, keys in a hash map can be any clojure object
{:a 1 "b" 2 [3 4] 5}
;; @@

;; @@
;; get can be used to extract an entry from a hash map
(let [m {:a 1 :b 2 :c 3}]
  (get m :a))
;; @@

;; @@
;; a get command can be given a value for missing entries
(let [m {:a 1 :b 2 :c 3}]
  (get m :d 4))
;; @@

;; @@
;; the assoc command inserts an entry into a hash map
(let [m {:a 1 :b 2 :c 3}]
  (assoc m :d 4))
;; @@

;; @@
;; like vectors, hash maps can be used as functions
(let [m {:a 1 :b 2 :c 3}]
  (m :c))
;; @@

;; @@
;; keywords start with :, and can be used as functions as well
(let [m {:a 1 :b 2 :c 3}]
  (:c m))
;; @@

;; **
;;; 
;; **

;; **
;;; ### Filter-Map-Reduce Mantra
;; **

;; @@
;; Return all elements of the array which match the predicate
(filter #(> % 0) 
        [-1 1 -2 2 -3 3])
;; @@

;; @@
;; Apply the function f(x) = x*x to every element of the list `(1 2 3 4)`
(map (fn [x]
       (* x x))
     (list 1 2 3 4))
;; @@

;; @@
;; Here's a different way of writing the above:
(map #(* % %)
     (range 1 5))
;; @@

;; @@
;; Apply the function f(x,y) = x + 2y to the x values `(1 2 3)` and the y values `(10 9 8)`
(map (fn [x y]
       (+ x (* 2 y)))
     [1 2 3]   ; these are values x1, x2, x3
     [10 9 8]) ; these are values y1, y2, y3
;; @@

;; @@
;; Calculate the sum of elements
(reduce + 0.0 [1 2 3])
;; @@

;; @@
;; This does the same thing, but produces a long
(reduce + [1 2 3])
;; @@

;; @@
;; This creates a vector containing only the positive elements
(reduce (fn [y x]
          ;; append x to y if larger than 0
          (if (> x 0)
            (conj y x)
            y))
        ;; initial value for y
        []
        ;; values for x
        [-1 1 -2 2 -3 3])
;; @@

;; @@
;; Sum the squares of all even numbers from 1 to 100
(reduce + 
        (map #(* % %) 
             (filter #(mod % 2) 
                     (range 1 101))))
;; @@
