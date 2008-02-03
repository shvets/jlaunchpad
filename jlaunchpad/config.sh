#!/bin/sh

# Default values
PROXY_SERVER_HOST_NAME=
PROXY_SERVER_PORT=
PROXY_USER=
PROXY_PASSWORD=

DEBUG_MODE=true
CYGWIN=false

JAVA_HOME=~/jdk1.6.0_03
JLAUNCHPAD_HOME=~/jlaunchpad
REPOSITORY_HOME=/media/sda2/maven-repository

# Constants
JAVA_SPECIFICATION_VERSION_LEVEL=1.5
JLAUNCHPAD_VERSION=1.0.1
CLASSWORLDS_VERSION=1.1
JDOM_VERSION=1.1
BOOTSTRAP_MINI_VERSION=2.0.8


# Overwrites default values, if exists
if [ -f ~/jlaunchpad/config.sh ]; then
  . ~/jlaunchpad/config.sh
else
  if [ -f user/config.sh ]; then
    . user/config.sh
  fi
fi

if [ "x$JAVA_HOME" = "x" ]; then
  ECHO JDK cannot be found!
  pause
  exit 1
fi

# System properties
SYSTEM_PROPERTIES="-Djlaunchpad.home=$JLAUNCHPAD_HOME \
-Drepository.home=$REPOSITORY_HOME \
-Djlaunchpad.version=$JLAUNCHPAD_VERSION \
-Dclassworlds.version=$CLASSWORLDS_VERSION \
-Djdom.version=$JDOM_VERSION \
-Dbootstrap-mini.version=$BOOTSTRAP_MINI_VERSION \
-Djava.specification.version.level=$JAVA_SPECIFICATION_VERSION_LEVEL \
-Djava.home.internal=$JAVA_HOME \
-Ddebug.mode=$DEBUG_MODE"

if [ "x$PROXY_SERVER_HOST_NAME" = "x" ]; then
  SYSTEM_PROPERTIES="$SYSTEM_PROPERTIES -DproxySet=true -DproxyHost=$PROXY_SERVER_HOST_NAME -DproxyPort=$PROXY_SERVER_PORT"
  SYSTEM_PROPERTIES="$SYSTEM_PROPERTIES -DproxyUser=$PROXY_USER -DproxyUser=$PROXY_PASSWORD"
fi

export PROXY_SERVER_HOST_NAME PROXY_SERVER_PORT
export JAVA_HOME REPOSITORY_HOME
export JAVA_SPECIFICATION_VERSION_LEVEL JLAUNCHPAD_VERSION
export SYSTEM_PROPERTIES