Worksheets for the RAD seminar on probabilistic probramming.

The worksheets are based on the materials for the [Probabilistic Programming for Advanced Machine Learning 2016 (PPAML16) summerschool](http://ppaml.galois.com/wiki/wiki/SummerSchools/2016/LectureMaterials), which you can find [here](https://bitbucket.org/probprog/ppaml-summer-school-2016).

### Getting started

Make sure you have a recent Java Development Kit installed. To install the JDK on Ubuntu use:
```
sudo apt-get install default-jdk
```
Then download and install Leiningen which is project management system for Clojure. You need
version > 2.x so make sure to remove the Linux package if you have it installed already and run:
```
wget https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein
chmod a+x ./lein
sudo mv ./lein /usr/local/bin/lein
```
Now clone the repository, cd into it and run
```
lein gorilla :port 9876
```
Now open [http://localhost:9876/worksheet.html](http://localhost:9876/worksheet.html) and you
should be in the Gorilla REPL with all of the worksheets available.
