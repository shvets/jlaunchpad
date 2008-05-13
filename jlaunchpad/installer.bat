@ECHO OFF

@call %~dp0config.bat

SET JLAUNCHPAD_PROJECT=%~dp0

SET BOOTSTRAP_MINI_PROJECT=%JLAUNCHPAD_PROJECT%\projects\bootstrap-mini
SET POM_READER_PROJECT=%JLAUNCHPAD_PROJECT%\projects\pom-reader
SET JDOM_PROJECT=%JLAUNCHPAD_PROJECT%\projects\jdom
SET JLAUNCHPAD_COMMON_PROJECT=%JLAUNCHPAD_PROJECT%\projects\jlaunchpad-common
SET JLAUNCHPAD_LAUNCHER_PROJECT=%JLAUNCHPAD_PROJECT%\projects\jlaunchpad-launcher

SET CLASSPATH=%BOOTSTRAP_MINI_PROJECT%\target\bootstrap-mini.jar
SET CLASSPATH=%CLASSPATH%;%JLAUNCHPAD_COMMON_PROJECT%\target\jlaunchpad-common.jar
SET CLASSPATH=%CLASSPATH%;%POM_READER_PROJECT%\target\pom-reader.jar
SET CLASSPATH=%CLASSPATH%;%JLAUNCHPAD_LAUNCHER_PROJECT%\target\jlaunchpad-launcher.jar
SET CLASSPATH=%CLASSPATH%;%JDOM_PROJECT%\target\jdom.jar

REM SET MAIN_CLASS=org.sf.jlaunchpad.install.CoreInstaller
SET MAIN_CLASS=org.sf.jlaunchpad.install.GuiInstaller

rem Overwrites default values, if exists
IF EXIST user\config.bat (
  @call "user\config.bat"
)

%JAVA_HOME%\bin\java -classpath %CLASSPATH% %SYSTEM_PROPERTIES% -Dbasedir=. %MAIN_CLASS%
