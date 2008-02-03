SET JLAUNCHPAD_HOME=c:\jlaunchpad

if exist "%USERPROFILE%\jlaunchpad\config.bat" (
  @call "%USERPROFILE%\jlaunchpad\config.bat"
)

SET APP_NAME=tomcat

start %JLAUNCHPAD_HOME%\jlaunchpad.bat %*
