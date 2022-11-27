#!/bin/bash

cd `dirname $0`/..
target_dir=`pwd`

pid=`ps ax | grep -i 'deil-component' | grep ${target_dir} | grep java | grep -v grep | awk '{print $1}'`
if [ -z "$pid" ] ; then
        echo "************ NO SERVICE RUNNING... ************"
        exit -1;
fi

echo "************ THE SERVICE(${pid}) IS RUNNING... ************"

kill ${pid}

echo "************ SHUTDOWN SERVICE(${pid}) SUCCESS ************"
