SET LAUNCHER_HOME=d:\launcher

if exist "%USERPROFILE%\jlaunchpad\config.bat" (
  @call "%USERPROFILE%\jlaunchpad\config.bat"
)

SET GRAILS_HOME=C:\Work\jlaunchpad\trunk\samples\grails

SET MAIN_CLASS=org.codehaus.groovy.grails.cli.GrailsScriptRunner

SET PROPERTIES="-deps.file.name=%GRAILS_HOME%\deps.xml" "-main.class.name=%MAIN_CLASS%"

SET APP_NAME=grails

%LAUNCHER_HOME%\launcher.bat %PROPERTIES% %*
