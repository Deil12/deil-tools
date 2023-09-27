#!/bin/bash

################################################## 同 步 更 新 本 脚 本 ##################################################
#
###################################################### 目 录 结 构 ######################################################
# XXX-1.0.0/ (保持根目录名与jar包名相同)
#     |-- bin/
#         |-- XXXService (脚本文件)
#     |-- XXX-1.0.0.jar (SpringBoot打包生成的jar)

#export PROFILES="prod"
export PROFILES="test"

export BASE_DIR=$(cd $(dirname $0)/.. && pwd)
export SERVER=${BASE_DIR##*/}
SERVER_NAME=${BASE_DIR%-*}
export SERVER_NAME=${SERVER_NAME##*/}
export SERVER_VERSION=${BASE_DIR##*-}
export SERVER="${SERVER_NAME}-${SERVER_VERSION}"

export SALT=""
export LOG=""
export FIND=LogId

if [ "$1" != "" ]; then
  export ACT="$1"
fi

while getopts ":s:p:n:l:f" opt; do
  case $opt in
  s)
    SALT=$OPTARG
    ;;
  p)
    PROFILES=$OPTARG
    ;;
  n)
    SERVER=$OPTARG
    ;;
  l)
    LOG=$OPTARG
    ;;
  f)
    FIND=$OPTARG
    ;;
  ?)
    echo "
  * * * * * * * * * * * * * * *  ATTENTION: * * * * * * * * * * * * * *
    sh $0 -s [SALT] -p [PROFILES] -n [SERVER] -l [LOG] -f [KEYWORD]

                -s [加密盐]
                -p [配置环境]
                -n [服务名]
                -l [日志文件]
                -f [日志关键字]
                -h [帮助]
  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    "
    exit 1
    ;;
  esac
done

if [[ "$PROFILES" != "prod" ]]; then
  export STARTLOG=${BASE_DIR}/bin/logs/start.log
  LOG=${STARTLOG}
  SALT="password"
else
  export STARTLOG=/dev/null
  LOG=${BASE_DIR}/bin/logs/info-${SERVER_NAME}.log
  SALT="background-prod"
fi

function off() {
  pid=$(ps ax | grep -i ${SERVER} | grep ${BASE_DIR} | grep java | grep -v grep | awk '{print $1}')
  if [ -n "$pid" ]; then
    kill ${pid}
    echo -e "\033[42;31m************ KILLING THE ${SERVER}(${pid})... ************\033[0m"
  else
    echo -e "\033[42;31m************ ${SERVER} NOT ON ************\033[0m"
  fi
}

