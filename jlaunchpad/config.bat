rem config.bat

rem Default values

SET DRIVE_LETTER=c:

SET JAVA_HOME=%DRIVE_LETTER%\Java\jdk1.5.0

IF NOT EXIST %JAVA_HOME% (
  ECHO JDK cannot be found!
  PAUSE
  EXIT
)


SET DEBUG_MODE=false

rem Constants

SET JAVA_SPECIFICATION_VERSION_LEVEL=1.5
SET JLAUNCHPAD_VERSION=1.0.2
SET CLASSWORLDS_VERSION=1.1
SET JDOM_VERSION=1.1
SET BOOTSTRAP_MINI_VERSION=2.0.9

rem System properties

SET SYSTEM_PROPERTIES=-Djlaunchpad.version=%JLAUNCHPAD_VERSION%
SET SYSTEM_PROPERTIES=%SYSTEM_PROPERTIES% -Dclassworlds.version=%CLASSWORLDS_VERSION%
SET SYSTEM_PROPERTIES=%SYSTEM_PROPERTIES% -Djdom.version=%JDOM_VERSION%
SET SYSTEM_PROPERTIES=%SYSTEM_PROPERTIES% -Dbootstrap-mini.version=%BOOTSTRAP_MINI_VERSION%
SET SYSTEM_PROPERTIES=%SYSTEM_PROPERTIES% -Djava.specification.version.level=%JAVA_SPECIFICATION_VERSION_LEVEL%
SET SYSTEM_PROPERTIES=%SYSTEM_PROPERTIES% -Djava.home.internal=%JAVA_HOME%
SET SYSTEM_PROPERTIES=%SYSTEM_PROPERTIES% -Ddebug.mode=%DEBUG_MODE%

