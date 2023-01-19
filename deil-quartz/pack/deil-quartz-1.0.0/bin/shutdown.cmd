@echo off
if not exist "%JAVA_HOME%\bin\jps.exe" echo PLEASE CHECK THE JAVA_HOME & EXIT /B 1

setlocal

set "PATH=%JAVA_HOME%\bin;%PATH%"

for /f "tokens=1" %%i in ('jps -m ^| find "deil-quartz"') do ( taskkill /F /PID %%i )

echo Done!
