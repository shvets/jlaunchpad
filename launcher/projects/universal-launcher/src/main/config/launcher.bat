@echo off

set LAUNCHER_HOME=@launcher.home@

SET DEBUG_MODE=@debug.mode@

set CMD=java.exe

call %LAUNCHER_HOME%\launcher-core.bat %*
