;; gorilla-repl.fileformat = 1

;; **
;;; # Importance Sampling and Anglican
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
;;; \alpha &= \frac{q(\theta |\theta')p(\theta'|D)}{q(\theta' | \theta)p(\theta | D)}
;;; \end{align}
;;; $$
;;; 
;;; If @@q@@ is symmetric, @@q(\theta|\theta') = q(\theta'|\theta)@@, then the probability collapses to
;;; 
;;; $$
;;; r = \min\left(1, \frac{p(\theta'|D)}{p(\theta | D)}\right)
;;; $$
;;; 
;;; You can think of it as a noisy gradient ascend algorithm where a new sample is always accepted if it is more probable than the old one and sometimes accepted if it is less probable. In order to avoid the evaluation of the posterior, which we cannot perform due to the evidence @@p(\mathcal{D})@@, we can use Bayes rule 
;;; 
;;; $$
;;; \alpha = \frac{p(\theta'|D)}{p(\theta | D)} = 
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
;;; ## Gibbs Sampling
;; **

;; **
;;; ##Proof of Metropolis Hastings
;; **
