@echo off

SET LAUNCHER_HOME=C:\launcher

if exist "%USERPROFILE%\jlaunchpad\config.bat" (
  @call "%USERPROFILE%\jlaunchpad\config.bat"
)

SET MAIN_CLASS=org.jruby.Main

SET PROPERTIES="-deps.file.name=%~dp0deps.xml" "-main.class.name=%MAIN_CLASS%"

if "%1" == "" (
  echo "Please specify ruby script."
  exit
)

%LAUNCHER_HOME%\launcher.bat %PROPERTIES% %*

