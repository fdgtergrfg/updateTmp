#!/bin/bash

tmp='./bin/resources'
tmp='./target/classes':$tmp
tmp='./target/fetchnetworks-1.0-SNAPSHOT-jar-with-dependencies-without-resources/*':$tmp

CLASSPATH=$tmp:$CLASSPATH


echo $CLASSPATH
JVM_ARGS="-Xmn98m -Xmx128m -Xms128m -XX:NewRatio=4 -XX:SurvivorRatio=4 -XX:MaxTenuringThreshold=2"

java $JVM_ARGS -classpath $CLASSPATH com.ossean,Main $SITE >>log/info.log 2>&1 &