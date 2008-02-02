#!/bin/sh

CYGWIN=true

LAUNCHER_HOME=/home/alex/launcher

if [ -f ~/jlaunchpad/config.sh ]; then
  . ~/jlaunchpad/config.sh
fi

export APP_NAME=tomcat

$LAUNCHER_HOME/launcher.sh $*
