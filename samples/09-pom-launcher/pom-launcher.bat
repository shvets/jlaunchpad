@echo off

SET LAUNCHER_HOME=c:\launcher

if exist "%USERPROFILE%\jlaunchpad\config.bat" (
  @call "%USERPROFILE%\jlaunchpad\config.bat"
)

SET PROPERTIES="-deps.file.name=%~dp0cafebabe.sl" "-pomstarter" "-wait"

%LAUNCHER_HOME%\launcher.bat %PROPERTIES% %*

