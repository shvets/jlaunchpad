@echo off

SET DEBUG_MODE=@debug.mode@

SET DEBUG_OPTS=-Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=6006

SET http_proxy=@jruby.proxy.line@

SET JAVA_HOME=@java.home.internal@

goto execute

:processline
if %1 == "<java.classpath>" goto option
if %1 == "<java.endorsed.dirs>" goto option
if %1 == "<java.ext.dirs>" goto option
if %1 == "<java.library.path>" goto option
if %1 == "<java.system.props>" goto option
if %1 == "<java.bootclasspath>" goto option
if %1 == "<java.bootclasspath.append>" goto option
if %1 == "<java.bootclasspath.prepend>" goto option
if %1 == "<jvm.args>" goto option
if %1 == "<launcher.class>" goto option
if %1 == "<set.variables>" goto option
if %1 == "<command.line.args>" goto option

rem ignore if line is comment
SET LINE=%1
SET FIRST_CHAR=%LINE:~1,1%
IF "%FIRST_CHAR%" == "#" goto end

rem join the line to result
if defined RESULT set RESULT=%RESULT%%SEPARATOR%

if "%VARIABLE_NAME%" == "JAVA_SYSTEM_PROPS" (
  set RESULT=%RESULT%"-D%~1"
  goto end
)

if "%VARIABLE_NAME%" == "SET_VARIABLES" (
  set %~1
  goto end
)

if "%VARIABLE_NAME%" == "LAUNCHER_CLASS" (
  if not defined LAUNCHER_CLASS (
    SET LAUNCHER_CLASS=%~1
    goto end
  )
)

set RESULT=%RESULT%%~1
goto end

:option

rem append result to command
if DEFINED RESULT call :processresult

set OPTION=%1
set SECTION=%OPTION:~2,-2%
goto %SECTION%

:java.classpath
set VARIABLE_NAME=JAVA_CLASSPATH
set VARIABLE_VALUE=%JAVA_CLASSPATH%
set RESULT=
set PREFIX=
set SEPARATOR=;
goto end

:java.endorsed.dirs
set VARIABLE_NAME=JAVA_ENDORSED_DIRS
set VARIABLE_VALUE=%JAVA_ENDORSED_DIRS%
set RESULT=
set SEPARATOR=;
goto end

:java.ext.dirs
set VARIABLE_NAME=JAVA_EXT_DIRS
set VARIABLE_VALUE=%JAVA_EXT_DIRS%
set RESULT=
set SEPARATOR=;
goto end

:java.library.path
set VARIABLE_NAME=JAVA_LIBRARY_PATH
set VARIABLE_VALUE=%JAVA_LIBRARY_PATH%
set RESULT=
set SEPARATOR=;
goto end

:java.system.props
set VARIABLE_NAME=JAVA_SYSTEM_PROPS
set VARIABLE_VALUE=%JAVA_SYSTEM_PROPS%
set RESULT=
set SEPARATOR= 
goto end

:java.bootclasspath
set VARIABLE_NAME=JAVA_BOOTCLASSPATH
set VARIABLE_VALUE=%JAVA_BOOTCLASSPATH%
set RESULT=
set SEPARATOR=;
goto end

:java.bootclasspath.prepend
set VARIABLE_NAME=JAVA_BOOTCLASSPATH_PREPEND
set VARIABLE_VALUE=%JAVA_BOOTCLASSPATH_PREPEND%
set RESULT=
set SEPARATOR=;
goto end

:java.bootclasspath.append
set VARIABLE_NAME=JAVA_BOOTCLASSPATH_APPEND
set VARIABLE_VALUE=%JAVA_BOOTCLASSPATH_APPEND%
set RESULT=
set SEPARATOR=;
goto end

:jvm.args
set VARIABLE_NAME=JVM_ARGS
set VARIABLE_VALUE=%JVM_ARGS%
SET JVM_ARGS=
set RESULT=
set SEPARATOR= 
goto end

:launcher.class
set VARIABLE_NAME=LAUNCHER_CLASS
set VARIABLE_VALUE=%LAUNCHER_CLASS%
SET LAUNCHER_CLASS=
set RESULT=
set SEPARATOR= 
goto end

:set.variables
set VARIABLE_NAME=SET_VARIABLES
set VARIABLE_VALUE=%SET_VARIABLES%
set RESULT=
set SEPARATOR= 
goto end

