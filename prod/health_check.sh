#!/bin/bash
cd /opt
Response=`curl 127.0.0.1:8080/{{ name }}/home`
Value=`echo $Response|awk '{print $2}'`
if [ ! $Value ];then
    Backstage=`ps -ef|grep [j]ava`
    Rc=`echo $?`
    if [ $Rc -ne 0 ];then
        Command=`grep java init.sh |grep -v "^#"`
        `$Command`
    fi
fi