#!/bin/bash

#usage() {
#    echo "* * * * * * * 使 用 说 明: * * * * * * *"
#    echo "sh startup.sh -p [SALT_KEY]"
#    echo "sh startup.sh -p password"
#    echo "* * * * * * * * * * * * * * * * * * * *"
#    exit 1
#}

export SERVER_NAME="deil-component"
export SERVER_VERSION="1.0.0"
export PSW="password"

cygwin=false
darwin=false
os400=false
case "`uname`" in
CYGWIN*) cygwin=true;;
Darwin*) darwin=true;;
OS400*) os400=true;;
esac
error_exit ()
{
    echo "ERROR: $1 !!"
    exit 1
}
[ ! -e "$JAVA_HOME/bin/java" ] && JAVA_HOME=$HOME/jdk/java
[ ! -e "$JAVA_HOME/bin/java" ] && JAVA_HOME=/usr/java
[ ! -e "$JAVA_HOME/bin/java" ] && JAVA_HOME=/opt/taobao/java
[ ! -e "$JAVA_HOME/bin/java" ] && unset JAVA_HOME

if [ -z "$JAVA_HOME" ]; then
  if $darwin; then
    if [ -x '/usr/libexec/java_home' ] ; then
      export JAVA_HOME=`/usr/libexec/java_home`
    elif [ -d "/System/Library/Frameworks/JavaVM.framework/Versions/CurrentJDK/Home" ]; then
      export JAVA_HOME="/System/Library/Frameworks/JavaVM.framework/Versions/CurrentJDK/Home"
    fi
  else
    JAVA_PATH=`dirname $(readlink -f $(which javac))`
    if [ "x$JAVA_PATH" != "x" ]; then
      export JAVA_HOME=`dirname $JAVA_PATH 2>/dev/null`
    fi
  fi

  if [ -z "$JAVA_HOME" ]; then
        error_exit "************ PLEASE CHECK U‘R JAVA_HOME ENVIRONMENT! ************"
  fi
fi

export SERVER="${SERVER_NAME}-${SERVER_VERSION}"
while getopts ":p:s:" opt
do
    case $opt in
        p)
            PSW=$OPTARG;;
        s)
            SERVER=$OPTARG;;
        ?)
        echo "UNKNOWN PARAMETER"
        exit 1;;
    esac
done

export JAVA_HOME
export JAVA="$JAVA_HOME/bin/java"
export BASE_DIR=`cd $(dirname $0)/..; pwd`

JAVA_MAJOR_VERSION=$($JAVA -version 2>&1 | sed -E -n 's/.* version "([0-9]*).*$/\1/p')
if [[ "$JAVA_MAJOR_VERSION" -ge "9" ]] ; then
  JAVA_OPT="${JAVA_OPT} -Xlog:gc*:file=${BASE_DIR}/bin/logs/${SERVER_NAME}_gc.log:time,tags:filecount=10,filesize=102400"
else
  JAVA_OPT_EXT_FIX="-Djava.ext.dirs=${JAVA_HOME}/jre/lib/ext:${JAVA_HOME}/lib/ext"
  JAVA_OPT="${JAVA_OPT} -Xloggc:${BASE_DIR}/bin/logs/${SERVER_NAME}_gc.log -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=100M"
fi

JAVA_OPT="${JAVA_OPT} -Dfile.encoding=utf-8"
JAVA_OPT="${JAVA_OPT} -jar ${BASE_DIR}/target/${SERVER}.jar"
JAVA_OPT="${JAVA_OPT} --jasypt.encryptor.password=${PSW}"

if [ ! -d "${BASE_DIR}/bin/logs" ]; then
  mkdir ${BASE_DIR}/bin/logs
fi

if [ ! -f "${BASE_DIR}/bin/logs/start.out" ]; then
  touch "${BASE_DIR}/bin/logs/start.out"
fi

echo "$JAVA $JAVA_OPT_EXT_FIX ${JAVA_OPT}" > ${BASE_DIR}/bin/logs/start.out 2>&1 &

if [[ "$JAVA_OPT_EXT_FIX" == "" ]]; then
  nohup "$JAVA" ${JAVA_OPT} ${SERVER_NAME} >> ${BASE_DIR}/bin/logs/start.out 2>&1 &
else
  nohup "$JAVA" "$JAVA_OPT_EXT_FIX" ${JAVA_OPT} ${SERVER_NAME} >> ${BASE_DIR}/bin/logs/start.out 2>&1 &
fi

echo "************ ${SERVER_NAME} IS STARTING，CHECK THE ${BASE_DIR}/bin/logs/start.out ************"
