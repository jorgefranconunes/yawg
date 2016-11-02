= Yet Another Wiki Generator
:author: Jorge Nunes





== Introduction

This document describes the steps for seting up the development
environment for the Yawg project.

* Requirements

* Fetch source tree.

* Edit/compile/run cycle

* Running unit tests

* Running integration tests

* Code quality reports

* Creating a new release





== Requirements

The following packages are required for developing in the Yawg
project:

* Oracle JDK 8 -
  http://www.oracle.com/technetwork/java/javase/downloads/index.html

* Maven 3 - http://maven.apache.org/download.cgi

You will need to have the above packages installed in your
workstation.

The instructions in this document also assume a Linux environment, or
Windows with Cygwin installed.





== Fetch source tree

Do the following

----
git clone git@github.com:jorgefranconunes/yawg.git
----

After executing the above (and after a few moments) a directory named
"yawg" will have been created in your current working directory. That
"yawg" directory contains the project tree and will be your working
area.





== Edit/compile/run cycle

To perform a full build, just run "mvn" from the top of the project
tree:

----
mvn -f ./modules/pom.xml
----

TBD





== Running unit tests

To run all unit tests:

----
mvn -f ./modules/pom.xml
----

To run a single JUnit test suite, cd to the module directory and run:

----
mvn -Dtest=MyClassTest test
----

Of course, change the "MyClassTest" above with whatever one of your
test suites you want to run.

And to run a single test:

----
mvn -Dtest=MyClassTest#myTestMethod test
----





== Running integration tests

TBD...





== Code quality reports

To generate static analysis and test code coverage reports do the
following:

----
REPORT_DIR=$HOME/whatever
mvn -f ./modules/pom.xml -Dyawg.mavensite=$REPORT_DIR clean package site
----

This will create under $REPORT_DIR a set of HTML pages with the
results of static analysis and test code coverage. The entry page will
be at $REPORT_DIR/index.html





== Creating a new release

To generate the tarball for an engineering build do the following:

----
./devtools/bin/build-create-bundle
----

The above will perform a full build and create a tarball named
'yawg-x.y.z-yyyyMMddhhmm.tar.bz2' at the top of the working area.

To generate an official release see the instructions on the project
site at 'doc/content/developers/CreatingRelease.adoc'.

