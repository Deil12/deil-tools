#!/bin/bash

cd `dirname $0`/..
target_dir=`pwd`

pid=`ps ax | grep -i 'deil-quartz' | grep ${target_dir} | grep java | grep -v grep | awk '{print $1}'`
if [ -z "$pid" ] ; then
        echo "\033[42;30m************ NO SERVICE RUNNING... ************\033[0m"
        exit -1;
fi

echo "\033[42;31m************ THE SERVICE(${pid}) IS RUNNING... ************\033[0m"

kill ${pid}

echo "\033[42;30m************ SHUTDOWN SERVICE(${pid}) SUCCESS ************\033[0m"
