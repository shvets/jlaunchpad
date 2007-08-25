#!/bin/sh

. ./config.sh

JLAUNCHPAD_PROJECT=.

BOOTSTRAP_MINI_PROJECT=$JLAUNCHPAD_PROJECT/projects/bootstrap-mini
UNIVERSAL_LAUNCHER_COMMON_PROJECT=$JLAUNCHPAD_PROJECT/projects/universal-launcher-common
POM_READER_PROJECT=$JLAUNCHPAD_PROJECT/projects/pom-reader
UNIVERSAL_LAUNCHER_PROJECT=$JLAUNCHPAD_PROJECT/projects/universal-launcher

CLASSPATH=$BOOTSTRAP_MINI_PROJECT/target/bootstrap-mini.jar
CLASSPATH=$CLASSPATH:$UNIVERSAL_LAUNCHER_COMMON_PROJECT/target/universal-launcher-common.jar
CLASSPATH=$CLASSPATH:$POM_READER_PROJECT/target/pom-reader.jar
CLASSPATH=$CLASSPATH:$UNIVERSAL_LAUNCHER_PROJECT/target/universal-launcher.jar

if [ "$CYGWIN" = "true" ]; then
  CLASSPATH=`cygpath -wp $CLASSPATH`
fi

# MAIN_CLASS=org.sf.jlaunchpad.install.CoreInstaller
MAIN_CLASS=org.sf.jlaunchpad.install.GuiInstaller

$JAVA_HOME/bin/java -classpath $CLASSPATH $SYSTEM_PROPERTIES $MAIN_CLASS
