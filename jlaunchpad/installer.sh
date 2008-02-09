#!/bin/sh

. ./config.sh

JLAUNCHPAD_PROJECT=.

BOOTSTRAP_MINI_PROJECT=$JLAUNCHPAD_PROJECT/projects/bootstrap-mini
POM_READER_PROJECT=$JLAUNCHPAD_PROJECT/projects/pom-reader
JDOM_PROJECT=projects/jdom
JLAUNCHPAD_COMMON_PROJECT=$JLAUNCHPAD_PROJECT/projects/jlaunchpad-common
JLAUNCHPAD_LAUNCHER_PROJECT=$JLAUNCHPAD_PROJECT/projects/jlaunchpad-launcher

CLASSPATH=$BOOTSTRAP_MINI_PROJECT/target/bootstrap-mini.jar
CLASSPATH=$CLASSPATH:$JLAUNCHPAD_COMMON_PROJECT/target/jlaunchpad-common.jar
CLASSPATH=$CLASSPATH:$POM_READER_PROJECT/target/pom-reader.jar
CLASSPATH=$CLASSPATH:$JLAUNCHPAD_LAUNCHER_PROJECT/target/jlaunchpad-launcher.jar
CLASSPATH=$CLASSPATH:$JDOM_PROJECT/target/jdom.jar

if [ "$CYGWIN" = "true" ]; then
  CLASSPATH=`cygpath -wp $CLASSPATH`
fi

# Overwrites default values, if exists
if [ -e user/config.sh ]; then
  . user/config.sh
fi

# MAIN_CLASS=org.sf.jlaunchpad.install.CoreInstaller
MAIN_CLASS=org.sf.jlaunchpad.install.GuiInstaller

$JAVA_HOME/bin/java -classpath $CLASSPATH $SYSTEM_PROPERTIES $MAIN_CLASS
