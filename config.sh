#!/bin/sh

# Default values
PROXY_SERVER_HOST_NAME=
PROXY_SERVER_PORT=
JAVA_HOME=~/jdk1.6.0_01
LAUNCHER_HOME=~/launcher
REPOSITORY_HOME=/media/hda5/maven-repository

# Constants
JAVA_SPECIFICATION_VERSION=1.5
LAUNCHER_VERSION=1.0
CLASSWORLDS_VERSION=1.1

CYGWIN=true

# Overwrites default values, if exists
if [ -f ~/jlaunchpad/config.sh ]; then
  . ~/jlaunchpad/config.sh
fi

if [ "x$JAVA_HOME" = "x" ]; then
  ECHO JDK cannot be found!
  pause
  exit 1
fi

# System properties
SYSTEM_PROPERTIES="-Dlauncher.home=$LAUNCHER_HOME \
-Drepository.home=$REPOSITORY_HOME \
-Dlauncher.version=$LAUNCHER_VERSION \
-Dclassworlds.version=$CLASSWORLDS_VERSION \
-Djava.specification.version=$JAVA_SPECIFICATION_VERSION"

if [ "x$PROXY_SERVER_HOST_NAME" = "x" ]; then
  SYSTEM_PROPERTIES="$SYSTEM_PROPERTIES -DproxyHost=$PROXY_SERVER_HOST_NAME -DproxyPort=$PROXY_SERVER_PORT"
fi

export PROXY_SERVER_HOST_NAME PROXY_SERVER_PORT
export JAVA_HOME REPOSITORY_HOME
export JAVA_SPECIFICATION_VERSION LAUNCHER_VERSION
export SYSTEM_PROPERTIES
