SET JAVA_HOME=c:\Java\jdk1.5.0
SET SYSTEM_PARAMS=-Xmx378m -Xss1024k -Xverify:none -da
SET REPOSITORY_HOME=c:\maven-repository

SET CLASSPATH=%REPOSITORY_HOME%\jruby\jruby\1.0.2\jruby-1.0.2.jar
SET CLASSPATH=%CLASSPATH%;%REPOSITORY_HOME%\asm\asm\2.2.3\asm-2.2.3.jar
SET CLASSPATH=%CLASSPATH%;%REPOSITORY_HOME%\asm\asm-commons\2.2.3\asm-commons-2.2.3.jar
SET CLASSPATH=%CLASSPATH%;%REPOSITORY_HOME%\jline\jline\0.9.91\jline-0.9.91.jar
SET CLASSPATH=%CLASSPATH%;%REPOSITORY_HOME%\backport-util-concurrent\backport-util-concurrent\3.0\backport-util-concurrent-3.0.jar


rem SET JAVA_PROPS=-Djruby.base="c:\test" -Djruby.home="c:\test" -Djruby.lib="c:\test\lib" -Djruby.shell="cmd.exe" -Djruby.script=jruby.bat

SET JAVA_PROPS=-Djruby.base="%REPOSITORY_HOME%\jruby\jruby\1.0.2" -Djruby.home="%REPOSITORY_HOME%\jruby\jruby\1.0.2" -Djruby.lib="%REPOSITORY_HOME%\jruby\jruby\1.0.2\lib" -Djruby.shell="cmd.exe" -Djruby.script=jruby.bat

rem SET JAVA_PROPS=-Djruby.base="C:\Work\Downloads\jruby-1.0.2\bin\.." -Djruby.home="C:\Work\Downloads\jruby-1.0.2\bin\.." -Djruby.lib="C:\Work\Downloads\jruby-1.0.2\bin\..\lib" -Djruby.shell="cmd.exe" -Djruby.script=jruby.bat

%JAVA_HOME%\bin\java %SYSTEM_PARAMS% -cp %CLASSPATH% %JAVA_PROPS% org.jruby.Main -S gem install rails --include-dependencies --no-ri --no-rdoc

rem http://weblogs.java.net/blog/arungupta/archive/2007/11/jruby_102_relea.html