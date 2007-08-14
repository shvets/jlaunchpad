@ECHO OFF

rem glean.bat

SET LAUNCHER_HOME=d:\launcher

if exist "%USERPROFILE%\jlaunchpad\config.bat" (
  @call "%USERPROFILE%\jlaunchpad\config.bat"
)

SET GLEAN_HOME=C:\JavaPrograms\glean-1.2

SET MAIN_CLASS=org.apache.tools.ant.Main

rem SET PROPERTIES="-deps.file.name=%CD%\deps.xml" "-main.class.name=%MAIN_CLASS%" "-Dglean.home=%GLEAN_HOME%" "-Dfeedback.properties=%CD%\feedback.properties" "-Dbasedir=%CD%"
SET PROPERTIES="-deps.file.name=%CD%\deps.xml" "-main.class.name=%MAIN_CLASS%" "-Dglean.home=%GLEAN_HOME%" "-Dfeedback.properties=%CD%\feedback.properties" "-Dtool.home=%GLEAN_HOME%\tool\checkstyle"

rem %LAUNCHER_HOME%\launcher.bat %PROPERTIES% -f %GLEAN_HOME%\build.xml %*
%LAUNCHER_HOME%\launcher.bat %PROPERTIES% -f build.xml %*
