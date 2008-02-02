@echo off 

if exist projects\bootstrap-mini\target rmdir /S /Q projects\bootstrap-mini\target
if exist projects\classworlds\target rmdir /S /Q projects\classworlds\target
if exist projects\pom-reader\target rmdir /S /Q projects\pom-reader\target
if exist projects\jlaunchpad-common\target rmdir /S /Q projects\jlaunchpad-common\target
if exist projects\jlaunchpad-launcher\target rmdir /S /Q projects\jlaunchpad-launcher\target

if exist target rmdir /S /Q target
if exist classes rmdir /S /Q classes
