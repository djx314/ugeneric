import sbt._
import sbt.Keys._

object UGenericSettings {
  val versionSetting = Seq(version := "0.0.1-20200428SNAP1",
    organization := "org.scalax")
}