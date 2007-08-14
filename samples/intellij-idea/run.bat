SET LAUNCHER_HOME=c:\launcher

if exist "%USERPROFILE%\jlaunchpad\config.bat" (
  @call "%USERPROFILE%\jlaunchpad\config.bat"
)

SET APP_NAME=idea

%LAUNCHER_HOME%\launcher.bat %*
