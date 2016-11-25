;; gorilla-repl.fileformat = 1

;; **
;;; # Markov Chain Monte Carlo Sampling and Anglican
;;; 
;; **

;; **
;;; ## The Metropolis Hastings Algorithm
;;; 
;;; As before, we are interested in estimating the quantity
;;; 
;;; $$
;;; \mathbb{E}_{\theta \sim p(\theta | \mathcal{D})} \[f(\theta)\] = \int p(\theta | \mathcal{D}) f(\theta) d\theta
;;; $$
;;; 
;;; which is the expected value of a function over the posterior distribution of the latent variables given the data. 
;;; 
;; **

;; **
;;; For importance sampling we showed that
;;; 
;;; $$
;;; \mathbb{E}_{\theta \sim p(\theta | \mathcal{D})} \[f(\theta)\] =
;;; \frac{1}{N} \sum_i^{N} f(\theta_i) W(\theta_i) \hspace{0.5cm} \text{ where } \hspace{0.5cm} \theta_i \sim q(\theta)
;;; $$
;;; 
;;; where each sample had an associated weight assigned to it. The Metropolis Hastings algorithm genereates weightless samples instead and approximates the expected value as
;;; 
;;; $$
;;; \mathbb{E}_{\theta \sim p(\theta | \mathcal{D})} \[f(\theta)\] =
;;; \frac{1}{N} \sum_i^{N} f(\theta_i) \hspace{0.5cm} \text{ where } \hspace{0.5cm} \theta_i \sim p(\theta | \mathcal{D})
;;; $$
;;; 
;;; But how can we generate samples from the poserior? The main idea behind the MH algorithm is to use a Markov chain for that purpose. Start from a random state @@\theta@@ and choose a new state @@\theta'@@ by sampling from a **proposal distribution @@q(\theta'|\theta)@@**. The new state is accepted with probability
;;; 
;;; $$
;;; \begin{align}
;;; r &= \min(1, \alpha) \\\\
;;; \\\\
;;; \alpha(\theta'| \theta) &= \frac{q(\theta |\theta')p(\theta'| \mathcal{D})}{q(\theta' | \theta)p(\theta | \mathcal{D})}
;;; \end{align}
;;; $$
;;; 
;;; If @@q@@ is symmetric, @@q(\theta|\theta') = q(\theta'|\theta)@@, then the probability collapses to
;;; 
;;; $$
;;; r = \min\left(1, \frac{p(\theta'| \mathcal{D})}{p(\theta | \mathcal{D})}\right)
;;; $$
;;; 
;;; You can think of it as a noisy gradient ascend algorithm where a new sample is always accepted if it is more probable than the old one and sometimes accepted if it is less probable. In order to avoid the evaluation of the posterior, which we cannot perform due to the evidence @@p(\mathcal{D})@@, we can use Bayes rule 
;;; 
;;; $$
;;; \alpha = \frac{p(\theta'| \mathcal{D})}{p(\theta | \mathcal{D})} = 
;;; \frac
;;; {\frac{p(\mathcal{D}|\theta')p(\theta')}{p(\mathcal{D})}}
;;; {\frac{p(\mathcal{D}|\theta)p(\theta)}{p(\mathcal{D})}} =
;;; \frac
;;; {p(\mathcal{D}|\theta')p(\theta')}
;;; {p(\mathcal{D}|\theta)p(\theta)}
;;; $$
;;; 
;;; in order to derive an expression for the acceptance probability containing only the data likelihood and the prior over the latent variables.
;; **

;; **
;;; ##Proof of Metropolis Hastings
;;; 
;;; We would like @@p(\theta | \mathcal{D})@@ to be the stationary distribution of the Markov Chain which we have defined above and so it should be a solution to the detailed balance equation:
;;; 
;;; $$
;;; p(\theta | \mathcal{D}) p(\theta' | \theta) = p(\theta' | \mathcal{D}) p(\theta | \theta')
;;; $$
;;; 
;;; 
;;; Without loss of generality, consider two states such that 
;;; 
;;; $$
;;; p(\theta | \mathcal{D}) q(\theta' | \theta) > p(\theta' | \mathcal{D}) q(\theta | \theta') \implies \alpha < 1
;;; $$
;;; 
;;; which results in
;;; 
;;; $$
;;; \begin{align}
;;; r(\theta' | \theta) &= \alpha(\theta' | \theta) \\\\
;;; r(\theta | \theta') &= 1
;;; \end{align}
;;; $$
;; **

;; **
;;; For the the transition probability in the LHS of the detailed balance equation we have
;;; 
;;; $$
;;; p(\theta' | \theta) = q(\theta' | \theta)r(\theta' | \theta) = q(\theta' | \theta) \alpha(\theta' | \theta) = 
;;; q(\theta' | \theta) \frac{q(\theta |\theta')p(\theta'| \mathcal{D})}{q(\theta' | \theta)p(\theta | \mathcal{D})} =
;;; q(\theta | \theta') \frac{p(\theta'| \mathcal{D})}{p(\theta | \mathcal{D})}
;;; $$
;;; 
;;; and for the one in the RHS
;;; 
;;; $$
;;; p(\theta | \theta') = q(\theta | \theta')r(\theta | \theta') = q(\theta | \theta')
;;; $$
;;; 
;;; Now comining everything gives
;;; 
;;; $$
;;; \begin{align}
;;; p(\theta | \mathcal{D}) q(\theta | \theta') \frac{p(\theta'| \mathcal{D})}{p(\theta | \mathcal{D})}  &=
;;; p(\theta' | \mathcal{D}) q(\theta | \theta') \\\\
;;; 1 &= 1
;;; \end{align}
;;; $$
;;; 
;;; and so we have shown that @@p(\theta | \mathcal{D})@@ satisfies the detailed balance equation and thus it is the stationary distribution of the Markov chain.
;; **

;; **
;;; ## Sampling with MCMC in Anglican
;; **

;; @@
(ns mcmc-sampling
  (:require [gorilla-plot.core :as plot])
  (:use [anglican core runtime emit stat]))
;; @@

;; @@
(plot/plot
  #(+ (* 0.7 (exp (observe* (normal 20 10) %))) 
      (* 0.3 (exp (observe* (normal -20 10) %))))
  [-100 100])
;; @@

;; @@
(defquery gaussian-mixture []
  (let [mode (sample (discrete [0.7 0.3]))
        theta (sample (normal 0 100))]
    (observe (case mode
               0 (normal 20 10)
               1 (normal -20 10))
             theta)
    theta))
    
;; @@

;; **
;;; ### Practical Hints
;;; * MCMC samples do not have weights associated to them. 
;;; * **burn-in: ** Dismiss the first B samples in order to make sure that the Markov Chain distribution has converged
;;; * **thinning: ** Use only every K sample in order to reduce the dependencies between each sample.
;; **

;; @@
;; Number of samples
(def N 10000)
;; Number burn-in samples
(def B N)
;; @@

;; **
;;; We define some useful plotting functions
;; **

;; @@
(defn mcmc-hist [mcmc-samples]
  (plot/compose
    (plot/histogram 
      (map :result mcmc-samples)
      :bins 100 :normalize :probability)
    (plot/plot
    #(+ (* 0.7 (exp (observe* (normal 20 10) %))) 
        (* 0.3 (exp (observe* (normal -20 10) %))))
    [-100 100])))

(defn mcmc-trace-plot [mcmc-samples]
    (plot/list-plot (map :result mcmc-samples) :joined true :plot-range [:all [-100 100]]))

;; @@

;; **
;;; Now let's sample from our model by using the Random walk Metropolis Hasting (RMH) algorithm:
;; **

;; @@
(def mcmc-samples (take N (drop B (doquery :rmh gaussian-mixture nil))))
(first mcmc-samples)
(mcmc-hist mcmc-samples)
(mcmc-trace-plot mcmc-samples)
;; @@

;; **
;;; ## Sampling from Multimodal distributions
;;; 
;;; Sampling from multimodal distributions is always challenging and most of the times the sampler performance is highly sensitive to the proposal distribution. The RMH implemented in Anglican is a bit smarter than a vanilla RMH, but we can tweak some parameters and see how the proposal distribution affects the accuracy. The proposal distribution that `:rmh` utilises is a @@\mathcal{N}(\theta'; \theta, \sigma)@@. Let's see what the effect of @@\sigma@@ is:
;; **

;; @@
(def sigma 1.0)
(def mcmc-samples (take N (drop B (doquery :rmh gaussian-mixture nil :sigma sigma :alpha 1.0))))
(mcmc-hist mcmc-samples)
(mcmc-trace-plot mcmc-samples)
;; @@

;; @@
(def sigma 500.0)
(def mcmc-samples (take N (drop B (doquery :rmh gaussian-mixture nil :sigma sigma :alpha 1.0))))
(mcmc-hist mcmc-samples)
(mcmc-trace-plot mcmc-samples)
;; @@

;; @@
(def sigma 10.0)
(def mcmc-samples (take N (drop B (doquery :rmh gaussian-mixture nil :sigma sigma :alpha 1.0))))
(mcmc-hist mcmc-samples)
(mcmc-trace-plot mcmc-samples)
;; @@

;; **
;;; ## Minecraft Demo
;; **
