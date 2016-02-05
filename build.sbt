
name := "anorm-debugger"

organization := "com.jaroop"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "anorm" % "2.3.9",
  "org.scala-lang" % "scala-reflect" % "2.11.7"
)

resolvers ++= Seq("snapshots", "releases").map(Resolver.sonatypeRepo)

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

publishMavenStyle := false

pomExtra := {
  <url>https://github.com/mhzajac/anorm-debugger</url>
  <licenses>
    <license>
      <name>Apache 2</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
    </license>
  </licenses>
  <scm>
    <connection>scm:git:github.com/mhzajac/anorm-debugger</connection>
    <developerConnection>scm:git:git@github.com:mhzajac/anorm-debugger</developerConnection>
    <url>github.com/mhzajac/anorm-debugger</url>
  </scm>
  <developers>
    <developer>
      <id>mz</id>
      <name>Michael Zajac</name>
      <url>https://github.com/mhzajac</url>
    </developer>
  </developers>
}