:command.line.args
set VARIABLE_NAME=COMMAND_LINE_ARGS
set VARIABLE_VALUE=%COMMAND_LINE_ARGS%
set RESULT=
set SEPARATOR= 
goto end

:processresult

if not defined %VARIABLE_NAME% (
  set %VARIABLE_NAME%=%RESULT%
) else (
  set %VARIABLE_NAME%=%VARIABLE_VALUE%%SEPARATOR%%RESULT%
)

goto end

:processarg

if "%~1" == "" goto end

set CURR_ARG=%~1
set PARAM1=%CURR_ARG:~0,2%
set PARAM2=%CURR_ARG:~0,18%
set PARAM3=%CURR_ARG:~0,19%
set PARAM4=%CURR_ARG:~18%
set PARAM5=%CURR_ARG:~0,16%
set PARAM6=%CURR_ARG:~16%

if "%CURR_ARG:~0,2%" == "-D" (
  SET JAVA_SYSTEM_PROPS=%JAVA_SYSTEM_PROPS% "%~1"
) else if "%PARAM2%"=="-Xbootclasspath/p:" (
  if not "%JAVA_BOOTCLASSPATH_APPEND%" == "" (
    SET JAVA_BOOTCLASSPATH_PREPEND=%JAVA_BOOTCLASSPATH_PREPEND%%SEPARATOR%%PARAM4%
  ) else (
    SET JAVA_BOOTCLASSPATH_PERPEND=%PARAM4%
  )
) else if "%PARAM2%"=="-Xbootclasspath/a:" (
  if not "%JAVA_BOOTCLASSPATH_APPEND%" == "" (
    SET JAVA_BOOTCLASSPATH_APPEND=%JAVA_BOOTCLASSPATH_APPEND%%SEPARATOR%%PARAM4%
  ) else (
    SET JAVA_BOOTCLASSPATH_APPEND=%PARAM4%
  )
) else if "%PARAM5%"=="-Xbootclasspath:" (
  if not "%JAVA_BOOTCLASSPATH%" == "" (
    SET JAVA_BOOTCLASSPATH=%JAVA_BOOTCLASSPATH%%SEPARATOR%%PARAM6%
  ) else (
    SET JAVA_BOOTCLASSPATH_APPEND=%PARAM6%
  )
) else if "%CURR_ARG%"=="-debug" (
  SET JAVA_SYSTEM_PROPS=%JAVA_SYSTEM_PROPS% %DEBUG_OPTS%
  SET DEBUG_MODE=true
) else if "%PARAM3%"=="-Djava.library.path" (
  SET JAVA_LIBRARY_PATH=%JAVA_LIBRARY_PATH% "%~1%"
) else (
  set COMMAND_LINE_ARGS=%COMMAND_LINE_ARGS% "%~1%"
)

goto end

:execute

if not defined CMD set CMD=java.exe
if defined JAVA_HOME set JAVA_CMD="%JAVA_HOME%\bin\%CMD%"
rem if defined JAVA_CMD set CMD="%JAVA_CMD%"
rem set JAVA_CMD=

set LAUNCHER_APP_CONF=%JLAUNCHPAD_HOME%\jlaunchpad.jlcfg

if not defined MAIN_APP_CONF (
 SET MAIN_APP_CONF=%LAUNCHER_APP_CONF%
)

set CURRENT_APP_CONF=%CD%\%APP_NAME%.jlcfg

SET JAVA_CLASSPATH=
SET JAVA_ENDORSED_DIRS=
SET JAVA_EXT_DIRS=
SET JAVA_LIBRARY_PATH=
SET JAVA_SYSTEM_PROPS=
SET JAVA_BOOTCLASSPATH=
SET JAVA_BOOTCLASSPATH_PREPEND=
SET JAVA_BOOTCLASSPATH_APPEND=
SET JVM_ARGS=
SET LAUNCHER_CLASS=
SET COMMAND_LINE_ARGS=

rem process command line

rem FOR %%i in (%*) DO call :processarg ^%%i^

:cmdline_loop
if "x%~1" == "x" goto cmdline_loop_end

set CURR_ARG=%~1

