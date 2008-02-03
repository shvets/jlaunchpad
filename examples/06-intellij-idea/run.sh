#!/bin/sh

JLAUNCHPAD_HOME=/media/hda5/jlaunchpad

if [ -f ~/jlaunchpad/config.sh ]; then
  . ~/jlaunchpad/config.sh
fi

export APP_NAME=idea

$JLAUNCHPAD_HOME/jlaunchpad.sh $*
