@ECHO OFF

rem glean.bat

SET JLAUNCHPAD_HOME=c:\jlaunchpad

if exist "%USERPROFILE%\jlaunchpad\config.bat" (
  @call "%USERPROFILE%\jlaunchpad\config.bat"
)

SET MAIN_CLASS=org.apache.tools.ant.Main

SET PROPERTIES="-deps.file.name=%CD%\deps.xml" "-main.class.name=%MAIN_CLASS%"

%JLAUNCHPAD_HOME%\jlaunchpad.bat %PROPERTIES% -f build.xml %*
