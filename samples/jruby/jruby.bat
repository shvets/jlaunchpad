SET LAUNCHER_HOME=c:\launcher

if exist "%USERPROFILE%\jlaunchpad\config.bat" (
  @call "%USERPROFILE%\jlaunchpad\config.bat"
)

SET MAIN_CLASS=org.jruby.Main

SET PROPERTIES="-deps.file.name=%CD%\deps.xml" "-main.class.name=%MAIN_CLASS%"

rem %LAUNCHER_HOME%\launcher.bat %PROPERTIES% %*

%LAUNCHER_HOME%\launcher.bat %PROPERTIES% test1.rb 
