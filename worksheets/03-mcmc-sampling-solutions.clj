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
;; =>
;;; {"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"},{"type":"html","content":"<span class='clj-var'>#&#x27;mcmc-sampling/...complete-this...</span>","value":"#'mcmc-sampling/...complete-this..."}],"value":"[nil,#'mcmc-sampling/...complete-this...]"}
;; <=

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
;;; {"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"html","content":"<span class='clj-var'>#&#x27;mcmc-sampling/measurements</span>","value":"#'mcmc-sampling/measurements"},{"type":"vega","content":{"width":400,"height":247.2187957763672,"padding":{"top":10,"left":55,"bottom":40,"right":10},"data":[{"name":"b7085f8a-31d0-4763-bc52-c072220e3ec9","values":[{"x":1,"y":-27.02},{"x":2,"y":3.57},{"x":3,"y":8.191},{"x":4,"y":9.898},{"x":5,"y":9.603},{"x":6,"y":9.945},{"x":7,"y":10.056}]}],"marks":[{"type":"rect","from":{"data":"b7085f8a-31d0-4763-bc52-c072220e3ec9"},"properties":{"enter":{"x":{"scale":"x","field":"data.x"},"width":{"scale":"x","band":true,"offset":-1},"y":{"scale":"y","field":"data.y"},"y2":{"scale":"y","value":0}},"update":{"fill":{"value":"steelblue"},"opacity":{"value":1}},"hover":{"fill":{"value":"#FF29D2"}}}}],"scales":[{"name":"x","type":"ordinal","range":"width","domain":{"data":"b7085f8a-31d0-4763-bc52-c072220e3ec9","field":"data.x"}},{"name":"y","range":"height","nice":true,"domain":{"data":"b7085f8a-31d0-4763-bc52-c072220e3ec9","field":"data.y"}}],"axes":[{"type":"x","scale":"x"},{"type":"y","scale":"y"}]},"value":"#gorilla_repl.vega.VegaView{:content {:width 400, :height 247.2188, :padding {:top 10, :left 55, :bottom 40, :right 10}, :data [{:name \"b7085f8a-31d0-4763-bc52-c072220e3ec9\", :values ({:x 1, :y -27.02} {:x 2, :y 3.57} {:x 3, :y 8.191} {:x 4, :y 9.898} {:x 5, :y 9.603} {:x 6, :y 9.945} {:x 7, :y 10.056})}], :marks [{:type \"rect\", :from {:data \"b7085f8a-31d0-4763-bc52-c072220e3ec9\"}, :properties {:enter {:x {:scale \"x\", :field \"data.x\"}, :width {:scale \"x\", :band true, :offset -1}, :y {:scale \"y\", :field \"data.y\"}, :y2 {:scale \"y\", :value 0}}, :update {:fill {:value \"steelblue\"}, :opacity {:value 1}}, :hover {:fill {:value \"#FF29D2\"}}}}], :scales [{:name \"x\", :type \"ordinal\", :range \"width\", :domain {:data \"b7085f8a-31d0-4763-bc52-c072220e3ec9\", :field \"data.x\"}} {:name \"y\", :range \"height\", :nice true, :domain {:data \"b7085f8a-31d0-4763-bc52-c072220e3ec9\", :field \"data.y\"}}], :axes [{:type \"x\", :scale \"x\"} {:type \"y\", :scale \"y\"}]}}"}],"value":"[#'mcmc-sampling/measurements,#gorilla_repl.vega.VegaView{:content {:width 400, :height 247.2188, :padding {:top 10, :left 55, :bottom 40, :right 10}, :data [{:name \"b7085f8a-31d0-4763-bc52-c072220e3ec9\", :values ({:x 1, :y -27.02} {:x 2, :y 3.57} {:x 3, :y 8.191} {:x 4, :y 9.898} {:x 5, :y 9.603} {:x 6, :y 9.945} {:x 7, :y 10.056})}], :marks [{:type \"rect\", :from {:data \"b7085f8a-31d0-4763-bc52-c072220e3ec9\"}, :properties {:enter {:x {:scale \"x\", :field \"data.x\"}, :width {:scale \"x\", :band true, :offset -1}, :y {:scale \"y\", :field \"data.y\"}, :y2 {:scale \"y\", :value 0}}, :update {:fill {:value \"steelblue\"}, :opacity {:value 1}}, :hover {:fill {:value \"#FF29D2\"}}}}], :scales [{:name \"x\", :type \"ordinal\", :range \"width\", :domain {:data \"b7085f8a-31d0-4763-bc52-c072220e3ec9\", :field \"data.x\"}} {:name \"y\", :range \"height\", :nice true, :domain {:data \"b7085f8a-31d0-4763-bc52-c072220e3ec9\", :field \"data.y\"}}], :axes [{:type \"x\", :scale \"x\"} {:type \"y\", :scale \"y\"}]}}]"}
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
  (let [mu (sample (normal 0 50))
        noise-levels (repeatedly 
                      (count measurements)
                      #(sample (uniform-continuous 0 25)))]
    (map (fn [measurement noise-level]
            (observe (normal mu noise-level) measurement))
         measurements noise-levels)
     {:mu mu
      :noise noise-levels}))


(def N 5000)
(def scientist-samples (->> (doquery :rmh seven-scientists [measurements])
                           (drop N)
                           (take N)
                           (doall)))
;; @@
;; =>
;;; {"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"html","content":"<span class='clj-var'>#&#x27;mcmc-sampling/seven-scientists</span>","value":"#'mcmc-sampling/seven-scientists"},{"type":"html","content":"<span class='clj-var'>#&#x27;mcmc-sampling/N</span>","value":"#'mcmc-sampling/N"}],"value":"[#'mcmc-sampling/seven-scientists,#'mcmc-sampling/N]"},{"type":"html","content":"<span class='clj-var'>#&#x27;mcmc-sampling/scientist-samples</span>","value":"#'mcmc-sampling/scientist-samples"}],"value":"[[#'mcmc-sampling/seven-scientists,#'mcmc-sampling/N],#'mcmc-sampling/scientist-samples]"}
;; <=

;; **
;;; Let's have a look at the resulting samples.
;; **

;; @@
(println "Expected value of measured quantity:" (mean (map #(:mu (:result %)) scientist-samples)))
(plot/histogram (map #(:mu (:result %)) scientist-samples)
                :normalize :probability
                :bins 20)
;; @@
;; ->
;;; Expected value of measured quantity: 8.495091246523698
;;; 
;; <-
;; =>
;;; {"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"},{"type":"vega","content":{"width":400,"height":247.2187957763672,"padding":{"top":10,"left":55,"bottom":40,"right":10},"data":[{"name":"63cc58ec-7369-47d3-9477-456f48df7489","values":[{"x":1.381142796617242,"y":0},{"x":1.9104783697119312,"y":0.0048},{"x":2.4398139428066203,"y":0.0026},{"x":2.9691495159013095,"y":0.0234},{"x":3.4984850889959986,"y":0.0402},{"x":4.027820662090688,"y":0.0434},{"x":4.557156235185378,"y":0.0238},{"x":5.086491808280067,"y":0.0256},{"x":5.615827381374757,"y":0.0102},{"x":6.145162954469447,"y":0.0158},{"x":6.674498527564136,"y":0.0118},{"x":7.203834100658826,"y":0.0196},{"x":7.733169673753515,"y":0.0156},{"x":8.262505246848205,"y":0.0398},{"x":8.791840819942895,"y":0.0694},{"x":9.321176393037584,"y":0.078},{"x":9.850511966132274,"y":0.1996},{"x":10.379847539226963,"y":0.2748},{"x":10.909183112321653,"y":0.0586},{"x":11.438518685416343,"y":0.0246},{"x":11.967854258511032,"y":0.0184},{"x":12.497189831605722,"y":0}]}],"marks":[{"type":"line","from":{"data":"63cc58ec-7369-47d3-9477-456f48df7489"},"properties":{"enter":{"x":{"scale":"x","field":"data.x"},"y":{"scale":"y","field":"data.y"},"interpolate":{"value":"step-before"},"fill":{"value":"steelblue"},"fillOpacity":{"value":0.4},"stroke":{"value":"steelblue"},"strokeWidth":{"value":2},"strokeOpacity":{"value":1}}}}],"scales":[{"name":"x","type":"linear","range":"width","zero":false,"domain":{"data":"63cc58ec-7369-47d3-9477-456f48df7489","field":"data.x"}},{"name":"y","type":"linear","range":"height","nice":true,"zero":false,"domain":{"data":"63cc58ec-7369-47d3-9477-456f48df7489","field":"data.y"}}],"axes":[{"type":"x","scale":"x"},{"type":"y","scale":"y"}]},"value":"#gorilla_repl.vega.VegaView{:content {:width 400, :height 247.2188, :padding {:top 10, :left 55, :bottom 40, :right 10}, :data [{:name \"63cc58ec-7369-47d3-9477-456f48df7489\", :values ({:x 1.381142796617242, :y 0} {:x 1.9104783697119312, :y 0.0048} {:x 2.4398139428066203, :y 0.0026} {:x 2.9691495159013095, :y 0.0234} {:x 3.4984850889959986, :y 0.0402} {:x 4.027820662090688, :y 0.0434} {:x 4.557156235185378, :y 0.0238} {:x 5.086491808280067, :y 0.0256} {:x 5.615827381374757, :y 0.0102} {:x 6.145162954469447, :y 0.0158} {:x 6.674498527564136, :y 0.0118} {:x 7.203834100658826, :y 0.0196} {:x 7.733169673753515, :y 0.0156} {:x 8.262505246848205, :y 0.0398} {:x 8.791840819942895, :y 0.0694} {:x 9.321176393037584, :y 0.078} {:x 9.850511966132274, :y 0.1996} {:x 10.379847539226963, :y 0.2748} {:x 10.909183112321653, :y 0.0586} {:x 11.438518685416343, :y 0.0246} {:x 11.967854258511032, :y 0.0184} {:x 12.497189831605722, :y 0})}], :marks [{:type \"line\", :from {:data \"63cc58ec-7369-47d3-9477-456f48df7489\"}, :properties {:enter {:x {:scale \"x\", :field \"data.x\"}, :y {:scale \"y\", :field \"data.y\"}, :interpolate {:value \"step-before\"}, :fill {:value \"steelblue\"}, :fillOpacity {:value 0.4}, :stroke {:value \"steelblue\"}, :strokeWidth {:value 2}, :strokeOpacity {:value 1}}}}], :scales [{:name \"x\", :type \"linear\", :range \"width\", :zero false, :domain {:data \"63cc58ec-7369-47d3-9477-456f48df7489\", :field \"data.x\"}} {:name \"y\", :type \"linear\", :range \"height\", :nice true, :zero false, :domain {:data \"63cc58ec-7369-47d3-9477-456f48df7489\", :field \"data.y\"}}], :axes [{:type \"x\", :scale \"x\"} {:type \"y\", :scale \"y\"}]}}"}],"value":"[nil,#gorilla_repl.vega.VegaView{:content {:width 400, :height 247.2188, :padding {:top 10, :left 55, :bottom 40, :right 10}, :data [{:name \"63cc58ec-7369-47d3-9477-456f48df7489\", :values ({:x 1.381142796617242, :y 0} {:x 1.9104783697119312, :y 0.0048} {:x 2.4398139428066203, :y 0.0026} {:x 2.9691495159013095, :y 0.0234} {:x 3.4984850889959986, :y 0.0402} {:x 4.027820662090688, :y 0.0434} {:x 4.557156235185378, :y 0.0238} {:x 5.086491808280067, :y 0.0256} {:x 5.615827381374757, :y 0.0102} {:x 6.145162954469447, :y 0.0158} {:x 6.674498527564136, :y 0.0118} {:x 7.203834100658826, :y 0.0196} {:x 7.733169673753515, :y 0.0156} {:x 8.262505246848205, :y 0.0398} {:x 8.791840819942895, :y 0.0694} {:x 9.321176393037584, :y 0.078} {:x 9.850511966132274, :y 0.1996} {:x 10.379847539226963, :y 0.2748} {:x 10.909183112321653, :y 0.0586} {:x 11.438518685416343, :y 0.0246} {:x 11.967854258511032, :y 0.0184} {:x 12.497189831605722, :y 0})}], :marks [{:type \"line\", :from {:data \"63cc58ec-7369-47d3-9477-456f48df7489\"}, :properties {:enter {:x {:scale \"x\", :field \"data.x\"}, :y {:scale \"y\", :field \"data.y\"}, :interpolate {:value \"step-before\"}, :fill {:value \"steelblue\"}, :fillOpacity {:value 0.4}, :stroke {:value \"steelblue\"}, :strokeWidth {:value 2}, :strokeOpacity {:value 1}}}}], :scales [{:name \"x\", :type \"linear\", :range \"width\", :zero false, :domain {:data \"63cc58ec-7369-47d3-9477-456f48df7489\", :field \"data.x\"}} {:name \"y\", :type \"linear\", :range \"height\", :nice true, :zero false, :domain {:data \"63cc58ec-7369-47d3-9477-456f48df7489\", :field \"data.y\"}}], :axes [{:type \"x\", :scale \"x\"} {:type \"y\", :scale \"y\"}]}}]"}
;; <=

;; **
;;; The resulting noise levels are:
;; **

;; @@
(def noise-estimate (mean (map #(:noise (:result %)) scientist-samples)))

(plot/bar-chart (range 1 8) noise-estimate)
;; @@
;; =>
;;; {"type":"list-like","open":"","close":"","separator":"</pre><pre>","items":[{"type":"html","content":"<span class='clj-var'>#&#x27;mcmc-sampling/noise-estimate</span>","value":"#'mcmc-sampling/noise-estimate"},{"type":"vega","content":{"width":400,"height":247.2187957763672,"padding":{"top":10,"left":55,"bottom":40,"right":10},"data":[{"name":"944db7fd-90a3-4a7a-b174-78486a74b1dd","values":[{"x":1,"y":20.233225067029313},{"x":2,"y":10.570846340768272},{"x":3,"y":7.824486254534437},{"x":4,"y":7.089149077051216},{"x":5,"y":8.03668385807816},{"x":6,"y":8.874646405255362},{"x":7,"y":7.593521301906942}]}],"marks":[{"type":"rect","from":{"data":"944db7fd-90a3-4a7a-b174-78486a74b1dd"},"properties":{"enter":{"x":{"scale":"x","field":"data.x"},"width":{"scale":"x","band":true,"offset":-1},"y":{"scale":"y","field":"data.y"},"y2":{"scale":"y","value":0}},"update":{"fill":{"value":"steelblue"},"opacity":{"value":1}},"hover":{"fill":{"value":"#FF29D2"}}}}],"scales":[{"name":"x","type":"ordinal","range":"width","domain":{"data":"944db7fd-90a3-4a7a-b174-78486a74b1dd","field":"data.x"}},{"name":"y","range":"height","nice":true,"domain":{"data":"944db7fd-90a3-4a7a-b174-78486a74b1dd","field":"data.y"}}],"axes":[{"type":"x","scale":"x"},{"type":"y","scale":"y"}]},"value":"#gorilla_repl.vega.VegaView{:content {:width 400, :height 247.2188, :padding {:top 10, :left 55, :bottom 40, :right 10}, :data [{:name \"944db7fd-90a3-4a7a-b174-78486a74b1dd\", :values ({:x 1, :y 20.233225067029313} {:x 2, :y 10.570846340768272} {:x 3, :y 7.824486254534437} {:x 4, :y 7.089149077051216} {:x 5, :y 8.03668385807816} {:x 6, :y 8.874646405255362} {:x 7, :y 7.593521301906942})}], :marks [{:type \"rect\", :from {:data \"944db7fd-90a3-4a7a-b174-78486a74b1dd\"}, :properties {:enter {:x {:scale \"x\", :field \"data.x\"}, :width {:scale \"x\", :band true, :offset -1}, :y {:scale \"y\", :field \"data.y\"}, :y2 {:scale \"y\", :value 0}}, :update {:fill {:value \"steelblue\"}, :opacity {:value 1}}, :hover {:fill {:value \"#FF29D2\"}}}}], :scales [{:name \"x\", :type \"ordinal\", :range \"width\", :domain {:data \"944db7fd-90a3-4a7a-b174-78486a74b1dd\", :field \"data.x\"}} {:name \"y\", :range \"height\", :nice true, :domain {:data \"944db7fd-90a3-4a7a-b174-78486a74b1dd\", :field \"data.y\"}}], :axes [{:type \"x\", :scale \"x\"} {:type \"y\", :scale \"y\"}]}}"}],"value":"[#'mcmc-sampling/noise-estimate,#gorilla_repl.vega.VegaView{:content {:width 400, :height 247.2188, :padding {:top 10, :left 55, :bottom 40, :right 10}, :data [{:name \"944db7fd-90a3-4a7a-b174-78486a74b1dd\", :values ({:x 1, :y 20.233225067029313} {:x 2, :y 10.570846340768272} {:x 3, :y 7.824486254534437} {:x 4, :y 7.089149077051216} {:x 5, :y 8.03668385807816} {:x 6, :y 8.874646405255362} {:x 7, :y 7.593521301906942})}], :marks [{:type \"rect\", :from {:data \"944db7fd-90a3-4a7a-b174-78486a74b1dd\"}, :properties {:enter {:x {:scale \"x\", :field \"data.x\"}, :width {:scale \"x\", :band true, :offset -1}, :y {:scale \"y\", :field \"data.y\"}, :y2 {:scale \"y\", :value 0}}, :update {:fill {:value \"steelblue\"}, :opacity {:value 1}}, :hover {:fill {:value \"#FF29D2\"}}}}], :scales [{:name \"x\", :type \"ordinal\", :range \"width\", :domain {:data \"944db7fd-90a3-4a7a-b174-78486a74b1dd\", :field \"data.x\"}} {:name \"y\", :range \"height\", :nice true, :domain {:data \"944db7fd-90a3-4a7a-b174-78486a74b1dd\", :field \"data.y\"}}], :axes [{:type \"x\", :scale \"x\"} {:type \"y\", :scale \"y\"}]}}]"}
;; <=
