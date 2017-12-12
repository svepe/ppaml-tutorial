;; gorilla-repl.fileformat = 1

;; **
;;; # The Seven Scientists
;; **

;; @@
(ns mcmc-sampling
  (:require [gorilla-plot.core :as plot])
  (:use [anglican core runtime emit stat]))

(def ...complete-this... nil)
;; @@

;; **
;;; Here's an interesting variation on estimating the mean of a Gaussian from [MacKay 2003, exercise 22.15].
;;; 
;;; Suppose seven scientists all go and perform the same experiment, each collecting a measurement @@x\_i@@ for @@i = 1,\dots,7@@. 
;;; 
;;; These scientists are varyingly good at their job, and while we can assume each scientist would estimate @@x@@ correctly _on average_, some of them may have much more error in their measurements than others.
;;; 
;;; They come back with the following seven observations:
;;; 
;; **

;; @@
(def measurements [-27.020 3.570 8.191 9.898 9.603 9.945 10.056])

(plot/bar-chart (range 1 8) measurements)
;; @@
;; =>
;;; {"type":"vega","content":{"width":400,"height":247.2187957763672,"padding":{"top":10,"left":55,"bottom":40,"right":10},"data":[{"name":"5b132260-c521-4fe8-b779-e7c2768932a0","values":[{"x":1,"y":-27.02},{"x":2,"y":3.57},{"x":3,"y":8.191},{"x":4,"y":9.898},{"x":5,"y":9.603},{"x":6,"y":9.945},{"x":7,"y":10.056}]}],"marks":[{"type":"rect","from":{"data":"5b132260-c521-4fe8-b779-e7c2768932a0"},"properties":{"enter":{"x":{"scale":"x","field":"data.x"},"width":{"scale":"x","band":true,"offset":-1},"y":{"scale":"y","field":"data.y"},"y2":{"scale":"y","value":0}},"update":{"fill":{"value":"steelblue"},"opacity":{"value":1}},"hover":{"fill":{"value":"#FF29D2"}}}}],"scales":[{"name":"x","type":"ordinal","range":"width","domain":{"data":"5b132260-c521-4fe8-b779-e7c2768932a0","field":"data.x"}},{"name":"y","range":"height","nice":true,"domain":{"data":"5b132260-c521-4fe8-b779-e7c2768932a0","field":"data.y"}}],"axes":[{"type":"x","scale":"x"},{"type":"y","scale":"y"}]},"value":"#gorilla_repl.vega.VegaView{:content {:width 400, :height 247.2188, :padding {:top 10, :left 55, :bottom 40, :right 10}, :data [{:name \"5b132260-c521-4fe8-b779-e7c2768932a0\", :values ({:x 1, :y -27.02} {:x 2, :y 3.57} {:x 3, :y 8.191} {:x 4, :y 9.898} {:x 5, :y 9.603} {:x 6, :y 9.945} {:x 7, :y 10.056})}], :marks [{:type \"rect\", :from {:data \"5b132260-c521-4fe8-b779-e7c2768932a0\"}, :properties {:enter {:x {:scale \"x\", :field \"data.x\"}, :width {:scale \"x\", :band true, :offset -1}, :y {:scale \"y\", :field \"data.y\"}, :y2 {:scale \"y\", :value 0}}, :update {:fill {:value \"steelblue\"}, :opacity {:value 1}}, :hover {:fill {:value \"#FF29D2\"}}}}], :scales [{:name \"x\", :type \"ordinal\", :range \"width\", :domain {:data \"5b132260-c521-4fe8-b779-e7c2768932a0\", :field \"data.x\"}} {:name \"y\", :range \"height\", :nice true, :domain {:data \"5b132260-c521-4fe8-b779-e7c2768932a0\", :field \"data.y\"}}], :axes [{:type \"x\", :scale \"x\"} {:type \"y\", :scale \"y\"}]}}"}
;; <=

;; **
;;; Clearly scientist 1 does not know what he is doing (and 2 and 3 are probably a little suspicious too)!
;;; 
;;; To model this situation, we place simple priors on the mean @@\mu@@ of the measurements, and the error standard deviation @@\sigma\_i@@ for each of the @@i@@ scientists.
;;; 
;;; As a starting point, consider placing uninformative priors on these parameters; a suggestion is
;;; $$\begin{align}
;;; \mu &\sim \mathrm{Normal}(0, 50) \\\\
;;; \sigma\_i &\sim \mathrm{Uniform}(0, 25)
;;; \end{align}$$
;;; 
;;; We then suppose each data point is distributed with the same mean, but with a scientist-specific standard deviation:
;;; \begin{align}
;;; y\_i &\sim \mathrm{Normal}(\mu, \sigma_i)
;;; \end{align}
;;; 
;;; We can ask two questions, here:
;;; 
;;; * Given these measurements, what is the posterior distribution of @@\mu@@?
;;; * What is the posterior distribution over the noise level @@\sigma_i@@ for each of the scientists?
;;; 
;;; Write the code to answer those questions using Random Walk Metropolis Hastings.
;; **

;; @@
(defquery seven-scientists [measurements]
  ...complete-this...)

(def mcmc-samples
  ...complete-this...)
;; @@
