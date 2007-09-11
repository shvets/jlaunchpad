@echo off

SET LAUNCHER_HOME=d:\launcher

if exist "%USERPROFILE%\jlaunchpad\config.bat" (
  @call "%USERPROFILE%\jlaunchpad\config.bat"
)

SET MAIN_CLASS=bsh.Interpreter

SET PROPERTIES="-deps.file.name=%~dp0deps.xml" "-main.class.name=%MAIN_CLASS%"

if "%1" == "" (
  echo "Please specify beanshell sript."
  exit
)

%LAUNCHER_HOME%\launcher.bat %PROPERTIES% %*

