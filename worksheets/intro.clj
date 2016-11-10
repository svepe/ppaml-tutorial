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
;;; The main goal of probabilistic programming (PP) is to bridge the gap between the theoretical desing and the actual implementation of a probabilistic model. PP enables fast design-verify cycles which allow you to spend significantly more time thinking about the actual problem you want to solve rather than fighting with code.
;;; 
;;; 
;;; 
;;; 
;;; 
;;; 
;;; 
;;; 
;;; 
;;; 
;;; 
;;; 
;; **

;; @@
(ns undisturbed-beach
  (:require [gorilla-plot.core :as plot]))
;; @@
