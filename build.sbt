name := "angular"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "com.netflix.rxjava" % "rxjava-core" % "0.15.1",
  "com.netflix.rxjava" % "rxjava-scala" % "0.15.1"
)     

play.Project.playScalaSettings
