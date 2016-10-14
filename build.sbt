import sbtprotobuf.{ProtobufPlugin=>PB}

scalaVersion := "2.11.8"

val akkaVersion = "2.4.10"

val reaktiveVersion = "0.0.11"

lazy val projectSettings = PB.protobufSettings ++ Seq(
  licenses := Seq(("MIT", url("http://opensource.org/licenses/MIT"))),
  organization := "com.tradeshift",
  version := "0.0.1-SNAPSHOT",
  scalaVersion := "2.11.8",
  javacOptions ++= Seq("-source", "1.8"),
  javacOptions in (Compile, Keys.compile) ++= Seq("-target", "1.8", "-Xlint", "-Xlint:-processing", "-Xlint:-serial", "-Werror"),
  javacOptions in doc ++= Seq("-Xdoclint:none"),
  
  // Eclipse: Generate Java 8 projects
  EclipseKeys.executionEnvironment := Some(EclipseExecutionEnvironment.JavaSE18),
  
  // Eclipse: Download sources for dependencies
  EclipseKeys.withSource := true,  
  
  // Eclipse: Create Java project nature, not Scala
  EclipseKeys.projectFlavor := EclipseProjectFlavor.Java,
  
  // Publish without the scala version in the artifact name
  crossPaths := false,
  
  // Don't add the scala dependency automatically
  autoScalaLibrary := false,
  
  // Run tests sequentially (pending https://github.com/cuppa-framework/cuppa/pull/113)
  parallelExecution in Test := false,
  
  // Find some extra artifacts
  resolvers ++= Seq(
    Resolver.bintrayRepo("readytalk", "maven"),
    Resolver.bintrayRepo("jypma", "maven"),
    Resolver.jcenterRepo),
    
  // Set up protobuf
  dependencyOverrides += "com.google.protobuf" % "protobuf-java" % "2.6.1",
  unmanagedResourceDirectories in Compile <+= (sourceDirectory in PB.protobufConfig),
  PB.runProtoc in PB.protobufConfig := { args =>
    com.github.os72.protocjar.Protoc.runProtoc("-v261" +: args.toArray)
  },
  
  // Shared dependencies for all projects
  libraryDependencies ++= {
    Seq(
      "com.typesafe.akka" %% "akka-actor" % akkaVersion,
      "com.typesafe.akka" %% "akka-http-experimental" % akkaVersion,
      "com.typesafe.akka" %% "akka-http-jackson-experimental" % akkaVersion,
      "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,    
      "io.javaslang" % "javaslang" % "2.0.1",
      "org.slf4j" % "slf4j-api" % "1.7.12",
      "org.slf4j" % "slf4j-log4j12" % "1.7.12" % "test",
      "com.typesafe.akka" %% "akka-http-testkit" % akkaVersion % "test",
      "junit" % "junit" % "4.11" % "test",
      "org.assertj" % "assertj-core" % "3.2.0" % "test",
      "org.mockito" % "mockito-core" % "1.10.19" % "test",
      "info.solidsoft.mockito" % "mockito-java8" % "0.3.0" % "test",
      "com.novocode" % "junit-interface" % "0.11" % "test",
      "org.forgerock.cuppa" % "cuppa" % "1.1.0" % "test",
      "org.forgerock.cuppa" % "cuppa-junit" % "1.1.0" % "test"
    )
  }
)

lazy val persistenceSettings = projectSettings ++ Seq(
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-persistence" % akkaVersion,
    "org.iq80.leveldb"            % "leveldb"          % "0.7",
    "org.fusesource.leveldbjni"   % "leveldbjni-all"   % "1.8",   
    "com.github.dnvriend" %% "akka-persistence-inmemory" % "1.3.5" % "test"
  ),
  
  // levelDB uses JNI, which only works in SBT if forking when running
  fork in run := true
)

lazy val reaktiveSettings = persistenceSettings ++ Seq(
  libraryDependencies ++= Seq(
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
    "com.github.tomakehurst" % "wiremock" % "1.58" % "test"
  )
)

lazy val `example-0-base` = project.settings(projectSettings: _*)

lazy val `example-1-akka` = project.settings(projectSettings: _*)

lazy val `example-2-persistence` = project.settings(reaktiveSettings: _*)

lazy val `example-3-reaktive` = project.settings(reaktiveSettings: _*)

lazy val root = (project in file(".")).settings(publish := { }, publishLocal := { }).aggregate(
  `example-0-base`,
  `example-1-akka`,
  `example-2-persistence`,
  `example-3-reaktive`
)

// Don't publish the root artifact; only publish sub-projects
publishArtifact := false

publishTo := Some(Resolver.file("unused", file("/tmp/unused")))
