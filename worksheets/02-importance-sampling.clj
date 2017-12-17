;; gorilla-repl.fileformat = 1

;; **
;;; # Importance Sampling and Anglican
;;; 
;; **

;; **
;;; ## Basics of Probability Theory
;;; * Conditional probability:
;;; 
;;; $$
;;; \begin{alignat}{3}
;;; p(y, x) &= p(y | x)p(x) &= p(x | y)p(y) \quad & \\\\
;;; \frac{p(y, x)}{p(x)} &= p(y | x) & \\\\
;;; & \hspace{1cm} p(y | x) &= \frac{p(x | y)p(y)}{p(x)} &
;;; \end{alignat}
;;; $$
;;; 
;;; 
;;; 
;;; * Marginalisation:
;;; 
;;; $$
;;; p(y) = \int p(y, x) dx
;;; $$
;;; 
;;; * Expected value:
;;; 
;;; $$
;;; \mathbb{E}_{x \sim p(x)} \[f(x)\] = \int f(x)p(x) dx
;;; $$
;; **

;; **
;;; ## Imprtance Sampling
;;; As we saw last time, the posterior over some latent variables can be expressed as
;;; 
;;; $$ 
;;; p(\theta | \mathcal{D}) = 
;;; \frac
;;; {p(\mathcal{D} | \theta) p(\theta)} 
;;; {p(\mathcal{D})} = 
;;; \frac
;;; {p(\mathcal{D} | \theta) p(\theta)}
;;; {\int p(\mathcal{D} | \theta) p(\theta)d\theta} \propto
;;; p(\mathcal{D} | \theta) p(\theta)
;;; $$
;;; 
;;; If we have to predict the probability of a new value we should marginalise the latent variables out
;;; 
;;; $$
;;; p(x' | \mathcal{D}) = \int p(x', \theta | \mathcal{D})d \theta = \int p(x' | \theta, \mathcal{D}) p(\theta | \mathcal{D}) d \theta = \mathbb{E}_{\theta \sim p(\theta | \mathcal{D})} \[p(x' | \theta, \mathcal{D})\]
;;; $$
;;; 
;;; Predicting new datapoints is only one of the numerous applications of the posterior distribution. It turns out, that we are often interested in an expected value with respect to the posterior distribution, rather than the distribution itself,
;;; 
;;; $$
;;; \mathbb{E}_{\theta \sim p(\theta | \mathcal{D})} \[f(\theta)\] = \int p(\theta | \mathcal{D}) f(\theta) d\theta
;;; $$
;;; 
;;; This integral rarely has a closed form solution, however we can come up with an estimate of its value using importance sampling and Monte Carlo approximation. The key idea is to introduce a proposal distribution and take the expected value with respect to it instead
;;; 
;;; $$
;;; \begin{align}
;;; \int p(\theta | \mathcal{D}) f(\theta) d\theta &= 
;;; \int p(\theta | \mathcal{D}) f(\theta) \frac{q(\theta)}{q(\theta)} d\theta \\\\
;;; &= \int \frac{p(\theta | \mathcal{D})}{q(\theta)} f(\theta) q(\theta) d\theta \\\\
;;; &= \int W(\theta) f(\theta) q(\theta) d\theta = \\\\
;;; &= \mathbb{E}_{\theta \sim q(\theta)} \[W(\theta)f(\theta)\]
;;; \end{align}
;;; $$
;;; 
;;; where 
;;; 
;;; $$
;;; W(\theta) = \frac{p(\theta | \mathcal{D})}{q(\theta)}
;;; $$
;;; 
;;; is defined as an "importance weight". Using a Monte Carlo estimator, we have
;;; 
;;; $$
;;; \mathbb{E}_{\theta \sim q(\theta)} \[W(\theta)f(\theta)\] = \frac{1}{N} \sum_i^{N} f(\theta_i) W(\theta_i) \hspace{0.5cm} \text{ where } \hspace{0.5cm} \theta_i \sim q(\theta)
;;; $$
;;; 
;;; Typically, we can evaluate the posterior only up to a constant
;;; 
;;; $$
;;; p(\theta | \mathcal{D}) \propto p(\mathcal{D} | \theta) p(\theta) = p(\theta, \mathcal{D})
;;; $$
;;; 
;;; and so we define non-normalised "importance weight" as
;;; 
;;; $$
;;; w(\theta) = \frac{p(\theta, \mathcal{D})}{q(\theta)}
;;; $$
;;; 
;;; resulting in
;;; 
;;; $$
;;; W(\theta) = \frac{w(\theta)}{\sum_{i=1}^N w_i(\theta)}
;;; $$
;;; 
;;; and
;;; 
;;; $$
;;; \mathbb{E}_{\theta \sim q(\theta)} \[W(\theta)f(\theta)\] = \frac{1}{\sum_j^N w_j(\theta)} \sum_i^{N} f(\theta_i) w(\theta_i) \hspace{0.5cm} \text{ where } \hspace{0.5cm} \theta_i \sim q(\theta)
;;; $$
;;; 
;;; ---
;;; **MATH EXERCISE** Show that the average of the unnormalized importance weights is an unbiased estimate of the   evidence (i.e. of the normalizing constant for the posterior distribution), that is:
;;; $$\mathbb{E}\left[\frac{1}{N} \sum\_{i=1}^N w(\theta\_i)\right] = p(\mathcal{D})$$
;;; 
;;; ---
;;; 
;;; It is common to use the prior as a proposal distribution
;;; 
;;; $$
;;; q(\theta)=p(\theta) \implies w(\theta) = \frac{p(\theta, \mathcal{D})}{q(\theta)} = \frac{p(\theta, \mathcal{D})}{p(\theta)} = p(\mathcal{D} | \theta)
;;; $$
;;; 
;;; which is simply the likelihood. This method is often referred to as **likelihood weighting**. In order to estimate the expected value of a function with respect to the posterior all you need to do is:
;;; 1. sample you prior - **generate latent variables**
;;; 2. weigh each sample according to the data likelihood
;;; 3. normalise the importance weights
;;; 4. calculate the weighted mean
;;; 
;;; Now let's see how we can use importance sampling in Anglican by studying a simple coin flip model.
;; **

