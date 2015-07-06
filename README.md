
Gravity
=======

An Android app for N-body simulation.

Features to include:

* 2D and 3D N-body simulation
* Built-in configurations of interesting N-body configurations
  (e.g. figure 8).
* Globular cluster
* Galaxy collision...

For more information about Gravity, please go to
  <https://github.com/whily/gravity>

Development
-----------

The following tools are needed to build Gravity from source:

* JDK version 6/7 from <http://www.java.com> if Java is not available. 
  Note that JDK is preinstalled on Mac OS X and available via package manager
  on many Linux systems. 
* Android SDK r23.0.5.
* Scala (2.11.6)
* sbt (0.13.8)
* [Inkscape](http://inkscape.org) and [ImageMagick](http://www.imagemagick.org)
  to generate icons.

The library dependencies include
[scasci](https://github.com/whily/scasci) and
[scaland](https://github.com/whily/scaland). Please follow the steps
discussed in those libraries on how to use them.

To compile/run the code, follow the steps below in the project directory:

        $ ./genart
        $ sbt android:run        
        
Testing
-------

There are two types of testing can be performed:

* Unit testing. Simply run the following command in shell:
    
        $ sbt test
        
* Android integration testing. Run the following commands in sbt:

        > project tests
        > android:install
        > android:test

