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
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;posterior-inference-exercises/...complete-this...</span>","value":"#'posterior-inference-exercises/...complete-this..."}
;; <=

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
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;posterior-inference-exercises/importance-samples</span>","value":"#'posterior-inference-exercises/importance-samples"}
;; <=

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
;; =>
;;; {"type":"vega","content":{"width":400,"height":247.2187957763672,"padding":{"top":10,"left":55,"bottom":40,"right":10},"data":[{"name":"f46251f0-e5ce-481f-b5bb-208b683d9084","values":[{"x":-10,"y":47.27881673135647},{"x":-9.5,"y":42.53699770814047},{"x":-9.0,"y":29.346490331331086},{"x":-8.5,"y":26.425546991372098},{"x":-8.0,"y":22.514858836787376},{"x":-7.5,"y":21.31074917647966},{"x":-7.0,"y":8.873596627476527},{"x":-6.5,"y":13.016527650182317},{"x":-6.0,"y":8.319581348372257},{"x":-5.5,"y":3.6233636155523854},{"x":-5.0,"y":7.560321261488125},{"x":-4.5,"y":5.581338777218533},{"x":-4.0,"y":2.163987362021305},{"x":-3.5,"y":5.2565855509787855},{"x":-3.0,"y":8.161840056820424},{"x":-2.5,"y":-4.49913557229799},{"x":-2.0,"y":-0.5624338236424902},{"x":-1.5,"y":12.265532463582998},{"x":-1.0,"y":3.566766953541927},{"x":-0.5,"y":-0.004786000440235277},{"x":0.0,"y":3.376241153530865},{"x":0.5,"y":1.2850862961235472},{"x":1.0,"y":3.235880657886796},{"x":1.5,"y":-0.5679115130777104},{"x":2.0,"y":0.40050053049670964},{"x":2.5,"y":0.8333103693192048},{"x":3.0,"y":-1.558732976814164},{"x":3.5,"y":5.011880888325108},{"x":4.0,"y":-11.722648694624116},{"x":4.5,"y":-3.3511036373911147},{"x":5.0,"y":3.1168938998083062},{"x":5.5,"y":0.32377941485886286},{"x":6.0,"y":-16.221576620767383},{"x":6.5,"y":-2.490173840532803},{"x":7.0,"y":-13.067121732884953},{"x":7.5,"y":-11.586374733992347},{"x":8.0,"y":-29.607436307987676},{"x":8.5,"y":-26.08138270221496},{"x":9.0,"y":-27.32740917046169},{"x":9.5,"y":-33.943544863219486}]}],"marks":[{"type":"symbol","from":{"data":"f46251f0-e5ce-481f-b5bb-208b683d9084"},"properties":{"enter":{"x":{"scale":"x","field":"data.x"},"y":{"scale":"y","field":"data.y"},"fill":{"value":"steelblue"},"fillOpacity":{"value":1}},"update":{"shape":"circle","size":{"value":5},"stroke":{"value":"transparent"}},"hover":{"size":{"value":15},"stroke":{"value":"white"}}}}],"scales":[{"name":"x","type":"linear","range":"width","zero":false,"domain":{"data":"f46251f0-e5ce-481f-b5bb-208b683d9084","field":"data.x"}},{"name":"y","type":"linear","range":"height","nice":true,"zero":false,"domain":{"data":"f46251f0-e5ce-481f-b5bb-208b683d9084","field":"data.y"}}],"axes":[{"type":"x","scale":"x"},{"type":"y","scale":"y"}]},"value":"#gorilla_repl.vega.VegaView{:content {:width 400, :height 247.2188, :padding {:top 10, :left 55, :bottom 40, :right 10}, :data [{:name \"f46251f0-e5ce-481f-b5bb-208b683d9084\", :values ({:x -10, :y 47.27881673135647} {:x -9.5, :y 42.53699770814047} {:x -9.0, :y 29.346490331331086} {:x -8.5, :y 26.425546991372098} {:x -8.0, :y 22.514858836787376} {:x -7.5, :y 21.31074917647966} {:x -7.0, :y 8.873596627476527} {:x -6.5, :y 13.016527650182317} {:x -6.0, :y 8.319581348372257} {:x -5.5, :y 3.6233636155523854} {:x -5.0, :y 7.560321261488125} {:x -4.5, :y 5.581338777218533} {:x -4.0, :y 2.163987362021305} {:x -3.5, :y 5.2565855509787855} {:x -3.0, :y 8.161840056820424} {:x -2.5, :y -4.49913557229799} {:x -2.0, :y -0.5624338236424902} {:x -1.5, :y 12.265532463582998} {:x -1.0, :y 3.566766953541927} {:x -0.5, :y -0.004786000440235277} {:x 0.0, :y 3.376241153530865} {:x 0.5, :y 1.2850862961235472} {:x 1.0, :y 3.235880657886796} {:x 1.5, :y -0.5679115130777104} {:x 2.0, :y 0.40050053049670964} {:x 2.5, :y 0.8333103693192048} {:x 3.0, :y -1.558732976814164} {:x 3.5, :y 5.011880888325108} {:x 4.0, :y -11.722648694624116} {:x 4.5, :y -3.3511036373911147} {:x 5.0, :y 3.1168938998083062} {:x 5.5, :y 0.32377941485886286} {:x 6.0, :y -16.221576620767383} {:x 6.5, :y -2.490173840532803} {:x 7.0, :y -13.067121732884953} {:x 7.5, :y -11.586374733992347} {:x 8.0, :y -29.607436307987676} {:x 8.5, :y -26.08138270221496} {:x 9.0, :y -27.32740917046169} {:x 9.5, :y -33.943544863219486})}], :marks [{:type \"symbol\", :from {:data \"f46251f0-e5ce-481f-b5bb-208b683d9084\"}, :properties {:enter {:x {:scale \"x\", :field \"data.x\"}, :y {:scale \"y\", :field \"data.y\"}, :fill {:value \"steelblue\"}, :fillOpacity {:value 1}}, :update {:shape \"circle\", :size {:value 5}, :stroke {:value \"transparent\"}}, :hover {:size {:value 15}, :stroke {:value \"white\"}}}}], :scales [{:name \"x\", :type \"linear\", :range \"width\", :zero false, :domain {:data \"f46251f0-e5ce-481f-b5bb-208b683d9084\", :field \"data.x\"}} {:name \"y\", :type \"linear\", :range \"height\", :nice true, :zero false, :domain {:data \"f46251f0-e5ce-481f-b5bb-208b683d9084\", :field \"data.y\"}}], :axes [{:type \"x\", :scale \"x\"} {:type \"y\", :scale \"y\"}]}}"}
;; <=

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
;; =>
;;; {"type":"html","content":"<span class='clj-double'>3.0</span>","value":"3.0"}
;; <=

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
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;posterior-inference-exercises/fit-curve</span>","value":"#'posterior-inference-exercises/fit-curve"}
;; <=

;; **
;;; What is the influence of the hyperparameters @@m@@, @@w@@, @@y@@ and the number of samples on the final fit? Try to put some priors on those as well. You may also find it interesting to plot the obtained samples - you can vary the line width according to the log-weight of the sample.
;; **
