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
;;; w &\sim \mathrm{Normal}(0, \mathbb{I}) \\\\
;;; y &\sim \mathrm{Normal}(c(w, m); 1)
;;; \end{align}
;;; $$
;;; 
;;; Write code to fit the following data:
;; **

;; @@
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
;;; {"type":"vega","content":{"width":400,"height":247.2187957763672,"padding":{"top":10,"left":55,"bottom":40,"right":10},"data":[{"name":"24311164-7212-4bee-8ae0-50c9958b0edd","values":[{"x":-10,"y":43.63227001224327},{"x":-9.5,"y":38.24315095343019},{"x":-9.0,"y":33.74518898308792},{"x":-8.5,"y":30.18389423716372},{"x":-8.0,"y":30.079431832215686},{"x":-7.5,"y":12.170690083756561},{"x":-7.0,"y":15.735112839008126},{"x":-6.5,"y":12.048350542000113},{"x":-6.0,"y":13.507694356545258},{"x":-5.5,"y":10.161769496806262},{"x":-5.0,"y":7.629181288990988},{"x":-4.5,"y":2.1365373461211354},{"x":-4.0,"y":5.814188056660508},{"x":-3.5,"y":2.4961774858056827},{"x":-3.0,"y":5.731043656532197},{"x":-2.5,"y":0.8756706690996231},{"x":-2.0,"y":3.5743695810277787},{"x":-1.5,"y":1.7866159647500537},{"x":-1.0,"y":-2.528819351349773},{"x":-0.5,"y":3.186302211331877},{"x":0.0,"y":-2.852387868309963},{"x":0.5,"y":-5.141859632544098},{"x":1.0,"y":-2.283795814692395},{"x":1.5,"y":0.4575217431600506},{"x":2.0,"y":0.6214964749978876},{"x":2.5,"y":-7.1472206438973975},{"x":3.0,"y":2.568472384907096},{"x":3.5,"y":-0.9519397275571821},{"x":4.0,"y":-3.6501806123577865},{"x":4.5,"y":-1.6055192268555012},{"x":5.0,"y":3.401989859739854},{"x":5.5,"y":-2.8577083268253918},{"x":6.0,"y":-4.872024388460656},{"x":6.5,"y":-7.691410293912369},{"x":7.0,"y":-12.236992562087206},{"x":7.5,"y":-12.833369550142159},{"x":8.0,"y":-6.537522507766621},{"x":8.5,"y":-24.9310600853157},{"x":9.0,"y":-27.77124879877613},{"x":9.5,"y":-34.250618505284145}]}],"marks":[{"type":"symbol","from":{"data":"24311164-7212-4bee-8ae0-50c9958b0edd"},"properties":{"enter":{"x":{"scale":"x","field":"data.x"},"y":{"scale":"y","field":"data.y"},"fill":{"value":"steelblue"},"fillOpacity":{"value":1}},"update":{"shape":"circle","size":{"value":5},"stroke":{"value":"transparent"}},"hover":{"size":{"value":15},"stroke":{"value":"white"}}}}],"scales":[{"name":"x","type":"linear","range":"width","zero":false,"domain":{"data":"24311164-7212-4bee-8ae0-50c9958b0edd","field":"data.x"}},{"name":"y","type":"linear","range":"height","nice":true,"zero":false,"domain":{"data":"24311164-7212-4bee-8ae0-50c9958b0edd","field":"data.y"}}],"axes":[{"type":"x","scale":"x"},{"type":"y","scale":"y"}]},"value":"#gorilla_repl.vega.VegaView{:content {:width 400, :height 247.2188, :padding {:top 10, :left 55, :bottom 40, :right 10}, :data [{:name \"24311164-7212-4bee-8ae0-50c9958b0edd\", :values ({:x -10, :y 43.63227001224327} {:x -9.5, :y 38.24315095343019} {:x -9.0, :y 33.74518898308792} {:x -8.5, :y 30.18389423716372} {:x -8.0, :y 30.079431832215686} {:x -7.5, :y 12.170690083756561} {:x -7.0, :y 15.735112839008126} {:x -6.5, :y 12.048350542000113} {:x -6.0, :y 13.507694356545258} {:x -5.5, :y 10.161769496806262} {:x -5.0, :y 7.629181288990988} {:x -4.5, :y 2.1365373461211354} {:x -4.0, :y 5.814188056660508} {:x -3.5, :y 2.4961774858056827} {:x -3.0, :y 5.731043656532197} {:x -2.5, :y 0.8756706690996231} {:x -2.0, :y 3.5743695810277787} {:x -1.5, :y 1.7866159647500537} {:x -1.0, :y -2.528819351349773} {:x -0.5, :y 3.186302211331877} {:x 0.0, :y -2.852387868309963} {:x 0.5, :y -5.141859632544098} {:x 1.0, :y -2.283795814692395} {:x 1.5, :y 0.4575217431600506} {:x 2.0, :y 0.6214964749978876} {:x 2.5, :y -7.1472206438973975} {:x 3.0, :y 2.568472384907096} {:x 3.5, :y -0.9519397275571821} {:x 4.0, :y -3.6501806123577865} {:x 4.5, :y -1.6055192268555012} {:x 5.0, :y 3.401989859739854} {:x 5.5, :y -2.8577083268253918} {:x 6.0, :y -4.872024388460656} {:x 6.5, :y -7.691410293912369} {:x 7.0, :y -12.236992562087206} {:x 7.5, :y -12.833369550142159} {:x 8.0, :y -6.537522507766621} {:x 8.5, :y -24.9310600853157} {:x 9.0, :y -27.77124879877613} {:x 9.5, :y -34.250618505284145})}], :marks [{:type \"symbol\", :from {:data \"24311164-7212-4bee-8ae0-50c9958b0edd\"}, :properties {:enter {:x {:scale \"x\", :field \"data.x\"}, :y {:scale \"y\", :field \"data.y\"}, :fill {:value \"steelblue\"}, :fillOpacity {:value 1}}, :update {:shape \"circle\", :size {:value 5}, :stroke {:value \"transparent\"}}, :hover {:size {:value 15}, :stroke {:value \"white\"}}}}], :scales [{:name \"x\", :type \"linear\", :range \"width\", :zero false, :domain {:data \"24311164-7212-4bee-8ae0-50c9958b0edd\", :field \"data.x\"}} {:name \"y\", :type \"linear\", :range \"height\", :nice true, :zero false, :domain {:data \"24311164-7212-4bee-8ae0-50c9958b0edd\", :field \"data.y\"}}], :axes [{:type \"x\", :scale \"x\"} {:type \"y\", :scale \"y\"}]}}"}
;; <=

;; **
;;; What is the influence of the hyperparameters for @@m@@, @@w@@ and @@y@@ on the final fit? Try to put some priors on those as well.
;; **

;; **
;;; 
;; **
