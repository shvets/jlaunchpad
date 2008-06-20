#!/bin/sh

DEBUG_MODE=@debug.mode@

DEBUG_OPTS="-Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=6006"

http_proxy=@jruby.proxy.line@

JAVA_HOME="@java.home.internal@"

readCommandLine() {
  for arg in $*; do
    processArg $arg
  done
}

processArg() {
  arg=$1

  if [ "$arg" = "" ]; then
    return
  fi

  if [ ${#arg} -lt 2 ]; then
    return
  fi

  PARAM1=`expr substr $arg 1 2`
  PARAM2=`expr substr $arg 1 18`
  PARAM3=`expr substr $arg 1 19`
  PARAM4=`expr substr $arg 1 17`

  if [ "$PARAM1" = "-D" ]; then
    JAVA_SYSTEM_PROPS="$JAVA_SYSTEM_PROPS $arg"
  elif [ "$PARAM2" = "-Xbootclasspath/p:" ]; then
    JAVA_BOOTCLASSPATH_PREPEND="$JAVA_BOOTCLASSPATH_PREPEND$SEPARATOR$arg"
  elif [ "$PARAM2" = "-Xbootclasspath/a:" ]; then
    JAVA_BOOTCLASSPATH_APPEND="$JAVA_BOOTCLASSPATH_APPEND$SEPARATOR$arg"
  elif [ "$PARAM4" = "-Xbootclasspath:" ]; then
    JAVA_BOOTCLASSPATH="$JAVA_BOOTCLASSPATH$SEPARATOR$arg"
  elif [ "$arg" = "-debug" ]; then
    JAVA_SYSTEM_PROPS="$JAVA_SYSTEM_PROPS $DEBUG_OPTS"
    DEBUG_MODE=true
  elif [ "$PARAM3" = "-Djava.library.path" ]; then
    JAVA_LIBRARY_PATH="$JAVA_LIBRARY_PATH $arg"
  else
    COMMAND_LINE_ARGS="$COMMAND_LINE_ARGS $arg"
  fi
}

readFile() {
    SECTION=""
    RESULT=""
    line=""

    if [ -n "`tail -1 $FILE`" ]; then
      echo>>$FILE
    fi

    while read line
    do
       processline
       processresult
    done < $FILE

}

processline() {
    case "$line" in
        '<java.classpath>')
            VARIABLE_NAME="JAVA_CLASSPATH"
            VARIABLE_VALUE="$JAVA_CLASSPATH"
            RESULT="";
            SEPARATOR=":"
            ;;
        '<java.endorsed.dirs>')
            VARIABLE_NAME="JAVA_ENDORSED_DIRS"
            VARIABLE_VALUE="$JAVA_ENDORSED_DIRS"
            RESULT="";
            SEPARATOR=":"
            ;;
        '<java.ext.dirs>')
            VARIABLE_NAME="JAVA_EXT_DIRS"
            VARIABLE_VALUE="$JAVA_EXT_DIRS"
            RESULT="";
            SEPARATOR=":"
            ;;
        '<java.library.path>')
            VARIABLE_NAME="JAVA_LIBRARY_PATH"
            VARIABLE_VALUE="$JAVA_LIBRARY_PATH"
            RESULT="";
            SEPARATOR=":"
            ;;
        '<java.system.props>')
            VARIABLE_NAME="JAVA_SYSTEM_PROPS"
            VARIABLE_VALUE="$JAVA_SYSTEM_PROPS"
            RESULT="";
            SEPARATOR=" "
            ;;
        '<java.bootclasspath>')
            VARIABLE_NAME="$JAVA_BOOTCLASSPATH"
            VARIABLE_VALUE="$JAVA_BOOTCLASSPATH"
            RESULT="";
            SEPARATOR=":"
            ;;
        '<java.bootclasspath.prepend>')
            VARIABLE_NAME="JAVA_BOOTCLASSPATH_PREPEND"
            VARIABLE_VALUE="$JAVA_BOOTCLASSPATH_PREPEND"
            RESULT="";
            SEPARATOR=":"
            ;;
        '<java.bootclasspath.append>')
            VARIABLE_NAME="JAVA_BOOTCLASSPATH_APPEND"
            VARIABLE_VALUE="$JAVA_BOOTCLASSPATH_APPEND"
            RESULT="";
            SEPARATOR=":"
            ;;
        '<jvm.args>')
            VARIABLE_NAME="JVM_ARGS"
            VARIABLE_VALUE="$JVM_ARGS"
            RESULT="";
            SEPARATOR=" "
            ;;
       '<launcher.class>')
            VARIABLE_NAME="LAUNCHER_CLASS"
            VARIABLE_VALUE="$LAUNCHER_CLASS"
            LAUNCHER_CLASS=
            RESULT="";
            SEPARATOR=" "
            ;;
       '<set.variables>')
            VARIABLE_NAME="SET_VARIABLES"
            VARIABLE_VALUE="$SET_VARIABLES"
            RESULT="";
            SEPARATOR=" "
            ;;
      '<command.line.args>')
            VARIABLE_NAME="COMMAND_LINE_ARGS"
            VARIABLE_VALUE="$COMMAND_LINE_ARGS"
            RESULT="";
            SEPARATOR=" "
            ;;
        *)
            join
    esac
}

