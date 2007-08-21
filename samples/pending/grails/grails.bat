SET LAUNCHER_HOME=d:\launcher

if exist "%USERPROFILE%\jlaunchpad\config.bat" (
  @call "%USERPROFILE%\jlaunchpad\config.bat"
)

rem SET GRAILS_HOME=d:\JavaPrograms\grails-0.5.6
SET GRAILS_HOME=%~dp0

SET MAIN_CLASS=org.codehaus.groovy.grails.cli.GrailsScriptRunner

SET SYSTEM_PROPERTIES="-Dgrails.version=0.5.6"

SET PROPERTIES="-deps.file.name=%~dp0deps.xml" "-main.class.name=%MAIN_CLASS%"

SET APP_NAME=grails

%LAUNCHER_HOME%\launcher.bat %SYSTEM_PROPERTIES% %PROPERTIES% %*
