#!/bin/sh

CYGWIN=true

LAUNCHER_HOME=/cygdrive/d/launcher-cygnus

if [ -f ~/jlaunchpad/config.sh ]; then
  . ~/jlaunchpad/config.sh
fi

MAIN_CLASS=groovy.ui.GroovyMain

PWD=`pwd`

if [ "$CYGWIN" = "true" ]; then
  PWD=`cygpath -wp $PWD`
fi

PROPERTIES="-deps.file.name=$PWD/deps.xml -main.class.name=$MAIN_CLASS"

# $LAUNCHER_HOME/launcher.sh $PROPERTIES $*

$LAUNCHER_HOME/launcher.sh $PROPERTIES Hello.groovy
