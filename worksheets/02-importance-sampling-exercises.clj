;; gorilla-repl.fileformat = 1

;; **
;;; # Importance Sampling Exercises
;; **

;; @@
(ns posterior-inference-exercises
 (:require [gorilla-plot.core :as plot]
           [clojure.core.matrix :as m 
            :refer [shape zero-vector identity-matrix diagonal-matrix dot]])
 (:use [anglican core emit runtime stat]))

(m/set-current-implementation :vectorz)

(def ...complete-this... nil)
;; @@

;; **
;;; ## Exercise 1: Inference in a conjugate Gaussian model
;; **

;; **
;;; In our first exercise, suppose we have a very simple generative model for data:
;;; $$\begin{align}
;;; \sigma^2 &= 2 \\\\
;;; \mu &\sim \mathrm{Normal}(1, \sqrt 5) \\\\
;;; y\_i|\mu &\sim \mathrm{Normal}(\mu, \sigma).
;;; \end{align}$$
;;; 
;;; In this generative model, we have a Gaussian distribution as our prior for the latent variable @@\mu@@; the likelihood is then also Gaussian, with mean @@\mu@@.
;; **

;; **
;;; Now suppose we observe two data points, @@y\_1 = 9@@ and @@y\_2 = 8@@.  
;;; 
;;; 
;;; ---
;;; **MATH EXERCISE ** Write down the functional form for the joint distribution @@p(\mu, y\_1, y\_2)@@, as a product of the prior distribution @@p(\mu)@@, the likelihood @@p(y\_1,y\_2|\mu)@@
;;; 
;;; Reminder: the functional form of a Gaussian probability distribution over a random variable @@x@@ with mean @@\mu@@ and standard deviation @@\sigma@@ is 
;;; 
;;; $$
;;; p(x | \mu, \sigma) = 
;;; \frac{1}{\sigma \sqrt{2\pi}}
;;; \exp \left\\{ \frac{1}{2\sigma^2} (x - \mu)^2 \right\\}$$
;;; 
;;; ---
;; **

;; **
;;; ** MATH EXERCISE ** The joint density function above, defined over @@y\_1, y\_2, \mu@@ is an unnormalized density in @@\mu@@. Normalize this density to find the posterior distribution of @@\mu | y\_1, y\_2@@ analytically.
;;; 
;;; Note that @@p(y\_1, y\_2) = \int p(y\_1, y\_2, \mu) \mathrm{d}\mu@@.
;;; 
;;; _HINT_: First, notice that the posterior distribution over @@\mu@@ is also a Gaussian distribution. Then, find the mean and variance of that Gaussian.
;;; 
;;; ---
;;; 
;; **

;; **
;;; Write code to estimate the posterior @@\log p(\mu | y_1, y_2)@@ using importance sampling. Plot the empirical distribution obtained from the samples, as well as the resulting Gaussian.
;; **

;; @@
(def dataset [9 8])

(defquery conjugate-gaussian [dataset]
  ...complete-this...)

(def importance-samples
  ...complete-this...)
;; @@

;; **
;;; # Exercise 2: Generative Curve Fitting
;;; 
;;; 
;; **

;; **
;;; Curve fitting is usually posed as an optimization problem and most of us have fitted at least a line using methods such as least squares. However, in order to *feel* what generative modelling is we will take a non-conventional route. Let's generate as many curves as possible and compare them to the data @@\mathcal{D}=\\{ (x_1, y_1), \ldots (x_N, y_N)\\}@@ in order to estimate the parameters @@w@@. If we assume that we are fitting a polynomial of order @@m@@ then the curve will be:
;;; 
;;; $$
;;; c(x; w, m) = w_m x^m + \ldots + w_1 x + w_0
;;; $$
;;; 
;;; We can define the following generative model:
;;; 
;;; $$
;;; \begin{align}
;;; m &\sim \mathrm{Poisson}(2) \\\\
;;; w|m &\sim \mathrm{Normal}(0, \mathbb{I}) \\\\
;;; y|m,w &\sim \mathrm{Normal}(c(w, m); 1)
;;; \end{align}
;;; $$
;;; 
;;; We will fit a curve to the following data:
;; **

;; @@
;; f(x) = -4x^3 + 3x^2 -2x + 5
(defn f [x]
  (+ (* -4 x x x)
     (* 3 x x)
     (- 2 x)
     5))

(defn noisy-f [x]
  (+ 
    (* 0.01 (f x))
    (sample* (normal 0 4))))

(def xs (range -10 10 0.5))
(def ys (map noisy-f xs))
(plot/list-plot (map vector xs ys) :symbol-size 5)
;; @@

;; **
;;; In order to define a multivariate normal distribution you should use some standard matrix operations defined in the module `clojure.core.matrix`.
;; **

;; @@
;; Create vector [0 0 0]
(zero-vector 3)
;; Get the shape of vector [0 0 0]
(shape (zero-vector 3))

;; Create identity matrix of size 3
(identity-matrix 3)
;; Get the shape of the identity matrix
(shape (identity-matrix 3))

;; Create a diagonal matrix with 1, 2, 3 along the diagonal
(diagonal-matrix [1 2 3])

;; Calculate the dot product of two vectors
(dot [1 1 1] [1 1 1])

;; @@

;; @@
;; Calculates c(x; w, m) where m is encoded in the length of w
(defn curve [w x]
  ...complete-this...)

;; with-primitive-procedures is an Anglican macros allowing you
;; to use non-Anglican functions withing Anglican queries.
(with-primitive-procedures [zero-vector diagonal-matrix dot shape curve]
  (defquery fit-curve [xs ys]
    ...complete-this...))
;; @@

;; **
;;; What is the influence of the hyperparameters @@m@@, @@w@@, @@y@@ and the number of samples on the final fit? Try to put some priors on those as well. You may also find it interesting to plot the obtained samples - you can vary the line width according to the log-weight of the sample.
;; **
