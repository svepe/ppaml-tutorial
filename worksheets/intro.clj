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
;;; The main goal of probabilistic programming (PP) is to bridge the gap between the theoretical desing and the actual implementation of a probabilistic model. PP enables fast design-verify cycles which allows you to spend significantly more time thinking about the actual problem you want to solve rather than fighting with code.
;;; 
;;; 
;;; <div style="text-align:center">
;;; 	<img src="https://raw.github.com/svepe/radpp/master/resources/intro/pp-venn.png" style="width: 30%"/>
;;; </div>
;;; 
;;; There are numerous probabilistic programming languages ([extensive list](http://probabilistic-programming.org/wiki/Home)) which can be classified by several features:
;;; 
;;; * Supported probabilistic models
;;; * Inference engine
;;; * Dynamic model structure
;;; 
;;; Probabilistic programming intuitively fits to the paradigm of generative modelling which we are going to explore throughout the seminar series.
;;; 
;;; ## Anglican 
;;; 
;;; During the seminars we will use [Anglican](http://www.robots.ox.ac.uk/~fwood/anglican/) <img src="http://www.robots.ox.ac.uk/~fwood/anglican/assets/images/anglican_logo.png" style="width: 2%"/> which is a high-order probobailistic language implemented as a domain specific language (DSL) in Clojure. It supports 10+ inference algorithms, all based on sampling, and a large number of probabilisitic primitives. Additionally, it can cope with models that change over time and have a varying number of unknown variables. 
;;; 
;;; ## Clojure
;;; 
;;; [Clojure](http://clojure.org/>) <img src="http://clojure.org/images/clojure-logo-120b.png" style="width: 3%"/> is a functional language which runs on the virtual machine, essentially a modern LISP. Why should you care about it - it is simply yet another language? Actually, Clojure is a well ballanced mixture between extremely powerful abstract concepts such as metaprogramming and down-to-Earth practical aspects such as compatibility with the entire Java world. My personal opinion is that functional languages have been surprisingly underused by the machine learning community, mainly due to the simplicity of Python. Having Clojure particularly in mind, here are the reasons why I think so.
;;; 
;;; * Write mainly critical code, focus on the problem:
;;;     ```
;;;     // C++                                            ;; Clojure
;;;     for (size_t i = 0; i < array.size(); ++i)         (map #(* 2 %) array)
;;;   	  array[i] = array[i] * 2
;;;     ```
;;; * ML is essentially applying function after function to your data. Why not use a language where functions are first-class citizens?
;;; * Functional programming advocates code with no side affects (fewer bugs!):
;;;   * Immutable data
;;;   * State and identity
;;;   * Parallelisation is often natural
;;; * Clojure is designed for working with data and provides extremely efficient data structures.
;;; * The filter-map-reduce mantra
;;; * ***Metaprogramming***
;;;   * Anglican is a metaprogram!
;;; 
;; **

;; @@
j(ns undisturbed-beach
  (:require [gorilla-plot.core :as plot]))
;; @@

;; @@

;; @@
