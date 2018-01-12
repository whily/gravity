
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

* JDK version 6/7/8 from <http://www.java.com> if Java is not available.
  Note that JDK is preinstalled on Mac OS X and available via package manager
  on many Linux systems.
* Android SDK r24.4.1.
* Scala (2.11.11)
* sbt (0.13.16)
* [Inkscape](http://inkscape.org) and [ImageMagick](http://www.imagemagick.org)
  to generate icons.

The library dependencies include
[scasci](https://github.com/whily/scasci) and
[scaland](https://github.com/whily/scaland). Please follow the steps
discussed in those libraries on how to use them.

To compile/run the code, follow the steps below in the project directory:

        $ ./genart
        $ sbt android:run

To build a release version and start it in a connected device:

        $ sbt android:set-release android:run
