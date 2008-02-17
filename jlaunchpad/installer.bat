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

%JAVA_HOME%\bin\java -classpath %CLASSPATH% %SYSTEM_PROPERTIES% -Dbasedir=. %MAIN_CLASS%

rem Overwrites default values, if exists
IF EXIST user\config.bat (
  @call "user\config.bat"
)

rem System properties
SET SYSTEM_PROPERTIES=-Djlaunchpad.home=%JLAUNCHPAD_HOME%
SET SYSTEM_PROPERTIES=%SYSTEM_PROPERTIES% -Drepository.home=%REPOSITORY_HOME%
SET SYSTEM_PROPERTIES=%SYSTEM_PROPERTIES% -Djlaunchpad.version=%JLAUNCHPAD_VERSION%
SET SYSTEM_PROPERTIES=%SYSTEM_PROPERTIES% -Dclassworlds.version=%CLASSWORLDS_VERSION%
SET SYSTEM_PROPERTIES=%SYSTEM_PROPERTIES% -Djdom.version=%JDOM_VERSION%
SET SYSTEM_PROPERTIES=%SYSTEM_PROPERTIES% -Dbootstrap-mini.version=%BOOTSTRAP_MINI_VERSION%
SET SYSTEM_PROPERTIES=%SYSTEM_PROPERTIES% -Djava.specification.version.level=%JAVA_SPECIFICATION_VERSION_LEVEL%
SET SYSTEM_PROPERTIES=%SYSTEM_PROPERTIES% -Djava.home.internal=%JAVA_HOME%
SET SYSTEM_PROPERTIES=%SYSTEM_PROPERTIES% -Ddebug.mode=%DEBUG_MODE%

IF NOT "%PROXY_SERVER_HOST_NAME" == "" (
  SET SYSTEM_PROPERTIES=%SYSTEM_PROPERTIES% -DproxySet=true -DproxyHost=%PROXY_SERVER_HOST_NAME% -DproxyPort=%PROXY_SERVER_PORT%
  SET SYSTEM_PROPERTIES=%SYSTEM_PROPERTIES% -DproxyUser=%PROXY_USER% -DproxyPassword=%PROXY_PASSWORD%
)
