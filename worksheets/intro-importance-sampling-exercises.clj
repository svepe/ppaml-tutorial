;; gorilla-repl.fileformat = 1

;; **
;;; # Bayesian Posterior Inference with Importance Sampling
;; **

;; @@
(ns posterior-inference-exercises
 (:require [gorilla-plot.core :as plot]
           [anglican.stat :as stat])
 (:use [anglican core emit runtime]))

(defn normal-logpdf [x mu sigma]
  (if (> sigma 0)
    (anglican.runtime/observe* (anglican.runtime/normal mu sigma) x)
    (Math/log 0.0)))

(defn randn [mu sigma]
  (anglican.runtime/sample* (anglican.runtime/normal mu sigma)))

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
;;; In this generative model, we have a Gaussian distribution as our prior for the latent variable @@\mu@@; the likelihood &mdash; that is, the probability of the data given the latent variables &mdash; is then also Gaussian, with mean @@\mu@@.
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
;;; 
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
;;; 
;; **

;; **
;;; As we have discussed a common choice for the proposal is just the prior distribution, 
;;; $$q(\mu) = \mathrm{Normal}(\mu | 1, \sqrt{5}).$$
;; **

;; **
;;; 
;; **

;; **
;;; We provide two helper functions for working with Gaussians: `(randn mu sigma)` will sample a random variable, and `(normal-logpdf x mu sigma)` will compute @@p(x  | \mu, \sigma)@@.
;; **

;; @@
(randn 0 1)

(normal-logpdf 1.5 1 2)
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-double'>-1.643335713764618</span>","value":"-1.643335713764618"}
;; <=

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
;;; Write code to fit the following data:
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
     (randn 0 4)))