join() {
    if [ -n "$line" ]; then
      # skip lines of zero length
 
      if [ `echo $line | cut -c 1` = '#' ]; then
        return # ignore lines starting with '#'
      fi

      if [ -n "$RESULT" ]; then
        RESULT="$RESULT$SEPARATOR"
      fi

      if [ "$VARIABLE_NAME" = "JAVA_SYSTEM_PROPS" ]; then
        RESULT="$RESULT-D$line"
      elif [ "$VARIABLE_NAME" = "SET_VARIABLES" ]; then
        eval `echo "$line"`
      elif [ "$VARIABLE_NAME" = "LAUNCHER_CLASS" ]; then
        if [ ! $LAUNCHER_CLASS ]; then
          LAUNCHER_CLASS="$line"
        fi
      else
        line=`echo $line | sed 's/\(%\)\([a-zA-Z0-9_]*\)\(\%\)/\$\2/g'`
        line=`eval echo "$line"` # evaluate environment variables used, if any

        RESULT="$RESULT$line"
      fi
    fi
}

processresult() {
  if [ "$VARIABLE_NAME" = "" ]; then
    return;
  fi

  if [ "$VARIABLE_NAME" = "LAUNCHER_CLASS" ]; then
    return;
  fi

  if [ "$VARIABLE_VALUE" ]; then
    eval `echo $VARIABLE_NAME=\"$VARIABLE_VALUE$SEPARATOR$RESULT\"`
  else
    eval `echo $VARIABLE_NAME=\"$RESULT\"`
  fi
}

#if [ -n "$JAVA_CMD" ]
#then
#    CMD="$JAVA_CMD"
#elif [ -n "$JAVA_HOME" ] # is JAVA_HOME defined
#then
#    CMD="$JAVA_HOME/bin/java"
#else
#    CMD=java
#fi

if [ -n "$CMD" ]; then
  CMD=java.exe
fi

if [ "x$JAVA_HOME" = "x" ]; then
  # do nothing
  tmp=tmp
else
  JAVA_CMD="$JAVA_HOME/bin/java"
fi

LAUNCHER_APP_CONF=$JLAUNCHPAD_HOME/jlaunchpad.jlcfg

if [ "$MAIN_APP_CONF" = "" ]; then
  MAIN_APP_CONF=$LAUNCHER_APP_CONF
fi

CURRENT_APP_CONF=`pwd`/$APP_NAME.jlcfg

JAVA_CLASSPATH=
JAVA_ENDORSED_DIRS=
JAVA_EXT_DIRS=
JAVA_LIBRARY_PATH=
JAVA_SYSTEM_PROPS=
JAVA_BOOTCLASSPATH=
JAVA_BOOTCLASSPATH_PREPEND=
JAVA_BOOTCLASSPATH_APPEND=
JVM_ARGS=
LAUNCHER_CLASS=
COMMAND_LINE_ARGS=


