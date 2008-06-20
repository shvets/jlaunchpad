@echo off

SET JLAUNCHPAD_HOME=C:\Work\jlaunchpad

if exist "%USERPROFILE%\jlaunchpad\config.bat" (
  @call "%USERPROFILE%\jlaunchpad\config.bat"
)

SET MAIN_CLASS=com.springsource.platform.kernel.bootstrap.Bootstrap

SET PROPERTIES="-deps.file.name=%~dp0ss-ap-deps.xml" "-main.class.name=%MAIN_CLASS%"

SET APP_NAME=ss-ap

%JLAUNCHPAD_HOME%\jlaunchpad.bat %PROPERTIES% %*
