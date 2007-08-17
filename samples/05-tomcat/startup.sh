#!/bin/sh

CYGWIN=true

LAUNCHER_HOME=d:/launcher

if [ -f ~/jlaunchpad/config.sh ]; then
  . ~/jlaunchpad/config.sh
fi

export APP_NAME=tomcat

$LAUNCHER_HOME/launcher.sh $*