;; **
;;; ## Coin Flip: A Generative View
;;; 
;;; Consider the outcome of a coin flip experiment
;;; 
;;; $$
;;; y \sim \mathrm{Bernoulli}(\theta)
;;; $$
;;; 
;;; where our belief about the bias of the coin is
;;; 
;;; $$
;;; \theta \sim \mathrm{Beta}(5,3) 
;;; $$
;;; 
;;; Having this infromation, we can **simulate** as many coin flip trials as we need. We will be **generating** outcome values and compare them with the actual observed data in order to answer various probabilistic queries. Let's consider the following query 
;;; 
;;; $$
;;; p(\theta>0.7 | y = true) = ?
;;; $$
;;; 
;;; For this we can easily look up and/or compute the ground truth 
;;; 
;;; $$
;;; p(\theta>0.7 | y = true) = 0.448 = 1 - \mathrm{BetaCDF}(0.7|6,3)
;;; $$
;;; 
;; **

;; **
;;; ## Anglican overview
;; **

;; **
;;; The Anglican system consists of three main components:
;;; 
;;; 1. A [language](http://www.robots.ox.ac.uk/~fwood/anglican/language/index.html) for defining probabilistic programs. This language implements a large subset of the language features in Clojure. We refer to an Anglican program as a *query*.
;;; 
;;; 2. An inference backend that implements a number of different [inference methods](http://www.robots.ox.ac.uk/~fwood/anglican/inference/index.html) for Anglican queries. 
;;; 
;;; 3. A [library](https://crossclj.info/ns/anglican/1.0.0/anglican.runtime.html) of functions such as basic math operations and constructors for *distribution* objects, all of which can be used both in Anglican and in Clojure programs.
;;; 
;;; 
;;; ## Distributions 
;;; 
;;; Anglican provides a number of distriubtion primitives to the language, for example `normal`. Calling `(normal mu std)`, with arguments `mu` and `std`, creates a _distribution object_. A distribution object can be used both in Clojure programs and in Anglican programs, but behaves a little differently in each case.
;;; 
;;; In a Clojure program, a distribution object implements two methods
;;; 
;;; 1. `sample*` generates a sample from the distribution. For example `(sample* (normal 0.0 1.0))` draws a standard normal random variate.
;;; 
;;; 2. `observe*` computes the log probability of a sample. For example `(observe* (normal 0.0 1.0) 3.0)` returns the log probability of the value `3.0` under the distribution `(normal 0.0 1.0)`.
;;; 
;;; In an Anglican program, there are two *special forms* that interact with distribution objects
;;; 
;;; 1. `sample` asks the inference backend to generate a sample from the distribution. By default the backend does this by simply calling the Clojure function `sample*`. However in some inference algorithms the backend may resuse a previously sampled value, or sample from a learned proposal.
;;; 
;;; 2. `observe` asks the inference backend to update the log probability of the current execution according the the log probability that can be calculated using the Clojure function `observe*`.
;;; 
;;; Below are some example distribution primitives; these are sufficient to solve the exercises.  A full list of built-in primitives can be found [here](http://www.robots.ox.ac.uk/~fwood/anglican/language/index.html).
;; **

;; @@
(ns importance-sampling
  (:require [gorilla-plot.core :as plot])
  (:use [anglican core runtime emit stat]))
;; @@

;; @@
;; Draw from a normal distribution with mean 1 and standard deviation 2:
(sample* (normal 1 2))

;; Flip a coin, which comes up `true` with probability 0.7, and false with probabilty 0.3:
(sample* (flip 0.7))

;; Sample from a uniform distribution on the open interval (3, 10):
(sample* (uniform-continuous 3 10))

;; Sample from a beta distribution with parameters a=2, b=3:
(sample* (beta 2 3))

;; Sample from a binomial distribution with n=10 and p=0.4:
(sample* (binomial 10 0.4))

;; Sample from a discrete distribution with probabilities [0.3 0.2 0.5] on 0, 1, 2:
(sample* (discrete [0.3 0.2 0.5]))
;; @@

;; @@
;; `repeatedly` can be pretty useful, here.
;; Suppose we want to draw 10 samples from the same normal distribution:
(let [normal-dist (normal 1 2.2)]
  (repeatedly 10 (fn [] (sample* normal-dist))))

;; The # symbol can be used as a shorthand for function definition.
;; The same code as the previous line can also be written like so:
(let [normal-dist (normal 1 2.2)]
  (repeatedly 10 #(sample* normal-dist)))
;; @@

;; @@
;; Using observe: log p(x=3), where x ~ Normal(0, 1):
(observe* (normal 0 1) 3)
;; @@

;; **
;;; ## A First Anglican Query
;; **

;; **
;;; Probabilistic models written in Anglican are called `queries`, and are defined using `defquery`. Here is the model again
;;; 
;;; $$\begin{align}\theta &\sim \mathrm{Beta}(5,3) \\\\
;;; y &\sim \mathrm{Bernoulli}(\theta)\end{align}$$
;;; $$p(\theta>0.7 | y = true) = ?$$
;;; 
;;; with ground truth answer 0.448.
;;; 
;;; The following program defines the statistical model:
;;; 
;; **

;; @@
(defquery one-flip [y]
  (let [theta (sample (beta 5 3))]
    (observe (flip theta) y)
    (> theta 0.7)))
;; @@

;; **
;;; Take a moment to make sure that code block makes sense! `defquery` looks a lot like a function definition, except the contents of the `defquery` are actually Anglican code, which is then _compiled_ into a computable representation of the posterior (think sampler).
;;; 
;;; - The query is named `one-flip`, and it takes a single argument `y`, which is the observed value.
;;; 
;;; - The `let` block defines `theta` as a random sample from the distribution `(beta 5 3)`.
;;; 
;;; - The `observe` statement asserts that we see `y` as data generated from `(flip theta)`.
;;; 
;;; - The final statement defines the return value for the program, which is equal to the `true/false` value of the expression `(> theta 0.7)`.
;;; 
;;; Together, these four lines define our first Anglican program/query/model.
;; **

;; **
;;; ## Importance Sampling with Anglican
;; **

;; @@
(def importance-samples
  (take 1000
	    (doquery :importance one-flip [true])))
(first importance-samples)
;; @@

;; **
;;; If you want to count the number of samples you can use the Clojure built-in function `frequencies`
;; **

;; @@
(frequencies
  (map :result
       importance-samples))
;; @@

;; **
;;; Anglican provides a function known as `collect-results` which summarizes a set of weighted samples
;; **

;; @@
(collect-results 
  (take 10000 importance-samples))
;; @@

;; **
;;; The output of `collect-results` is a map `{value log-weight}`, which can be post-processed using functions in `anglican.stat`. For example we use `empirical-distribution` to normalize log weights into probabilities that sum to 1.0:
;; **

;; @@
(empirical-distribution
  (collect-results 
    importance-samples))
;; @@

;; **
;;; ## A Second Query: Multiple Observes
;;; 
;;; How would we modify this model to return, instead of a one-flip posterior, the posterior distribution given a sequence of flips? That is, we keep the basic model
;;; 
;;; $$\begin{align}\theta &\sim \mathrm{Beta}(5,3) \\\\
;;; y\_i &\sim \mathrm{Bernoulli}(\theta)\end{align}$$
;;; 
;;; and ask 
;;; 
;;; $$p(\theta>0.7 | x\_i)$$
;;; 
;;; for some sequence @@x\_i@@. Now, we let `y-values`, the argument to our query, be a sequence, and we can use `map` (or `loop` and `recur`) to `observe` all different outcomes.
;;; 
;;; Here's one way of writing this:
;; **

;; @@
(defquery many-flips [y-values]
  (let [theta (sample (beta 5 3))
        outcome-dist (flip theta)]
    (map (fn [y] 
           (observe outcome-dist y)) 
         y-values)
    (> theta 0.7)))
;; @@

;; **
;;; We can use `doquery` to estimate the posterior distribution of @@\theta > 0.7@@ given the sequence `[true, false, false, true]`, just as before (the analytical answer is 0.21).
;; **

;; @@
(def data [true, false, false, true])

(def importance-samples 
  (take 1000 (doquery :importance many-flips [data])))


(empirical-distribution
  (collect-results 
    importance-samples))
;; @@

;; **
;;; ## Visualising Results
;; **

;; **
;;; A rudimentary plotting capability comes as part of [Gorilla REPL](http://gorilla-repl.org/). The documentation for the supported plotting functionality is available [here](http://gorilla-repl.org/plotting.html). If you are interested in more advanced plotting, then your best option is to export your data to JSON or CSV and then use matplotlib with Python.
;; **

;; @@
(def data [false, false, false, false])

(defquery many-flips [y-values]
  (let [theta (sample (beta 5 3))
        outcome-dist (flip theta)]
    (map (fn [y] 
           (observe outcome-dist y)) 
         y-values)
    theta)) ; This query returns theta itself

(def importance-samples 
  (take 1000 (doquery :importance many-flips [data])))

(plot/histogram 
  (map :result importance-samples)
  :bins 20 :normalize :probability)
;; @@

;; **
;;; But this plot seems to be the prior on @@\theta@@! We need to take into account the weight of each sample when plotting the histogram. We will generate a new set of samples, where the number of appearances of each sample will be proportional to its weight. We will **resample** our samples in order to get rid of their w
;; **

;; @@
(defn resample [N values weights]
  (let [dist (categorical (map list values weights))]
    (repeatedly N #(sample* dist))))
;; @@

;; @@
(def weightless-samples
	(let [dist (empirical-distribution
  			   	 (collect-results 
        	       importance-samples))]
  		(resample 1000 (keys dist) (vals dist))))

(plot/histogram 
  weightless-samples
  :bins 20 :normalize :probability)
;; @@
