import sbt._
import sbt.Keys._

import android.Keys._
import android.Dependencies.apklib

object GravityBuild extends android.AutoBuild {
  lazy val main = Project(id = "main", base = file(".")) settings(Seq(
    name := "gravity",
    version := "0.0.1",
    versionCode := Some(1),
    scalaVersion := "2.11.0",
    scalacOptions in Compile ++= Seq("-deprecation", "-Xexperimental"),

    javacOptions ++= Seq("-encoding", "UTF-8", "-source", "1.6", "-target", "1.6"),
    javacOptions in Compile  += "-deprecation",        

    proguardOptions in Android += "-keep class net.whily.android.gravity.** { *; }",
    proguardOptions in Android += "-keep class scala.collection.SeqLike { public java.lang.String toString(); }",

    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "2.2.0" % "test",
      "net.whily" %% "scasci" % "0.0.1-SNAPSHOT",
      "net.whily" %% "scaland" % "0.0.1-SNAPSHOT")
  ): _*)
}
