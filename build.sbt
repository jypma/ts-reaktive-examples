import sbtprotobuf.{ProtobufPlugin=>PB}

scalaVersion := "2.11.8"

lazy val projectSettings = PB.protobufSettings ++ Seq(
  licenses := Seq(("MIT", url("http://opensource.org/licenses/MIT"))),
  organization := "com.tradeshift",
  version := "0.0.1-SNAPSHOT",
  scalaVersion := "2.11.8",
  publishMavenStyle := true,
  javacOptions ++= Seq("-source", "1.8"),
  javacOptions in (Compile, Keys.compile) ++= Seq("-target", "1.8", "-Xlint", "-Xlint:-processing", "-Xlint:-serial", "-Werror"),
  javacOptions in doc ++= Seq("-Xdoclint:none"),
  EclipseKeys.executionEnvironment := Some(EclipseExecutionEnvironment.JavaSE18),
  EclipseKeys.withSource := true,  
  crossPaths := false,
  autoScalaLibrary := false,
  parallelExecution in Test := false,
  resolvers ++= Seq(
    Resolver.bintrayRepo("readytalk", "maven"),
    Resolver.bintrayRepo("jypma", "maven"),
    Resolver.jcenterRepo),
  dependencyOverrides += "com.google.protobuf" % "protobuf-java" % "2.6.1",
  unmanagedResourceDirectories in Compile <+= (sourceDirectory in PB.protobufConfig),
  PB.runProtoc in PB.protobufConfig := { args =>
    com.github.os72.protocjar.Protoc.runProtoc("-v261" +: args.toArray)
  },
  libraryDependencies ++= {
    val akkaVersion = "2.4.9"
    Seq(
      "com.typesafe.akka" %% "akka-actor" % akkaVersion,
      "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,    
      "io.javaslang" % "javaslang" % "2.0.1",
      "org.slf4j" % "slf4j-api" % "1.7.12",
      "org.slf4j" % "slf4j-log4j12" % "1.7.12" % "test"
    )
  },
  unmanagedResourceDirectories in Compile <+= (sourceDirectory in PB.protobufConfig)
)

lazy val reaktiveSettings = projectSettings ++ Seq(
  libraryDependencies ++= {
  val akkaVersion = "2.4.9"
  val reaktiveVersion = "0.0.10"
  
  Seq(
    "com.tradeshift" % "ts-reaktive-actors" % reaktiveVersion,
    "com.tradeshift" % "ts-reaktive-actors" % reaktiveVersion % PB.protobufConfig.name,
    "com.tradeshift" %% "ts-reaktive-akka" % reaktiveVersion,
    "com.tradeshift" % "ts-reaktive-cassandra" % reaktiveVersion,
    "com.tradeshift" % "ts-reaktive-ssl" % reaktiveVersion,
    "com.tradeshift" %% "ts-reaktive-kamon-akka" % reaktiveVersion,
    "com.tradeshift" %% "ts-reaktive-kamon-akka-http" % reaktiveVersion,
    "com.tradeshift" %% "ts-reaktive-kamon-akka-cluster" % reaktiveVersion,
    "com.tradeshift" %% "ts-reaktive-kamon-log4j" % reaktiveVersion,
    "com.tradeshift" % "ts-reaktive-marshal" % reaktiveVersion,
    "com.tradeshift" % "ts-reaktive-testkit" % reaktiveVersion % "test",
    "com.tradeshift" % "ts-reaktive-testkit-assertj" % reaktiveVersion % "test",
    "com.typesafe.akka" %% "akka-http-testkit" % akkaVersion % "test", // we also write tests using akka's plain testkit
    "com.tradeshift" %% "spray-caching" % "0.1-201603291310",
    "junit" % "junit" % "4.11" % "test",
    "org.assertj" % "assertj-core" % "3.2.0" % "test",
    "org.mockito" % "mockito-core" % "1.10.19" % "test",
    "info.solidsoft.mockito" % "mockito-java8" % "0.3.0" % "test",
    "com.novocode" % "junit-interface" % "0.11" % "test",
    "org.forgerock.cuppa" % "cuppa" % "1.1.0" % "test",
    "org.forgerock.cuppa" % "cuppa-junit" % "1.1.0" % "test",
    "org.apache.cassandra" % "cassandra-all" % "3.0.3" % "test" exclude("ch.qos.logback", "logback-classic"),
    "com.github.tomakehurst" % "wiremock" % "1.58" % "test"
  )
})

lazy val `example-1-akka` = project.settings(projectSettings: _*)

lazy val `example-2-persistence` = project.settings(projectSettings: _*)

lazy val `example-3-reaktive` = project.settings(reaktiveSettings: _*)

lazy val root = (project in file(".")).settings(publish := { }, publishLocal := { }).aggregate(
  `example-1-akka`,
  `example-2-persistence`,
  `example-3-reaktive`
)

// Don't publish the root artifact; only publish sub-projects
publishArtifact := false

publishTo := Some(Resolver.file("unused", file("/tmp/unused")))
