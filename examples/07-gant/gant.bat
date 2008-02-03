SET JLAUNCHPAD_HOME=c:\jlaunchpad

if exist "%USERPROFILE%\jlaunchpad\config.bat" (
  @call "%USERPROFILE%\jlaunchpad\config.bat"
)

SET MAIN_CLASS=gant.Gant

SET PROPERTIES="-deps.file.name=%CD%\deps.xml" "-main.class.name=%MAIN_CLASS%"

%JLAUNCHPAD_HOME%\jlaunchpad.bat %PROPERTIES% %*
