;; gorilla-repl.fileformat = 1

;; **
;;; # Bayesian Posterior Inference with Importance Sampling
;; **

;; @@
(ns posterior-inference-exercises
 (:require [gorilla-plot.core :as plot]
           [clojure.core.matrix :as m :refer [zero-vector identity-matrix dot]])
 (:use [anglican core emit runtime stat]))

(m/set-current-implementation :vectorz)

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
;;; {"type":"vega","content":{"width":400,"height":247.2187957763672,"padding":{"top":10,"left":55,"bottom":40,"right":10},"data":[{"name":"073f01c6-4883-476b-897b-4a8133bd665b","values":[{"x":-10,"y":36.11462279496016},{"x":-9.5,"y":30.509071988964784},{"x":-9.0,"y":30.580422813191298},{"x":-8.5,"y":31.966512960899593},{"x":-8.0,"y":24.58530292454383},{"x":-7.5,"y":18.039438672692356},{"x":-7.0,"y":6.572242098288308},{"x":-6.5,"y":10.483529782453148},{"x":-6.0,"y":12.4107461147074},{"x":-5.5,"y":4.08390536389852},{"x":-5.0,"y":18.262305113948376},{"x":-4.5,"y":-0.5591163845785649},{"x":-4.0,"y":-0.6368264437515565},{"x":-3.5,"y":1.0767316294566753},{"x":-3.0,"y":0.13246103629215122},{"x":-2.5,"y":3.014186820241486},{"x":-2.0,"y":2.7497215464570663},{"x":-1.5,"y":-5.8406890731676535},{"x":-1.0,"y":-0.1465328448041148},{"x":-0.5,"y":4.714614562106057},{"x":0.0,"y":0.35986315788454537},{"x":0.5,"y":-0.4359034982303984},{"x":1.0,"y":1.9862236797811024},{"x":1.5,"y":-0.3852213301179071},{"x":2.0,"y":-2.257715533451085},{"x":2.5,"y":-3.5412855517793917},{"x":3.0,"y":0.8743403727692922},{"x":3.5,"y":-4.881653503616805},{"x":4.0,"y":-4.059567735978149},{"x":4.5,"y":0.09729913443233151},{"x":5.0,"y":-2.701053758740776},{"x":5.5,"y":-2.4440616632918033},{"x":6.0,"y":-2.6472047226199615},{"x":6.5,"y":-7.714341605991846},{"x":7.0,"y":-6.1613296407804565},{"x":7.5,"y":-25.132692119670686},{"x":8.0,"y":-16.641401972653536},{"x":8.5,"y":-26.985378250973348},{"x":9.0,"y":-28.384471244121208},{"x":9.5,"y":-33.706987741081974}]}],"marks":[{"type":"symbol","from":{"data":"073f01c6-4883-476b-897b-4a8133bd665b"},"properties":{"enter":{"x":{"scale":"x","field":"data.x"},"y":{"scale":"y","field":"data.y"},"fill":{"value":"steelblue"},"fillOpacity":{"value":1}},"update":{"shape":"circle","size":{"value":5},"stroke":{"value":"transparent"}},"hover":{"size":{"value":15},"stroke":{"value":"white"}}}}],"scales":[{"name":"x","type":"linear","range":"width","zero":false,"domain":{"data":"073f01c6-4883-476b-897b-4a8133bd665b","field":"data.x"}},{"name":"y","type":"linear","range":"height","nice":true,"zero":false,"domain":{"data":"073f01c6-4883-476b-897b-4a8133bd665b","field":"data.y"}}],"axes":[{"type":"x","scale":"x"},{"type":"y","scale":"y"}]},"value":"#gorilla_repl.vega.VegaView{:content {:width 400, :height 247.2188, :padding {:top 10, :left 55, :bottom 40, :right 10}, :data [{:name \"073f01c6-4883-476b-897b-4a8133bd665b\", :values ({:x -10, :y 36.11462279496016} {:x -9.5, :y 30.509071988964784} {:x -9.0, :y 30.580422813191298} {:x -8.5, :y 31.966512960899593} {:x -8.0, :y 24.58530292454383} {:x -7.5, :y 18.039438672692356} {:x -7.0, :y 6.572242098288308} {:x -6.5, :y 10.483529782453148} {:x -6.0, :y 12.4107461147074} {:x -5.5, :y 4.08390536389852} {:x -5.0, :y 18.262305113948376} {:x -4.5, :y -0.5591163845785649} {:x -4.0, :y -0.6368264437515565} {:x -3.5, :y 1.0767316294566753} {:x -3.0, :y 0.13246103629215122} {:x -2.5, :y 3.014186820241486} {:x -2.0, :y 2.7497215464570663} {:x -1.5, :y -5.8406890731676535} {:x -1.0, :y -0.1465328448041148} {:x -0.5, :y 4.714614562106057} {:x 0.0, :y 0.35986315788454537} {:x 0.5, :y -0.4359034982303984} {:x 1.0, :y 1.9862236797811024} {:x 1.5, :y -0.3852213301179071} {:x 2.0, :y -2.257715533451085} {:x 2.5, :y -3.5412855517793917} {:x 3.0, :y 0.8743403727692922} {:x 3.5, :y -4.881653503616805} {:x 4.0, :y -4.059567735978149} {:x 4.5, :y 0.09729913443233151} {:x 5.0, :y -2.701053758740776} {:x 5.5, :y -2.4440616632918033} {:x 6.0, :y -2.6472047226199615} {:x 6.5, :y -7.714341605991846} {:x 7.0, :y -6.1613296407804565} {:x 7.5, :y -25.132692119670686} {:x 8.0, :y -16.641401972653536} {:x 8.5, :y -26.985378250973348} {:x 9.0, :y -28.384471244121208} {:x 9.5, :y -33.706987741081974})}], :marks [{:type \"symbol\", :from {:data \"073f01c6-4883-476b-897b-4a8133bd665b\"}, :properties {:enter {:x {:scale \"x\", :field \"data.x\"}, :y {:scale \"y\", :field \"data.y\"}, :fill {:value \"steelblue\"}, :fillOpacity {:value 1}}, :update {:shape \"circle\", :size {:value 5}, :stroke {:value \"transparent\"}}, :hover {:size {:value 15}, :stroke {:value \"white\"}}}}], :scales [{:name \"x\", :type \"linear\", :range \"width\", :zero false, :domain {:data \"073f01c6-4883-476b-897b-4a8133bd665b\", :field \"data.x\"}} {:name \"y\", :type \"linear\", :range \"height\", :nice true, :zero false, :domain {:data \"073f01c6-4883-476b-897b-4a8133bd665b\", :field \"data.y\"}}], :axes [{:type \"x\", :scale \"x\"} {:type \"y\", :scale \"y\"}]}}"}
;; <=

