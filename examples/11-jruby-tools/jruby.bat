@echo off

SET LAUNCHER_HOME=c:\launcher

if exist "%USERPROFILE%\jlaunchpad\config.bat" (
  @call "%USERPROFILE%\jlaunchpad\config.bat"
)

SET MAIN_CLASS=org.jruby.Main

SET PROPERTIES="-deps.file.name=%~dp0deps.xml" "-main.class.name=%MAIN_CLASS%"

if "%1" == "" (
  echo "Please specify ruby sript."
  exit
)

SET REPOSITOTY_HOME=c:\maven-repository
SET JRUBY_HOME=%REPOSITOTY_HOME%\jruby\jruby\1.0.1

SET SYSTEM_PARAMETERS="-Djruby.base=%JRUBY_HOME%"
SET SYSTEM_PARAMETERS=%SYSTEM_PARAMETERS% "-Djruby.home=%JRUBY_HOME%"
SET SYSTEM_PARAMETERS=%SYSTEM_PARAMETERS% "-Djruby.lib=%JRUBY_HOME%\lib"
SET SYSTEM_PARAMETERS=%SYSTEM_PARAMETERS% "-Djruby.shell=cmd.exe"
SET SYSTEM_PARAMETERS=%SYSTEM_PARAMETERS% "-Djruby.script=jruby.bat"

%LAUNCHER_HOME%\launcher.bat %SYSTEM_PARAMETERS% %PROPERTIES% %*

