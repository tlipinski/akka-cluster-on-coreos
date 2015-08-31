#!/bin/bash

JAR_NAME=$1
shift 1
OPTS=$@

java $OPTS -jar $JAR_NAME