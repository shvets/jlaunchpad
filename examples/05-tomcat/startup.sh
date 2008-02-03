#!/bin/sh

CYGWIN=true

JLAUNCHPAD_HOME=/home/alex/jlaunchpad

if [ -f ~/jlaunchpad/config.sh ]; then
  . ~/jlaunchpad/config.sh
fi

export APP_NAME=tomcat

$JLAUNCHPAD_HOME/jlaunchpad.sh $*
