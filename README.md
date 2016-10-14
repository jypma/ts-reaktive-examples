Examples for ts-reaktive
========================

This repository contains examples on building a reactive chat application, using several steps:

1. [example-1-akka](example-1-akka): Create a chat application using plain Akka.
2. [example-2-persistence](example-2-persistence): Add [ts-reaktive](https://github.com/Tradeshift/ts-reaktive/) and persistence so messages are remembered after a restart
3. [example-3-cluster](example-3-cluster): Add clustering features so we can survive hardware failures

### Developing using the command line

The project is built using [sbt](http://www.scala-sbt.org/) as a multi-project build for consistency. 

In order to run example 1, type `sbt "project example-1-akka" run`. 

Command-line arguments can be given by quoting "run" as well, e.g. `sbt "project example-3-reaktive" "run 1"`

For more information, refer to the sbt documentation.

### Developing using Eclipse

Generate eclipse project files by running `sbt eclipse` from the command line. You can then import the example projects without additional eclipse plugins.

### Developing using IntelliJ

IntelliJ should be able to open sbt-based projects directly, if the Scala and SBT plugins are installed.

### Downgrading to Maven

If you really must, you can generate a Maven-style `pom.xml` using `sbt makePom`. SBT will write a `.pom` file to the `target/` directory of each of the projects. This is unsupported, as e.g. protobuf and docker will no longer work.
