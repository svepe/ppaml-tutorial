;; gorilla-repl.fileformat = 1

;; **
;;; # Bayesian Posterior Inference with Importance Sampling
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
;;; As we have discussed a common choice for the proposal is just the prior distribution, 
;;; $$q(\mu) = \mathrm{Normal}(\mu | 1, \sqrt{5}).$$
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
;;; c(w, m) = w_m x^m + \ldots + w_1 x + w_0
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
;;; {"type":"vega","content":{"width":400,"height":247.2187957763672,"padding":{"top":10,"left":55,"bottom":40,"right":10},"data":[{"name":"cfbe3058-efb7-4925-9aa6-ea30c91b0104","values":[{"x":-10,"y":45.00705996478687},{"x":-9.5,"y":32.14391263052633},{"x":-9.0,"y":29.650095316088354},{"x":-8.5,"y":27.20627970497423},{"x":-8.0,"y":31.182235358482746},{"x":-7.5,"y":18.91328516509535},{"x":-7.0,"y":18.57100866067705},{"x":-6.5,"y":15.570081845519717},{"x":-6.0,"y":12.15983558794768},{"x":-5.5,"y":7.424664411287681},{"x":-5.0,"y":10.96988244225868},{"x":-4.5,"y":3.2611489937239053},{"x":-4.0,"y":5.166907059995728},{"x":-3.5,"y":0.32947824806351544},{"x":-3.0,"y":-2.287320059223676},{"x":-2.5,"y":4.782667391336701},{"x":-2.0,"y":-2.7811946441280684},{"x":-1.5,"y":-1.2096226671206765},{"x":-1.0,"y":-7.176902987142497},{"x":-0.5,"y":0.28332770086253634},{"x":0.0,"y":-0.3502628538384935},{"x":0.5,"y":1.897719058569579},{"x":1.0,"y":-5.595883712724336},{"x":1.5,"y":8.385219159744507},{"x":2.0,"y":4.514695279529725},{"x":2.5,"y":5.573028169875636},{"x":3.0,"y":0.3791839932393657},{"x":3.5,"y":-1.5071285304085196},{"x":4.0,"y":-2.493963256496876},{"x":4.5,"y":-1.4015283441798616},{"x":5.0,"y":-6.411483346213268},{"x":5.5,"y":-9.818265067591671},{"x":6.0,"y":-10.107362135435768},{"x":6.5,"y":-14.233652871358483},{"x":7.0,"y":-14.166464110555667},{"x":7.5,"y":-17.179160416963292},{"x":8.0,"y":-20.645707128466274},{"x":8.5,"y":-23.569167967473952},{"x":9.0,"y":-25.11511450230532},{"x":9.5,"y":-34.709342286070765}]}],"marks":[{"type":"symbol","from":{"data":"cfbe3058-efb7-4925-9aa6-ea30c91b0104"},"properties":{"enter":{"x":{"scale":"x","field":"data.x"},"y":{"scale":"y","field":"data.y"},"fill":{"value":"steelblue"},"fillOpacity":{"value":1}},"update":{"shape":"circle","size":{"value":5},"stroke":{"value":"transparent"}},"hover":{"size":{"value":15},"stroke":{"value":"white"}}}}],"scales":[{"name":"x","type":"linear","range":"width","zero":false,"domain":{"data":"cfbe3058-efb7-4925-9aa6-ea30c91b0104","field":"data.x"}},{"name":"y","type":"linear","range":"height","nice":true,"zero":false,"domain":{"data":"cfbe3058-efb7-4925-9aa6-ea30c91b0104","field":"data.y"}}],"axes":[{"type":"x","scale":"x"},{"type":"y","scale":"y"}]},"value":"#gorilla_repl.vega.VegaView{:content {:width 400, :height 247.2188, :padding {:top 10, :left 55, :bottom 40, :right 10}, :data [{:name \"cfbe3058-efb7-4925-9aa6-ea30c91b0104\", :values ({:x -10, :y 45.00705996478687} {:x -9.5, :y 32.14391263052633} {:x -9.0, :y 29.650095316088354} {:x -8.5, :y 27.20627970497423} {:x -8.0, :y 31.182235358482746} {:x -7.5, :y 18.91328516509535} {:x -7.0, :y 18.57100866067705} {:x -6.5, :y 15.570081845519717} {:x -6.0, :y 12.15983558794768} {:x -5.5, :y 7.424664411287681} {:x -5.0, :y 10.96988244225868} {:x -4.5, :y 3.2611489937239053} {:x -4.0, :y 5.166907059995728} {:x -3.5, :y 0.32947824806351544} {:x -3.0, :y -2.287320059223676} {:x -2.5, :y 4.782667391336701} {:x -2.0, :y -2.7811946441280684} {:x -1.5, :y -1.2096226671206765} {:x -1.0, :y -7.176902987142497} {:x -0.5, :y 0.28332770086253634} {:x 0.0, :y -0.3502628538384935} {:x 0.5, :y 1.897719058569579} {:x 1.0, :y -5.595883712724336} {:x 1.5, :y 8.385219159744507} {:x 2.0, :y 4.514695279529725} {:x 2.5, :y 5.573028169875636} {:x 3.0, :y 0.3791839932393657} {:x 3.5, :y -1.5071285304085196} {:x 4.0, :y -2.493963256496876} {:x 4.5, :y -1.4015283441798616} {:x 5.0, :y -6.411483346213268} {:x 5.5, :y -9.818265067591671} {:x 6.0, :y -10.107362135435768} {:x 6.5, :y -14.233652871358483} {:x 7.0, :y -14.166464110555667} {:x 7.5, :y -17.179160416963292} {:x 8.0, :y -20.645707128466274} {:x 8.5, :y -23.569167967473952} {:x 9.0, :y -25.11511450230532} {:x 9.5, :y -34.709342286070765})}], :marks [{:type \"symbol\", :from {:data \"cfbe3058-efb7-4925-9aa6-ea30c91b0104\"}, :properties {:enter {:x {:scale \"x\", :field \"data.x\"}, :y {:scale \"y\", :field \"data.y\"}, :fill {:value \"steelblue\"}, :fillOpacity {:value 1}}, :update {:shape \"circle\", :size {:value 5}, :stroke {:value \"transparent\"}}, :hover {:size {:value 15}, :stroke {:value \"white\"}}}}], :scales [{:name \"x\", :type \"linear\", :range \"width\", :zero false, :domain {:data \"cfbe3058-efb7-4925-9aa6-ea30c91b0104\", :field \"data.x\"}} {:name \"y\", :type \"linear\", :range \"height\", :nice true, :zero false, :domain {:data \"cfbe3058-efb7-4925-9aa6-ea30c91b0104\", :field \"data.y\"}}], :axes [{:type \"x\", :scale \"x\"} {:type \"y\", :scale \"y\"}]}}"}
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
;; Calculates c(w, m)
(defn curve [x w]
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
;;; What is the influence of the hyperparameters for @@m@@, @@w@@ and @@y@@ on the final fit? Try to put some priors on those as well.
;; **
