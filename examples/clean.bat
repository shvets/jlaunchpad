@call mvn.bat -f pom.xml clean

if exist projects\scalac\target rmdir /S /Q projects\scalac\target

if exist projects\grails\target rmdir /S /Q projects\grails\target

if exist target rmdir /S /Q target
