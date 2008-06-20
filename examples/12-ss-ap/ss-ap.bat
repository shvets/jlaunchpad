@ECHO OFF

SET JAVA_HOME=c:\Java\jdk1.5.0

SET SS_AP_HOME=C:\Work\springsource-ap-1.0.0.beta6

SET SYSTEM_PROPERTIES=-Dcom.springsource.platform.home="%SS_AP_HOME%"
SET SYSTEM_PROPERTIES=%SYSTEM_PROPERTIES% -Djava.io.tmpdir="%SS_AP_HOME%\work\tmp"

SET CLASSPATH="%SS_AP_HOME%\lib\com.springsource.javax.transaction-1.1.0.jar"
SET CLASSPATH=%CLASSPATH%;"%SS_AP_HOME%\lib\com.springsource.json-1.0.0.BUILD-20080422112602.jar"
SET CLASSPATH=%CLASSPATH%;"%SS_AP_HOME%\lib\com.springsource.org.antlr-3.0.1.jar"
SET CLASSPATH=%CLASSPATH%;"%SS_AP_HOME%\lib\com.springsource.platform.bootstrap.configuration-1.0.0.beta6.jar"
SET CLASSPATH=%CLASSPATH%;"%SS_AP_HOME%\lib\com.springsource.platform.kernel.bootstrap-1.0.0.beta6.jar"
SET CLASSPATH=%CLASSPATH%;"%SS_AP_HOME%\lib\com.springsource.platform.osgi.framework-1.0.0.beta6.jar"
SET CLASSPATH=%CLASSPATH%;"%SS_AP_HOME%\lib\com.springsource.platform.osgi.manifest-1.0.0.beta6.jar"
SET CLASSPATH=%CLASSPATH%;"%SS_AP_HOME%\lib\com.springsource.platform.osgi.provisioning-1.0.0.beta6.jar"
SET CLASSPATH=%CLASSPATH%;"%SS_AP_HOME%\lib\com.springsource.platform.serviceability.logging-1.0.0.beta6.jar"
SET CLASSPATH=%CLASSPATH%;"%SS_AP_HOME%\lib\com.springsource.platform.serviceability.output-1.0.0.beta6.jar"
SET CLASSPATH=%CLASSPATH%;"%SS_AP_HOME%\lib\com.springsource.platform.serviceability.tracing-1.0.0.beta6.jar"
SET CLASSPATH=%CLASSPATH%;"%SS_AP_HOME%\lib\com.springsource.slf4j.api-1.5.0.jar"
SET CLASSPATH=%CLASSPATH%;"%SS_AP_HOME%\lib\com.springsource.slf4j.org.apache.commons.logging-1.5.0.jar"
SET CLASSPATH=%CLASSPATH%;"%SS_AP_HOME%\lib\com.springsource.slf4j.org.apache.log4j-1.5.0.jar"
SET CLASSPATH=%CLASSPATH%;"%SS_AP_HOME%\lib\org.eclipse.osgi-3.4.0.v20080529-1200.jar"
SET CLASSPATH=%CLASSPATH%;"%SS_AP_HOME%\lib\org.springframework.core-2.5.5.BUILD-20080530.jar" 

%JAVA_HOME%\bin\java %SYSTEM_PROPERTIES% -classpath %CLASSPATH% com.springsource.platform.kernel.bootstrap.Bootstrap