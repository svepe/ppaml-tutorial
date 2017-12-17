The worksheets are based on the materials for the [Probabilistic Programming for Advanced Machine Learning 2016 (PPAML16) summerschool](http://ppaml.galois.com/wiki/wiki/SummerSchools/2016/LectureMaterials), which you can find [here](https://bitbucket.org/probprog/ppaml-summer-school-2016).

### Worksheets

1. [Intro to Clojure](http://viewer.gorilla-repl.org/view.html?source=github&user=svepe&repo=ppaml-tutorialppaml-tutorialppaml-tutorial&path=/worksheets/01-intro-clojure.clj)
    1. [Exercises](http://viewer.gorilla-repl.org/view.html?source=github&user=svepe&repo=ppaml-tutorialppaml-tutorialppaml-tutorial&path=/worksheets/01-intro-clojure-exercises.clj)
    2. [Soulutions](http://viewer.gorilla-repl.org/view.html?source=github&user=svepe&repo=ppaml-tutorialppaml-tutorial&path=/worksheets/01-intro-clojure-solutions.clj)
2. [Importance Sampling](http://viewer.gorilla-repl.org/view.html?source=github&user=svepe&repo=ppaml-tutorial&path=/worksheets/02-importance-sampling.clj)
    1. [Exercises](http://viewer.gorilla-repl.org/view.html?source=github&user=svepe&repo=ppaml-tutorial&path=/worksheets/02-importance-sampling-exercises.clj)
    2. [Solutions](http://viewer.gorilla-repl.org/view.html?source=github&user=svepe&repo=ppaml-tutorialppaml-tutorialppaml-tutorial&path=/worksheets/02-importance-sampling-solutions.clj)

3. [MCMC Sampling](http://viewer.gorilla-repl.org/view.html?source=github&user=svepe&repo=ppaml-tutorialppaml-tutorial&path=/worksheets/03-mcmc-sampling.clj)
    1. [Exercises](http://viewer.gorilla-repl.org/view.html?source=github&user=svepe&repo=ppaml-tutorial&path=/worksheets/03-mcmc-sampling-exercises.clj)
    2. [Solutions](http://viewer.gorilla-repl.org/view.html?source=github&user=svepe&repo=ppaml-tutorial&path=/worksheets/03-mcmc-sampling-solutions.clj)
    
### Installation
If you want to run the worksheets locally and attempt solving the exercises then you should follow these installation instructions.
Make sure you have a recent Java Development Kit installed. To install the JDK on Ubuntu use:
```
sudo apt-get install default-jdk
```
Then download and install Leiningen which is a project management system for Clojure. You need
version > 2.x so make sure to remove the Linux package if you have it installed already and run:
```
wget https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein
chmod a+x ./lein
sudo mv ./lein /usr/local/bin/lein
```
Clone this repository, cd into it and run
```
lein gorilla :port 9876
```
Finally, open [http://localhost:9876/worksheet.html](http://localhost:9876/worksheet.html) and you
should be in the Gorilla REPL with all of the worksheets available.
