@ECHO OFF

@call config.bat

SET BOOTSTRAP_MINI_PROJECT=projects\bootstrap-mini
SET CLASSWORLDS_PROJECT=projects\classworlds
SET POM_READER_PROJECT=projects\pom-reader
SET UNIVERSAL_LAUNCHER_COMMON_PROJECT=projects\universal-launcher-common
SET UNIVERSAL_LAUNCHER_PROJECT=projects\universal-launcher

echo ---### Java Specification Version: %JAVA_SPECIFICATION_VERSION%

echo ---### Builds bootstrap-mini project

if not exist %BOOTSTRAP_MINI_PROJECT%\target\classes (
  mkdir %BOOTSTRAP_MINI_PROJECT%\target\classes
)

SET BM_CLASSPATH=%BOOTSTRAP_MINI_PROJECT%\src\main\java

%JAVA_HOME%\bin\javac -nowarn -source %JAVA_SPECIFICATION_VERSION% -target %JAVA_SPECIFICATION_VERSION% ^
  -classpath %BM_CLASSPATH% ^
  -d %BOOTSTRAP_MINI_PROJECT%\target\classes ^
  %BOOTSTRAP_MINI_PROJECT%\src\main\java\org\apache\maven\bootstrap\Bootstrap.java

%JAVA_HOME%\bin\jar cf %BOOTSTRAP_MINI_PROJECT%\target\bootstrap-mini.jar ^
  -C %BOOTSTRAP_MINI_PROJECT%\target\classes .

echo ---### Builds classworlds project

if not exist %CLASSWORLDS_PROJECT%\target\classes (
  mkdir %CLASSWORLDS_PROJECT%\target\classes
)

SET CW_CLASSPATH=%CLASSWORLDS_PROJECT%\src\main\java

%JAVA_HOME%\bin\javac -nowarn -source %JAVA_SPECIFICATION_VERSION% -target %JAVA_SPECIFICATION_VERSION% ^
  -classpath %CW_CLASSPATH% ^
  -d %CLASSWORLDS_PROJECT%\target\classes ^
  %CLASSWORLDS_PROJECT%\src\main\java\org\codehaus\classworlds\*.java

%JAVA_HOME%\bin\jar cf %CLASSWORLDS_PROJECT%\target\classworlds.jar ^
  -C %CLASSWORLDS_PROJECT%\target\classes .

echo ---### Builds universal-launcher-common project

if not exist %UNIVERSAL_LAUNCHER_COMMON_PROJECT%\target\classes (
  mkdir %UNIVERSAL_LAUNCHER_COMMON_PROJECT%\target\classes
)

SET UL_COMMON_CLASSPATH=%UNIVERSAL_LAUNCHER_COMMON_PROJECT%\src\main\java

%JAVA_HOME%\bin\javac -nowarn -source %JAVA_SPECIFICATION_VERSION% -target %JAVA_SPECIFICATION_VERSION% ^
  -classpath %UL_COMMON_CLASSPATH% ^
  -d %UNIVERSAL_LAUNCHER_COMMON_PROJECT%\target\classes ^
  %UNIVERSAL_LAUNCHER_COMMON_PROJECT%\src\main\java\org\sf\jlaunchpad\util\*.java

%JAVA_HOME%\bin\jar cf %UNIVERSAL_LAUNCHER_COMMON_PROJECT%\target\universal-launcher-common.jar ^
  -C %UNIVERSAL_LAUNCHER_COMMON_PROJECT%\target\classes .

echo ---### Builds pom-reader project

if not exist %POM_READER_PROJECT%\target\classes (
  mkdir %POM_READER_PROJECT%\target\classes
)

SET PR_CLASSPATH=%BOOTSTRAP_MINI_PROJECT%\target\classes
SET PR_CLASSPATH=%PR_CLASSPATH%;%UNIVERSAL_LAUNCHER_COMMON_PROJECT%\target\classes
SET PR_CLASSPATH=%PR_CLASSPATH%;%POM_READER_PROJECT%\src\main\java

%JAVA_HOME%\bin\javac -nowarn -source %JAVA_SPECIFICATION_VERSION% -target %JAVA_SPECIFICATION_VERSION% ^
  -classpath %PR_CLASSPATH% ^
  -d %POM_READER_PROJECT%\target\classes ^
  %POM_READER_PROJECT%\src\main\java\org\sf\pomreader\*.java 

%JAVA_HOME%\bin\jar cf %POM_READER_PROJECT%\target\pom-reader.jar ^
  -C %POM_READER_PROJECT%\target\classes .

echo ---### Builds universal-launcher project

if not exist %UNIVERSAL_LAUNCHER_PROJECT%\target\classes (
  mkdir %UNIVERSAL_LAUNCHER_PROJECT%\target\classes
)

SET UL_CLASSPATH=%UNIVERSAL_LAUNCHER_PROJECT%\src\main\java
SET UL_CLASSPATH=%UL_CLASSPATH%;%UNIVERSAL_LAUNCHER_COMMON_PROJECT%\target\classes
SET UL_CLASSPATH=%UL_CLASSPATH%;%BOOTSTRAP_MINI_PROJECT%\target\classes
SET UL_CLASSPATH=%UL_CLASSPATH%;%POM_READER_PROJECT%\target\classes
SET UL_CLASSPATH=%UL_CLASSPATH%;%CLASSWORLDS_PROJECT%\target\classes

%JAVA_HOME%\bin\javac -nowarn -source %JAVA_SPECIFICATION_VERSION% -target %JAVA_SPECIFICATION_VERSION% ^
  -classpath %UL_CLASSPATH% ^
  -d %UNIVERSAL_LAUNCHER_PROJECT%\target\classes ^
  %UNIVERSAL_LAUNCHER_PROJECT%\src\main\java\org\sf\jlaunchpad\core\*.java ^
  %UNIVERSAL_LAUNCHER_PROJECT%\src\main\java\org\sf\jlaunchpad\install\*.java ^
  %UNIVERSAL_LAUNCHER_PROJECT%\src\main\java\org\sf\jlaunchpad\*.java

%JAVA_HOME%\bin\jar cf %UNIVERSAL_LAUNCHER_PROJECT%\target\universal-launcher.jar ^
  -C %UNIVERSAL_LAUNCHER_PROJECT%\target\classes .
