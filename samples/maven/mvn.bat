@echo off

SET LAUNCHER_HOME=c:\launcher

if exist "%USERPROFILE%\jlaunchpad\config.bat" (
  @call "%USERPROFILE%\jlaunchpad\config.bat"
)

SET MAIN_CLASS=org.apache.maven.cli.MavenCli

SET PROPERTIES="-deps.file.name=%~dp0deps.xml" "-main.class.name=%MAIN_CLASS%"

%LAUNCHER_HOME%\launcher.bat %PROPERTIES% %*
