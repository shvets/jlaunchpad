#!/bin/sh

CYGWIN=true

JLAUNCHPAD_HOME=/cygdrive/d/jlaunchpad

if [ -f ~/jlaunchpad/config.sh ]; then
  . ~/jlaunchpad/config.sh
fi

MAIN_CLASS=groovy.ui.GroovyMain

PWD=`pwd`

if [ "$CYGWIN" = "true" ]; then
  PWD=`cygpath -wp $PWD`
fi

PROPERTIES="-deps.file.name=$PWD/deps.xml -main.class.name=$MAIN_CLASS"

# $JLAUNCHPAD_HOME/jlaunchpad.sh $PROPERTIES $*

$JLAUNCHPAD_HOME/jlaunchpad.sh $PROPERTIES Hello.groovy
