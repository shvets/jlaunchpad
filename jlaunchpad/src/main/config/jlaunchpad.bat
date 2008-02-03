@echo off

set JLAUNCHPAD_HOME=@jlaunchpad.home@

set CMD=java.exe

call %JLAUNCHPAD_HOME%\jlaunchpad-core.bat %*
