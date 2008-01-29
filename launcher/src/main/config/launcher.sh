#!/bin/sh

LAUNCHER_HOME=@launcher.home@

CMD=java

export LAUNCHER_HOME CMD

$LAUNCHER_HOME/launcher-core.sh $*
