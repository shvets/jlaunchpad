#!/bin/sh

LAUNCHER_HOME=/media/hda5/launcher

if [ -f ~/jlaunchpad/config.sh ]; then
  . ~/jlaunchpad/config.sh
fi

export APP_NAME=idea

$LAUNCHER_HOME/launcher.sh $*