function on() {
  cygwin=false
  darwin=false
  os400=false
  case "$(uname)" in
  CYGWIN*) cygwin=true ;;
  Darwin*) darwin=true ;;
  OS400*) os400=true ;;
  esac
  error_exit() {
    echo "ERROR: $1 !!"
    exit 1
  }
  [ ! -e "$JAVA_HOME/bin/java" ] && JAVA_HOME=$HOME/jdk/java
  [ ! -e "$JAVA_HOME/bin/java" ] && JAVA_HOME=/usr/java
  [ ! -e "$JAVA_HOME/bin/java" ] && JAVA_HOME=/opt/java
  [ ! -e "$JAVA_HOME/bin/java" ] && unset JAVA_HOME

  if [ -z "$JAVA_HOME" ]; then
    if $darwin; then
      if [ -x '/usr/libexec/java_home' ]; then
        export JAVA_HOME=$(/usr/libexec/java_home)
      elif [ -d "/System/Library/Frameworks/JavaVM.framework/Versions/CurrentJDK/Home" ]; then
        export JAVA_HOME="/System/Library/Frameworks/JavaVM.framework/Versions/CurrentJDK/Home"
      fi
    else
      JAVA_PATH=$(dirname $(readlink -f $(which javac)))
      if [ "x$JAVA_PATH" != "x" ]; then
        export JAVA_HOME=$(dirname $JAVA_PATH 2>/dev/null)
      fi
    fi

    if [ -z "$JAVA_HOME" ]; then
      error_exit "************ PLEASE CHECK U‘R JAVA_HOME ENVIRONMENT! ************"
    fi
  fi
  export JAVA_HOME
  export JAVA="$JAVA_HOME/bin/java"

  JAVA_MAJOR_VERSION=$($JAVA -version 2>&1 | sed -E -n 's/.* version "([0-9]*).*$/\1/p')
  if [[ "$JAVA_MAJOR_VERSION" -ge "9" ]]; then
    JAVA_OPT="${JAVA_OPT} -Xlog:gc*:file=${BASE_DIR}/bin/logs/${SERVER_NAME}_gc.log:time,tags:filecount=10,filesize=102400"
  else
    JAVA_OPT_EXT_FIX="-Djava.ext.dirs=${JAVA_HOME}/jre/lib/ext:${JAVA_HOME}/lib/ext"
    JAVA_OPT="${JAVA_OPT} -Xloggc:${BASE_DIR}/bin/logs/${SERVER_NAME}_gc.log -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=100M"
  fi

  JAVA_OPT="${JAVA_OPT} -Dfile.encoding=utf-8"
  JAVA_OPT="${JAVA_OPT} -Dspring.profiles.active=${PROFILES}"
  JAVA_OPT="${JAVA_OPT} -Djasypt.encryptor.password=${SALT}"
  JAVA_OPT="${JAVA_OPT} -jar ${BASE_DIR}/${SERVER}.jar"

  pid=$(ps ax | grep -i ${SERVER} | grep ${BASE_DIR} | grep java | grep -v grep | awk '{print $1}')
  if [ -n "$pid" ]; then
    kill ${pid}
    echo -e "\033[42;31m************ KILLING THE ${SERVER}(${pid})... ************\033[0m"
  fi

  if [ ! -d "${BASE_DIR}/bin/logs" ]; then
    mkdir ${BASE_DIR}/bin/logs
  fi

  if [ ! -f "${LOG}" ]; then
    touch "${LOG}"
  fi

  if [[ "$JAVA_OPT_EXT_FIX" == "" ]]; then
    nohup "$JAVA" ${JAVA_OPT} ${SERVER_NAME} >>${STARTLOG} 2>&1 &
  else
    nohup "$JAVA" "$JAVA_OPT_EXT_FIX" ${JAVA_OPT} ${SERVER_NAME} >>${STARTLOG} 2>&1 &
  fi

  echo -e "\033[42;30m************ ${SERVER_NAME}[$PROFILES] STARTING，CHECK THE ${LOG} ************\033[0m"
  tail -f ${LOG}
}

function restart() {
  off

  sleep 3
  if [[ "$PROFILES" != "prod" ]]; then
    mv ${STARTLOG} ${BASE_DIR}/bin/logs/start-${SERVER_NAME}$(date "+%Y%m%d%H%M").log
  fi

  on
}

function status() {
  pid=$(ps ax | grep -i ${SERVER} | grep ${BASE_DIR} | grep java | grep -v grep | awk '{print $1}')
  if [ -n "$pid" ]; then
    echo -e "\033[42;30m ${PROFILES} $(ps ax | grep -i ${SERVER} | grep ${BASE_DIR} | grep java | grep -v grep) \033[0m"
  else
    echo -e "\033[42;31m************ ${SERVER} OFF ************\033[0m"
  fi
}

function logs() {
  echo -e "\033[42;30m************ tail ${LOG} ************\033[0m"

  sleep 3
  if [[ ${FIND} == "LogId" ]]; then
    tail -666f ${LOG}
  else
    tail -666f ${LOG} | grep '$FIND'
  fi
}

case ${ACT} in
on | -s | -p | -n)
  on
  ;;
off)
  off
  ;;
restart)
  restart
  ;;
status | "")
  status
  ;;
logs | -l | -f)
  logs
  ;;
*)
  echo "
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * * * * * * * * * * * * * * *  ATTENTION: * * * * * * * * * * * * * * *
        sh $0 -s [SALT] -p [PROFILES] -n [SERVER] -l [LOG] -f [KEYWORD]
        sh $0 [on | off | restart | status | logs]

                 -s [加密盐]
                 -p [配置环境]
                 -n [服务名]
                 -l [日志文件]
                 -f [日志关键字]
                 -h [帮助]
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    "
  ;;
esac
