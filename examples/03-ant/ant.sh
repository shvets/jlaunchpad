#!/bin/sh

LAUNCHER_HOME=/home/alex/launcher

if [ -f ~/jlaunchpad/config.sh ]; then
  . ~/jlaunchpad/config.sh
fi

MAIN_CLASS=org.apache.tools.ant.Main

PWD=`pwd`

PROPERTIES="-deps.file.name=$PWD/deps.xml -main.class.name=$MAIN_CLASS"

$LAUNCHER_HOME/launcher.sh $PROPERTIES $*
