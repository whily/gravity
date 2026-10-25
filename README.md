
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

Gravity is written in Kotlin with
[Kotlin Multiplatform](https://kotlinlang.org/lp/multiplatform/).  The
following tools are needed to build Gravity from source:

* JDK version 21 or later from <http://www.java.com> if Java is not available.
  Note that JDK is preinstalled on Mac OS X and available via package manager
  on many Linux systems.
* Android SDK with platform android-34 and build tools 36.0.0.
* [Inkscape](http://inkscape.org) and [ImageMagick](http://www.imagemagick.org)
  to generate icons.

Gradle is used to build the code, and the included Gradle wrapper
downloads the matching Gradle distribution automatically, so a local
Gradle installation is not needed.

### Generate the icons

In project directory, run following command:

        $ ./genart

### Build the code

The library dependencies include
[kotsci](https://github.com/whily/kotsci) and
[kotland](https://github.com/whily/kotland).  Please follow the steps
discussed in those libraries on how to build them, and publish them
to the local Maven repository, e.g. in each library directory:

        $ gradle publishToMavenLocal

To build the app and install it in a connected device:

        $ ./gradlew :app:installDebug

To build a release version:

        $ ./gradlew :app:assembleRelease