;; **
;;; What is the influence of the hyperparameters for @@m@@, @@w@@ and @@y@@ on the final fit? Try to put some priors on those as well.
;; **

;; @@
(with-primitive-procedures [zero-vector identity-matrix dot]
  (defquery fit-curve [xs ys]
    (let [m (sample (poisson 2))
          w (sample (mvn (zero-vector (+ m 1)) (identity-matrix (+ m 1))))]
      (map (fn [x y] 
             (let [phi_x (map #(pow %1 %2) 
                              (into [] (repeat (+ 1 m) x))
                              (range 0 (+ 1 m) 1))]
               (observe (normal (dot w phi_x) 1) y)))
           xs
           ys))))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;posterior-inference-exercises/fit-curve</span>","value":"#'posterior-inference-exercises/fit-curve"}
;; <=

;; @@
(zero-vector 2)
(m/identity-matrix 2)

(mvn (m/zero-vector 2) (m/identity-matrix 2))

(sample* (poisson 2))

;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-unkown'>1</span>","value":"1"}
;; <=

;; @@
(def samples (take 1000 (doquery :importance fit-curve [xs ys])))

(empirical-distribution
  (collect-results samples))

;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-unkown'>1</span>","value":"1"},{"type":"html","content":"<span class='clj-double'>1.0</span>","value":"1.0"}],"value":"[1 1.0]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-unkown'>5</span>","value":"5"},{"type":"html","content":"<span class='clj-double'>0.0</span>","value":"0.0"}],"value":"[5 0.0]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-unkown'>4</span>","value":"4"},{"type":"html","content":"<span class='clj-double'>0.0</span>","value":"0.0"}],"value":"[4 0.0]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-unkown'>2</span>","value":"2"},{"type":"html","content":"<span class='clj-double'>1.2893750895781134E-50</span>","value":"1.2893750895781134E-50"}],"value":"[2 1.2893750895781134E-50]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-unkown'>3</span>","value":"3"},{"type":"html","content":"<span class='clj-double'>0.0</span>","value":"0.0"}],"value":"[3 0.0]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-unkown'>0</span>","value":"0"},{"type":"html","content":"<span class='clj-double'>0.0</span>","value":"0.0"}],"value":"[0 0.0]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-unkown'>6</span>","value":"6"},{"type":"html","content":"<span class='clj-double'>0.0</span>","value":"0.0"}],"value":"[6 0.0]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-unkown'>7</span>","value":"7"},{"type":"html","content":"<span class='clj-double'>0.0</span>","value":"0.0"}],"value":"[7 0.0]"}],"value":"{1 1.0, 5 0.0, 4 0.0, 2 1.2893750895781134E-50, 3 0.0, 0 0.0, 6 0.0, 7 0.0}"}
;; <=

;; @@
(into [] (repeat 5 10))
(range 0  1)
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-unkown'>(0 1 2 3 4)</span>","value":"(0 1 2 3 4)"}
;; <=

;; **
;;; 
;; **