(def xs (range -10 10 0.5))
(def ys (map noisy-f xs))
(plot/list-plot (map vector xs ys) :symbol-size 5)
;; @@
;; =>
;;; {"type":"vega","content":{"width":400,"height":247.2187957763672,"padding":{"top":10,"left":55,"bottom":40,"right":10},"data":[{"name":"b4ccf74a-08ed-4199-9fb8-e52774f60d8c","values":[{"x":-10,"y":39.98842034496512},{"x":-9.5,"y":36.736918656558586},{"x":-9.0,"y":31.838556991283202},{"x":-8.5,"y":23.380572508131923},{"x":-8.0,"y":23.79824466729415},{"x":-7.5,"y":12.26072217090184},{"x":-7.0,"y":13.662824397891944},{"x":-6.5,"y":13.174818673715164},{"x":-6.0,"y":9.572026860442882},{"x":-5.5,"y":6.753539025059634},{"x":-5.0,"y":3.4206786716397093},{"x":-4.5,"y":0.6078885755103407},{"x":-4.0,"y":2.037440483656359},{"x":-3.5,"y":0.4843288830651189},{"x":-3.0,"y":5.228783164939301},{"x":-2.5,"y":-0.16010779915239803},{"x":-2.0,"y":-4.5145385380014895},{"x":-1.5,"y":3.4248063320886333},{"x":-1.0,"y":-2.1734418472803414},{"x":-0.5,"y":-5.07190476273307},{"x":0.0,"y":-5.849076278825154},{"x":0.5,"y":-4.203927434677312},{"x":1.0,"y":-6.067207219702369},{"x":1.5,"y":-4.746082061665871},{"x":2.0,"y":-2.6290684486937104},{"x":2.5,"y":-2.866698851214611},{"x":3.0,"y":-3.1159431155623705},{"x":3.5,"y":-2.9329416873464553},{"x":4.0,"y":-9.791495716064645},{"x":4.5,"y":-6.220260032949964},{"x":5.0,"y":1.449732514410309},{"x":5.5,"y":-5.890198446730835},{"x":6.0,"y":-6.871957007148872},{"x":6.5,"y":-7.421149582528244},{"x":7.0,"y":-12.053808471132928},{"x":7.5,"y":-9.190620372448903},{"x":8.0,"y":-15.430823840550605},{"x":8.5,"y":-18.45964839394457},{"x":9.0,"y":-26.20917234046666},{"x":9.5,"y":-28.144609332312182}]}],"marks":[{"type":"symbol","from":{"data":"b4ccf74a-08ed-4199-9fb8-e52774f60d8c"},"properties":{"enter":{"x":{"scale":"x","field":"data.x"},"y":{"scale":"y","field":"data.y"},"fill":{"value":"steelblue"},"fillOpacity":{"value":1}},"update":{"shape":"circle","size":{"value":5},"stroke":{"value":"transparent"}},"hover":{"size":{"value":15},"stroke":{"value":"white"}}}}],"scales":[{"name":"x","type":"linear","range":"width","zero":false,"domain":{"data":"b4ccf74a-08ed-4199-9fb8-e52774f60d8c","field":"data.x"}},{"name":"y","type":"linear","range":"height","nice":true,"zero":false,"domain":{"data":"b4ccf74a-08ed-4199-9fb8-e52774f60d8c","field":"data.y"}}],"axes":[{"type":"x","scale":"x"},{"type":"y","scale":"y"}]},"value":"#gorilla_repl.vega.VegaView{:content {:width 400, :height 247.2188, :padding {:top 10, :left 55, :bottom 40, :right 10}, :data [{:name \"b4ccf74a-08ed-4199-9fb8-e52774f60d8c\", :values ({:x -10, :y 39.98842034496512} {:x -9.5, :y 36.736918656558586} {:x -9.0, :y 31.838556991283202} {:x -8.5, :y 23.380572508131923} {:x -8.0, :y 23.79824466729415} {:x -7.5, :y 12.26072217090184} {:x -7.0, :y 13.662824397891944} {:x -6.5, :y 13.174818673715164} {:x -6.0, :y 9.572026860442882} {:x -5.5, :y 6.753539025059634} {:x -5.0, :y 3.4206786716397093} {:x -4.5, :y 0.6078885755103407} {:x -4.0, :y 2.037440483656359} {:x -3.5, :y 0.4843288830651189} {:x -3.0, :y 5.228783164939301} {:x -2.5, :y -0.16010779915239803} {:x -2.0, :y -4.5145385380014895} {:x -1.5, :y 3.4248063320886333} {:x -1.0, :y -2.1734418472803414} {:x -0.5, :y -5.07190476273307} {:x 0.0, :y -5.849076278825154} {:x 0.5, :y -4.203927434677312} {:x 1.0, :y -6.067207219702369} {:x 1.5, :y -4.746082061665871} {:x 2.0, :y -2.6290684486937104} {:x 2.5, :y -2.866698851214611} {:x 3.0, :y -3.1159431155623705} {:x 3.5, :y -2.9329416873464553} {:x 4.0, :y -9.791495716064645} {:x 4.5, :y -6.220260032949964} {:x 5.0, :y 1.449732514410309} {:x 5.5, :y -5.890198446730835} {:x 6.0, :y -6.871957007148872} {:x 6.5, :y -7.421149582528244} {:x 7.0, :y -12.053808471132928} {:x 7.5, :y -9.190620372448903} {:x 8.0, :y -15.430823840550605} {:x 8.5, :y -18.45964839394457} {:x 9.0, :y -26.20917234046666} {:x 9.5, :y -28.144609332312182})}], :marks [{:type \"symbol\", :from {:data \"b4ccf74a-08ed-4199-9fb8-e52774f60d8c\"}, :properties {:enter {:x {:scale \"x\", :field \"data.x\"}, :y {:scale \"y\", :field \"data.y\"}, :fill {:value \"steelblue\"}, :fillOpacity {:value 1}}, :update {:shape \"circle\", :size {:value 5}, :stroke {:value \"transparent\"}}, :hover {:size {:value 15}, :stroke {:value \"white\"}}}}], :scales [{:name \"x\", :type \"linear\", :range \"width\", :zero false, :domain {:data \"b4ccf74a-08ed-4199-9fb8-e52774f60d8c\", :field \"data.x\"}} {:name \"y\", :type \"linear\", :range \"height\", :nice true, :zero false, :domain {:data \"b4ccf74a-08ed-4199-9fb8-e52774f60d8c\", :field \"data.y\"}}], :axes [{:type \"x\", :scale \"x\"} {:type \"y\", :scale \"y\"}]}}"}
;; <=

;; **
;;; What is the influence of the hyperparameters for @@m@@, @@w@@ and @@y@@ on the final fit? Try to put some priors on those as well.
;; **

;; **
;;; 
;; **
