#!/bin/sh

JLAUNCHPAD_HOME=@jlaunchpad.home@

CMD=java

export JLAUNCHPAD_HOME CMD

$JLAUNCHPAD_HOME/jlaunchpad-core.sh $*