if "%CURR_ARG:~0,2%" == "-D" (
  shift
  shift   
  call :processarg ^"%~1=%~2^"

  goto cmdline_loop
) else (
  shift
  call :processarg "%~1"
  goto cmdline_loop
)
:cmdline_loop_end

set SECTION=
set RESULT=

rem process config file located in $jlaunchpad.home

FOR /F "usebackq delims=" %%i in ("%LAUNCHER_APP_CONF%") DO call :processline  ^"%%i^"

rem process config file located in $main.app.dir

if not "%MAIN_APP_CONF%" == "%LAUNCHER_APP_CONF%" (
  if exist %MAIN_APP_CONF% FOR /F "usebackq delims=" %%i in ("%MAIN_APP_CONF%") DO call :processline ^"%%i^"
)

rem process config file located in $current.dir

if not "%CURRENT_APP_CONF%" == "%LAUNCHER_APP_CONF%" (
  if exist %CURRENT_APP_CONF% FOR /F "usebackq delims=" %%i in ("%CURRENT_APP_CONF%") DO call :processline ^"%%i^"
)


if DEFINED RESULT call :processresult

if not "%JAVA_CLASSPATH%" == "" (
  SET JAVA_CLASSPATH=-classpath "%JAVA_CLASSPATH%"
)

if not "%JAVA_BOOTCLASSPATH_PREPEND%" == "" (
  SET JAVA_BOOTCLASSPATH_PREPEND=-Xbootclasspath/p:"%JAVA_BOOTCLASSPATH_PREPEND%"
)

if not "%JAVA_BOOTCLASSPATH_APPEND%" == "" (
  SET JAVA_BOOTCLASSPATH_APPEND=-Xbootclasspath/a:"%JAVA_BOOTCLASSPATH_APPEND%"
)

if not "%JAVA_BOOTCLASSPATH%" == "" (
  SET JAVA_BOOTCLASSPATH_=-Xbootclasspath:"%JAVA_BOOTCLASSPATH%"
)

if not "%JAVA_ENDORSED_DIRS%" == "" (
  SET JAVA_ENDORSED_DIRS=-Djava.endorsed.dirs="%JAVA_ENDORSED_DIRS%"
)

if not "%JAVA_EXT_DIRS%" == "" (
  SET JAVA_EXT_DIRS=-Djava.ext.dirs="%JAVA_EXT_DIRS%"
)

if not "%JAVA_LIBRARY_PATH%" == "" (
  SET JAVA_LIBRARY_PATH=-Djava.library.path="%JAVA_LIBRARY_PATH%"
)

if "%DEBUG_MODE%" == "true" (
  SET JAVA_SYSTEM_PROPS=%JAVA_SYSTEM_PROPS% -Ddebug=true
)

if "%DEBUG_MODE%" == "true" (
  echo JAVA_BOOTCLASSPATH_APPEND: %JAVA_BOOTCLASSPATH_APPEND% 
  echo JAVA_BOOTCLASSPATH_PREPEND: %JAVA_BOOTCLASSPATH_PREPEND% 
  echo JAVA_BOOTCLASSPATH: %JAVA_BOOTCLASSPATH% 
  echo JAVA_LIBRARY_PATH: %JAVA_LIBRARY_PATH% 
  echo JAVA_EXT_DIRS: %JAVA_EXT_DIRS% 
  echo JAVA_ENDORSED_DIRS: %JAVA_ENDORSED_DIRS%
  echo JVM_ARGS: %JVM_ARGS%
  echo JAVA_SYSTEM_PROPS: %JAVA_SYSTEM_PROPS%
  echo JAVA_CLASSPATH: %JAVA_CLASSPATH%
  echo LAUNCHER_CLASS: %LAUNCHER_CLASS%
  echo COMMAND_LINE_ARGS: %COMMAND_LINE_ARGS%
)

%JAVA_CMD% ^
  %JAVA_BOOTCLASSPATH_APPEND% %JAVA_BOOTCLASSPATH_PREPEND% %JAVA_BOOTCLASSPATH% ^
  %JAVA_LIBRARY_PATH% %JAVA_EXT_DIRS% %JAVA_ENDORSED_DIRS% ^
  %JVM_ARGS% ^
  %JAVA_SYSTEM_PROPS% ^
  %JAVA_CLASSPATH% ^
  %LAUNCHER_CLASS% ^
  %COMMAND_LINE_ARGS%

:end
