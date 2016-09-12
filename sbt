#!/bin/bash
LAUNCH=`dirname $0`/sbt-launch.jar

if [ ! -f $LAUNCH ]; then
    wget -O $LAUNCH https://repo.typesafe.com/typesafe/ivy-releases/org.scala-sbt/sbt-launch/0.13.11/sbt-launch.jar
fi

SBT_OPTS="-Xms512M -Xmx1536M -Xss1M -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=256M"
java $SBT_OPTS -jar $LAUNCH "$@"
