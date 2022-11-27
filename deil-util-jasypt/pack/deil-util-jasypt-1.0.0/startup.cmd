@echo off
rem Copyright 1999-2018 Alibaba Group Holding Ltd.
if not exist "%JAVA_HOME%\bin\java.exe" echo Please set the JAVA_HOME variable in your environment, We need java(x64)! jdk8 or later is better! & EXIT /B 1
set "JAVA=%JAVA_HOME%\bin\java.exe"

setlocal enabledelayedexpansion

set BASE_DIR=%~dp0
set BASE_DIR="%BASE_DIR:~0,-5%"

set SERVER_VERSION=1.0.0
set SERVER_NAME=utils-jasypt
set SERVER=%SERVER_NAME%-%SERVER_VERSION%

set "JAR_OPTS=-jar %SERVER%.jar"

set COMMAND="%JAVA%" %JAR_OPTS% %SERVER_NAME% %*

%COMMAND%
