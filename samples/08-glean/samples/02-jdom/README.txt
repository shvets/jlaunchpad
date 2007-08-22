1. Get jdom sources:

>ant jdom.co

or unzip archive in the current directory.

2. Compile sources

2.1

>cd jdom

2.2. Modify build.bat file (add line):

SET JAVA_HOME=c:\Java\jdk1.5.0

2.3.

>build.bat

2.4.

>cd ..

>cd jdom-test

2.5.
 Modify build.bat file (add line):

SET JAVA_HOME=c:\Java\jdk1.5.0

2.6.

>build.bat

>cd ..

3. Run glean

>ant

