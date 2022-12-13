@echo off
if not exist "%JAVA_HOME%\bin\jps.exe" echo PLEASE SET THE JAVA_HOME VARIABLE IN YOUR ENVIRONMENT, WE NEED JAVA(X64)! JDK8 OR LATER IS BETTER! & EXIT /B 1

setlocal

set "PATH=%JAVA_HOME%\bin;%PATH%"

echo KILLING deil-component SERVER

for /f "tokens=1" %%i in ('jps -m ^| find "deil-quartz"') do ( taskkill /F /PID %%i )

echo Done!
