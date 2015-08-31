name := "coreos-cluster"

version := "1.0"

scalaVersion := "2.11.6"

lazy val root = (project in file(".")).enablePlugins(DockerPlugin)

libraryDependencies := Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.12",
  "com.typesafe.akka" %% "akka-cluster" % "2.3.12",
  "io.spray" %% "spray-json" % "1.3.2"
)

// Make the docker task depend on the assembly task, which generates a fat JAR file
docker <<= (docker dependsOn assembly)

dockerfile in docker := {
  val artifact = (assemblyOutputPath in assembly).value
  val artifactTargetPath = s"/app/${artifact.name}"
  new Dockerfile {
    from("java:8")
    add(artifact, artifactTargetPath)
    add(file(".") / "docker/run.sh", "/run.sh")
    expose(2550)
    entryPoint("./run.sh", artifactTargetPath)
  }
}

imageNames in docker := Seq(
  ImageName("tlipinski/akka-on-coreos-cluster")
)