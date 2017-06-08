@echo off
rem Script to launch the command line interface for mnt launcher
set BASE_HOME=%~dp0
set JAVA_EXEC=%BASE_HOME%\jre\bin\javaw
set CP=%BASE_HOME%\*;%BASE_HOME%\lib\*;
start %JAVA_EXEC% -cp "%CP%" com.mnt.gui.fx.launcher.MNTFXLauncher
@if errorlevel 1 off