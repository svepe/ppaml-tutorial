(defproject exercises "0.1.0-SNAPSHOT"
  :description "Worksheets for the RAD probabilistic programming seminars"
  :url "https://github.com/svepe/radpp"
  :license {:name "The MIT Licence (MIT)"
            :url "https://opensource.org/licenses/MIT"}
  :plugins [[dtolpin/lein-gorilla "0.4.1-SNAPSHOT"]]
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [anglican "1.0.0"]
                 [net.mikera/core.matrix "0.57.0"]
                 [net.mikera/vectorz-clj "0.45.0"]]
                 ;; [org.clojure/data.priority-map "0.0.7"]
                 ;; [net.polyc0l0r/clj-hdf5 "0.2.2-SNAPSHOT"]]
  :target-path "target/%s"
  :jvm-opts ["-Xmx6g" "-Xms4g"]
  :profiles {:uberjar {:aot :all}})
