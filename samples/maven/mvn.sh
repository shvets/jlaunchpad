#!/bin/sh

LAUNCHER_HOME=c:/launcher

if [ -f ~/jlaunchpad/config.sh ]; then
  . ~/jlaunchpad/config.sh
fi

MAIN_CLASS=org.apache.maven.cli.MavenCli

PWD=`pwd`

PROPERTIES="-deps.file.name=$PWD/deps.xml -main.class.name=$MAIN_CLASS"

$LAUNCHER_HOME/launcher.sh $PROPERTIES $*
