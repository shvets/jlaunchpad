#!/bin/sh

LAUNCHER_HOME=d:/launcher

if [ -f ~/jlaunchpad/config.sh ]; then
  . ~/jlaunchpad/config.sh
fi

PROPERTIES="-deps.file.name=`pwd`/deps.xml -pomstarter -wait"

$LAUNCHER_HOME/launcher.sh $PROPERTIES
