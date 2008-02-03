#!/bin/sh

JLAUNCHPAD_HOME=d:/jlaunchpad

if [ -f ~/jlaunchpad/config.sh ]; then
  . ~/jlaunchpad/config.sh
fi

PROPERTIES="-deps.file.name=`pwd`/deps.xml -pomstarter -wait"

$JLAUNCHPAD_HOME/jlaunchpad.sh $PROPERTIES
