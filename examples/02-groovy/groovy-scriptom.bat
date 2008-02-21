SET JLAUNCHPAD_HOME=c:\jlaunchpad

if exist "%USERPROFILE%\jlaunchpad\config.bat" (
  @call "%jUSERPROFILE%\jlaunchpad\config.bat"
)

SET MAIN_CLASS=groovy.ui.GroovyMain

SET PROPERTIES="-deps.file.name=%CD%\deps.xml" "-main.class.name=%MAIN_CLASS%"

SET APP_NAME=groovy

%JLAUNCHPAD_HOME%\jlaunchpad.bat %PROPERTIES% ie.groovy
