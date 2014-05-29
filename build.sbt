name := "multiplayer"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "io.backchat.hookup" % "hookup_2.10" % "0.2.3"
)     

play.Project.playScalaSettings


