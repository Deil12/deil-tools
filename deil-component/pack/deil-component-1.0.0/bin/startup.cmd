@echo off
if not exist "%JAVA_HOME%\bin\java.exe" echo PLEASE CHECK UR JAVA_HOME ENVIRONMENT! & EXIT /B 1
set "JAVA=%JAVA_HOME%\bin\java.exe"

setlocal enabledelayedexpansion

set BASE_DIR=%~dp0
set BASE_DIR="%BASE_DIR:~0,-5%"

set SERVER=customsMoveService-1.0.0
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
    if %PSW_INDEX% == !i! ( set MODE="%%a" )
    if %SERVER_INDEX% == !i! ( set SERVER="%%a" )
    set /a i+=1
)

set "SERVICE_OPTS=-jar %BASE_DIR%\%SERVER%.jar"
set "SERVICE_OPTS=%SERVICE_OPTS% --jasypt.encryptor.password=%PSW%"

set COMMAND="%JAVA%" %SERVICE_OPTS% tang.customsMoveService

%COMMAND%
