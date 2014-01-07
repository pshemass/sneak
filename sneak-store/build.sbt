name := "sneak-store"

version := "0.1"

scalaVersion := "2.10.3"

libraryDependencies ++= Seq(
  "com.typesafe.akka"        %% "akka-actor"            % "2.2.1",
  "com.typesafe.akka"        %% "akka-slf4j"            % "2.2.1",
  "com.typesafe.akka"        %% "akka-testkit"          % "2.2.1" % "test",
  "io.spray"                  % "spray-can"             % "1.2-20130712",
  "io.spray"                  % "spray-client"          % "1.2-20130712",
  "io.spray"                  % "spray-routing"         % "1.2-20130712",
  "io.spray"                 %% "spray-json"            % "1.2.3",
  "com.datastax.cassandra"    % "cassandra-driver-core" % "2.0.0-rc2",
  "joda-time"                 % "joda-time"             % "2.1",
  "org.joda"                  % "joda-convert"          % "1.2",
  "org.apache.kafka"         %% "kafka"                 % "0.8.0" excludeAll(
    ExclusionRule(organization = "com.sun.jdmk"),
    ExclusionRule(organization = "com.sun.jmx"),
    ExclusionRule(organization = "javax.jms")
    ),
  "com.typesafe"             %% "scalalogging-slf4j"    % "1.0.1",
  "ch.qos.logback"            % "logback-classic"       % "1.0.13",
  "com.softwaremill.macwire" %% "macros"                % "0.5",
  "com.softwaremill.macwire" %% "runtime"               % "0.5",
  "sneak"                    %% "sneak-commons"         % "0.0.1",
  "org.specs2"               %% "specs2"                % "2.2" % "test",
  "org.scalatest"            %% "scalatest"             % "2.0" % "test",
  "org.mockito"              %  "mockito-all"           % "1.9.5")

scalacOptions in Test ++= Seq("-Yrangepos")

net.virtualvoid.sbt.graph.Plugin.graphSettings