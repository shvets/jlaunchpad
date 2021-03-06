@echo off

SET JLAUNCHPAD_HOME=C:\jlaunchpad

if exist "%USERPROFILE%\jlaunchpad\config.bat" (
  @call "%USERPROFILE%\jlaunchpad\config.bat"
)

SET MAIN_CLASS=org.jruby.Main

SET PROPERTIES="-deps.file.name=%~dp0deps.xml" "-main.class.name=%MAIN_CLASS%"

if "%1" == "" (
  echo "Please specify ruby script."
  exit
)

%JLAUNCHPAD_HOME%\jlaunchpad.bat %PROPERTIES% %*

