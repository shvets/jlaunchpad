1. Get jdom sources:

>ant jdom.co

2. Compile sources

2.1

>cd jdom

2.2. Modify build.bat file (add line):

SET JAVA_HOME=c:\Java\jdk1.5.0

2.3.

>ant compile

2.4.

>cd ..

>cd jdom-test

2.5.
 Modify build.bat file (add line):

SET JAVA_HOME=c:\Java\jdk1.5.0

2.6.

>ant compile

>cd ..

3. Run glean

>ant

