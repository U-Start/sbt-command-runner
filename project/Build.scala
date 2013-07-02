import CodeStyle._
import com.typesafe.sbt.SbtScalariform._
import sbt.Keys._
import sbt._
import sbt.{Path, Credentials}
import scala.Some

object Properties {
  lazy val scalaVer = "2.10.0"
}

object Resolvers {
  lazy val typesafeReleases = "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"
  lazy val scalaToolsRepo   = "sonatype-oss-public" at "https://oss.sonatype.org/content/groups/public/"
}

object BuildSettings {
  lazy val buildSettings = Defaults.defaultSettings ++ Seq (
    organization        := "com.eligotech",
    crossScalaVersions  := Seq("2.9.1", "2.9.2", "2.10.0", "2.10.1"),
    sbtPlugin           := true,
    version             := "0.1-SNAPSHOT",
    scalaVersion        := Properties.scalaVer,
    scalacOptions       := Seq("-unchecked", "-deprecation", "-feature", "-target:jvm-1.7"),
    //doesn't work
    ivyValidate := false
  )
}


object PublishSettings {
  val publishSettings = Seq(
    publishTo <<= version { (v: String) =>
      val nexus = "http://repo.eligotech.com/nexus/"
      if (v.trim.endsWith("SNAPSHOT"))
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases"  at nexus + "content/repositories/releases")
    },
    credentials += Credentials(Path.userHome / ".sbt" / "sonatype.credentials"),
    publishMavenStyle := true
  )
}

object ApplicationBuild extends Build {

  import BuildSettings._
  import Resolvers._

  lazy val sbt_dist = Project(
    "sbt-command-runner",
    file("."),
    settings =  buildSettings ++
      Seq(resolvers ++= Seq(typesafeReleases, scalaToolsRepo))
  )
  .settings( PublishSettings.publishSettings:_* )
  .settings(defaultScalariformSettings: _*)
  .settings(ScalariformKeys.preferences := formattingPreferences)
}
