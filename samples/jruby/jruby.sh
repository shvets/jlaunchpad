#!/bin/sh

CYGWIN=true

LAUNCHER_HOME=d:/launcher

if [ -f ~/jlaunchpad/config.sh ]; then
  . ~/jlaunchpad/config.sh
fi

MAIN_CLASS=org.jruby.Main

PWD=`pwd`

if [ "$CYGWIN" = "true" ]; then
  PWD=`cygpath -wp $PWD`
fi

PROPERTIES="-deps.file.name=$PWD/deps.xml -main.class.name=$MAIN_CLASS"

# $LAUNCHER_HOME/launcher.sh $PROPERTIES $*

$LAUNCHER_HOME/launcher.sh $PROPERTIES test1.rb
