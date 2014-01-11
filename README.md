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
* Android SDK r22.
* Scala (2.10.0)
* sbt (0.12.4)
* [Inkscape](http://inkscape.org) and [ImageMagick](http://www.imagemagick.org)
  to generate icons.

The only library dependency is
[scasci](http://github.com/whily/scasci). Currently, the only way to
use this lib is to generate the jar file (with `sbt package` command)
and put it into the `/lib` directory under the source tree of Gravity.

To compile/run the code, follow the steps below:

1. This step is a work around. It seems that the plugin sbt-android
   assumes that tools like `aapt` and `dx` are located in
   `$ANDROID_HOME/platform-tools`. However at least in Android SDK
   r22, the location is `$ANDROID_HOME/build-tools/18.0.1/`. The
   simplest solution is to copy those binaries (including directory
   **lib** which is related to `dx`) to folder
   `$ANDROID_HOME/platform-tools`.
   
2. In the project directory, run the following command to build the
   app and start it in a connected device:

        $ sbt android:start-device
        
Testing
-------

There are two types of testing can be performed:

* Unit testing. Simply run the following command in shell:
    
        $ sbt test
        
* Android integration testing. Run the following commands in sbt:

        > project tests
        > android:install-device
        > android:test-device  

