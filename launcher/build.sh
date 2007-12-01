#!/bin/sh

. ./config.sh

BOOTSTRAP_MINI_PROJECT=projects/bootstrap-mini
CLASSWORLDS_PROJECT=projects/classworlds
POM_READER_PROJECT=projects/pom-reader
UNIVERSAL_LAUNCHER_COMMON_PROJECT=projects/universal-launcher-common
UNIVERSAL_LAUNCHER_PROJECT=projects/universal-launcher

echo ---### Java Specification Version: $JAVA_SPECIFICATION_VERSION

echo ---### Builds bootstrap-mini project

if [ ! -f $BOOTSTRAP_MINI_PROJECT/target/classes ]; then
  mkdir -p $BOOTSTRAP_MINI_PROJECT/target/classes
fi

BM_CLASSPATH=$BOOTSTRAP_MINI_PROJECT/src/main/java

$JAVA_HOME/bin/javac -nowarn -source $JAVA_SPECIFICATION_VERSION -target $JAVA_SPECIFICATION_VERSION \
  -classpath $BM_CLASSPATH \
  -d $BOOTSTRAP_MINI_PROJECT/target/classes \
  $BOOTSTRAP_MINI_PROJECT/src/main/java/org/apache/maven/bootstrap/Bootstrap.java

$JAVA_HOME/bin/jar cf $BOOTSTRAP_MINI_PROJECT/target/bootstrap-mini.jar \
  -C $BOOTSTRAP_MINI_PROJECT/target/classes .

echo ---### Builds classworlds project

if [ ! -f $CLASSWORLDS_PROJECT/target/classes ]; then
  mkdir -p $CLASSWORLDS_PROJECT/target/classes
fi

CW_CLASSPATH=$CLASSWORLDS_PROJECT/src/main/java

$JAVA_HOME/bin/javac -nowarn -source $JAVA_SPECIFICATION_VERSION -target $JAVA_SPECIFICATION_VERSION \
  -classpath $CW_CLASSPATH \
  -d $CLASSWORLDS_PROJECT/target/classes \
  $CLASSWORLDS_PROJECT/src/main/java/org/codehaus/classworlds/*.java

$JAVA_HOME/bin/jar cf $CLASSWORLDS_PROJECT/target/classworlds.jar \
  -C $CLASSWORLDS_PROJECT/target/classes .

echo ---### Builds universal-launcher-common project

if [ ! -f $UNIVERSAL_LAUNCHER_COMMON_PROJECT/target/classes ]; then
  mkdir -p $UNIVERSAL_LAUNCHER_COMMON_PROJECT/target/classes
fi

UL_COMMON_CLASSPATH=$UNIVERSAL_LAUNCHER_COMMON_PROJECT/src/main/java

$JAVA_HOME/bin/javac -nowarn -source $JAVA_SPECIFICATION_VERSION -target $JAVA_SPECIFICATION_VERSION \
  -classpath $UL_COMMON_CLASSPATH \
  -d $UNIVERSAL_LAUNCHER_COMMON_PROJECT/target/classes \
  $UNIVERSAL_LAUNCHER_COMMON_PROJECT/src/main/java/org/sf/jlaunchpad/util/*.java

$JAVA_HOME/bin/jar cf $UNIVERSAL_LAUNCHER_COMMON_PROJECT/target/universal-launcher-common.jar \
  -C $UNIVERSAL_LAUNCHER_COMMON_PROJECT/target/classes .

echo ---### Builds pom-reader project

if [ ! -f $POM_READER_PROJECT/target/classes ]; then
  mkdir -p $POM_READER_PROJECT/target/classes
fi

PR_CLASSPATH=$BOOTSTRAP_MINI_PROJECT/target/classes
PR_CLASSPATH=$PR_CLASSPATH:$UNIVERSAL_LAUNCHER_COMMON_PROJECT/target/classes
PR_CLASSPATH=$PR_CLASSPATH:$POM_READER_PROJECT/src/main/java

if [ "$CYGWIN" = "true" ]; then
  PR_CLASSPATH=`cygpath -wp $PR_CLASSPATH`
fi

$JAVA_HOME/bin/javac -nowarn -source $JAVA_SPECIFICATION_VERSION -target $JAVA_SPECIFICATION_VERSION \
  -classpath $PR_CLASSPATH \
  -sourcepath $POM_READER_PROJECT/src/main/java \
  -d $POM_READER_PROJECT/target/classes \
  $POM_READER_PROJECT/src/main/java/org/sf/pomreader/*.java

$JAVA_HOME/bin/jar cf $POM_READER_PROJECT/target/pom-reader.jar \
  -C $POM_READER_PROJECT/target/classes .

echo ---### Builds universal-launcher project

if [ ! -f $UNIVERSAL_LAUNCHER_PROJECT/target/classes ]; then
  mkdir -p $UNIVERSAL_LAUNCHER_PROJECT/target/classes
fi

UL_CLASSPATH=$UNIVERSAL_LAUNCHER_PROJECT/src/main/java
UL_CLASSPATH=$UL_CLASSPATH:$UNIVERSAL_LAUNCHER_COMMON_PROJECT/target/classes
UL_CLASSPATH=$UL_CLASSPATH:$BOOTSTRAP_MINI_PROJECT/target/classes
UL_CLASSPATH=$UL_CLASSPATH:$POM_READER_PROJECT/target/classes
UL_CLASSPATH=$UL_CLASSPATH:$CLASSWORLDS_PROJECT/target/classes

if [ "$CYGWIN" = "true" ]; then
  UL_CLASSPATH=`cygpath -wp $UL_CLASSPATH`
fi

$JAVA_HOME/bin/javac -nowarn -source $JAVA_SPECIFICATION_VERSION -target $JAVA_SPECIFICATION_VERSION \
  -classpath $UL_CLASSPATH \
  -d $UNIVERSAL_LAUNCHER_PROJECT/target/classes \
  $UNIVERSAL_LAUNCHER_PROJECT/src/main/java/org/sf/jlaunchpad/core/*.java \
  $UNIVERSAL_LAUNCHER_PROJECT/src/main/java/org/sf/jlaunchpad/install/*.java \
  $UNIVERSAL_LAUNCHER_PROJECT/src/main/java/org/sf/jlaunchpad/*.java

$JAVA_HOME/bin/jar cf $UNIVERSAL_LAUNCHER_PROJECT/target/universal-launcher.jar \
  -C $UNIVERSAL_LAUNCHER_PROJECT/target/classes .
