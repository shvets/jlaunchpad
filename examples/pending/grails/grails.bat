SET JLAUNCHPAD_HOME=C:\jlaunchpad

SET GRAILS_HOME=%~dp0
                                                                                                   
SET MAIN_CLASS=org.codehaus.groovy.grails.cli.GrailsScriptRunner

SET SYSTEM_PROPERTIES=-Dgrails.version=1.0.0 -Dgrails.home=C:\Work\Projects\jlaunchpad\trunk\examples\pending\grails

SET PROPERTIES="-deps.file.name=%~dp0deps.xml" "-main.class.name=%MAIN_CLASS%"

SET APP_NAME=grails

%JLAUNCHPAD_HOME%\jlaunchpad.bat %SYSTEM_PROPERTIES% %PROPERTIES% %*
