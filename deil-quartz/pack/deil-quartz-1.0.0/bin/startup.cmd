@echo off
REM usage() {
REM     echo "* * * * * * * 使 用 说 明: * * * * * * *"
REM     echo "startup.cmd -p [SALT_KEY]"
REM     echo "startup.cmd -p password"
REM     echo "* * * * * * * * * * * * * * * * * * * *"
REM     exit 1
REM }
if not exist "%JAVA_HOME%\bin\java.exe" echo PLEASE CHECK UR JAVA_HOME ENVIRONMENT! & EXIT /B 1
set "JAVA=%JAVA_HOME%\bin\java.exe"

setlocal enabledelayedexpansion

set BASE_DIR=%~dp0
set BASE_DIR="%BASE_DIR:~0,-5%"

set SERVER_NAME=deil-quartz
set SERVER_VERSION=1.0.0
set SERVER=%SERVER_NAME%-%SERVER_VERSION%
set PSW_INDEX=-1
set SERVER_INDEX=-1

set i=0
for %%a in (%*) do (
    if "%%a" == "-p" ( set /a PSW_INDEX=!i!+1 )
    if "%%a" == "-s" ( set /a SERVER_INDEX=!i!+1 )
    set /a i+=1
)

set i=0
for %%a in (%*) do (
    if %PSW_INDEX% == !i! ( set PSW="%%a" )
    if %SERVER_INDEX% == !i! ( set SERVER="%%a" )
    set /a i+=1
)

set "SERVICE_OPTS=-jar %BASE_DIR%\target\%SERVER%.jar"
set "SERVICE_OPTS=%SERVICE_OPTS% --jasypt.encryptor.password=%PSW%"

set COMMAND="%JAVA%" %SERVICE_OPTS% %SERVER_NAME%

%COMMAND%