# process command line

readCommandLine $*

# process config file located in $jlaunchpad.home

FILE=$LAUNCHER_APP_CONF

if [ -r "$FILE" ]; then
  readFile
else
  echo $FILE not found
fi

# process config file located in $main.app.dir

FILE=$MAIN_APP_CONF

if [ -r "$FILE" ]; then
  if [ "$MAIN_APP_CONF" != "$LAUNCHER_APP_CONF" ]; then
    readFile
  fi
fi

# process config file located in $current.dir

FILE=$CURRENT_APP_CONF


if [ -r "$FILE" ]; then
  if [ "$CURRENT_APP_CONF" != "$LAUNCHER_APP_CONF" ]; then
    readFile
  fi
fi

if [ "$JAVA_CLASSPATH" != "" ]; then
  JAVA_CLASSPATH="-classpath $JAVA_CLASSPATH"
fi

if [ "$JAVA_BOOTCLASSPATH_PREPEND" != "" ]; then
  JAVA_BOOTCLASSPATH_PREPEND="-Xbootclasspath/p:$JAVA_BOOTCLASSPATH_PREPEND"
fi

if [ "$JAVA_BOOTCLASSPATH_APPEND" != "" ]; then
  JAVA_BOOTCLASSPATH_APPEND="-Xbootclasspath/a:$JAVA_BOOTCLASSPATH_APPEND"
fi

if [ "$JAVA_BOOTCLASSPATH" != "" ]; then
  JAVA_BOOTCLASSPATH="-Xbootclasspath:$JAVA_BOOTCLASSPATH"
fi

if [ "$JAVA_ENDORSED_DIRS" != "" ]; then
  JAVA_ENDORSED_DIRS="-Djava.endorsed.dirs=$JAVA_ENDORSED_DIRS"
fi

if [ "$JAVA_EXT_DIRS" != "" ]; then
  JAVA_EXT_DIRS="-Djava.ext.dirs=$JAVA_EXT_DIRS"
fi

if [ "$JAVA_LIBRARY_PATH" != "" ]; then
  JAVA_LIBRARY_PATH="-Djava.library.path=$JAVA_LIBRARY_PATH"
fi


if [ "$DEBUG_MODE" = "true" ]; then
  JAVA_SYSTEM_PROPS="$JAVA_SYSTEM_PROPS -Ddebug=true"
fi
 
if [ "$DEBUG_MODE" = "true" ]; then
  echo JAVA_BOOTCLASSPATH_APPEND: $JAVA_BOOTCLASSPATH_APPEND 
  echo JAVA_BOOTCLASSPATH_PREPEND: $JAVA_BOOTCLASSPATH_PREPEND 
  echo JAVA_BOOTCLASSPATH: $JAVA_BOOTCLASSPATH 
  echo JAVA_LIBRARY_PATH: $JAVA_LIBRARY_PATH 
  echo JAVA_EXT_DIRS: $JAVA_EXT_DIRS 
  echo JAVA_ENDORSED_DIRS: $JAVA_ENDORSED_DIRS
  echo JVM_ARGS: $JVM_ARGS
  echo JAVA_SYSTEM_PROPS: $JAVA_SYSTEM_PROPS
  echo JAVA_CLASSPATH: $JAVA_CLASSPATH
  echo LAUNCHER_CLASS: $LAUNCHER_CLASS
  echo COMMAND_LINE_ARGS: $COMMAND_LINE_ARGS
fi

$JAVA_CMD \
  $JAVA_BOOTCLASSPATH_APPEND $JAVA_BOOTCLASSPATH_PREPEND $JAVA_BOOTCLASSPATH \
  $JAVA_LIBRARY_PATH $JAVA_EXT_DIRS $JAVA_ENDORSED_DIRS \
  $JVM_ARGS \
  $JAVA_SYSTEM_PROPS \
  $JAVA_CLASSPATH \
  $LAUNCHER_CLASS \
  $COMMAND_LINE_ARGS
