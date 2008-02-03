@echo off

SET JLAUNCHPAD_HOME=c:\jlaunchpad

if exist "%USERPROFILE%\jlaunchpad\config.bat" (
  @call "%USERPROFILE%\jlaunchpad\config.bat"
)

SET PROPERTIES="-deps.file.name=%~dp0cafebabe.sl" "-pomstarter" "-wait"

%JLAUNCHPAD_HOME%\jlaunchpad.bat %PROPERTIES% %*

