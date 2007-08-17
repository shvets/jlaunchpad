SET LAUNCHER_HOME=d:\launcher-cygnus

if exist "%USERPROFILE%\jlaunchpad\config.bat" (
  @call "%USERPROFILE%\jlaunchpad\config.bat"
)

SET MAIN_CLASS=groovy.ui.GroovyMain

SET PROPERTIES="-deps.file.name=%CD%\deps.xml" "-main.class.name=%MAIN_CLASS%"

SET APP_NAME=groovy

%LAUNCHER_HOME%\launcher.bat %PROPERTIES% ie.groovy
