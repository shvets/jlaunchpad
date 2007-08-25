SET LAUNCHER_HOME=c:\launcher

if exist "%USERPROFILE%\jlaunchpad\config.bat" (
  @call "%USERPROFILE%\jlaunchpad\config.bat"
)

SET APP_NAME=tomcat

start %LAUNCHER_HOME%\launcher.bat %*
