@echo off

SET JLAUNCHPAD_HOME=c:\Work\jlaunchpad

if exist "%USERPROFILE%\jlaunchpad\config.bat" (
  @call "%USERPROFILE%\jlaunchpad\config.bat"
)

SET MAIN_CLASS=bsh.Interpreter

SET PROPERTIES="-deps.file.name=%~dp0deps.xml" "-main.class.name=%MAIN_CLASS%"

if "%1" == "" (
  echo "Please specify beanshell sript."
  exit
)

%JLAUNCHPAD_HOME%\jlaunchpad.bat %PROPERTIES% %*

