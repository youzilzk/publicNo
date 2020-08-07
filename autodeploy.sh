#!/usr/bin/env bash

cd /nurxat/publicNo &&
echo -n 'current dictionary : '&& pwd echo ' as following:' &&
ls -l &&
mvn clean &&
echo -n 'current dictionary : '&& pwd echo ' as following:' &&
ls -l &&
git pull &&
echo -n 'current dictionary : '&& pwd echo ' as following:' &&
ls -l &&
mvn install &&
echo -n 'current dictionary : '&& pwd echo ' as following:' &&
ls -l &&
cd ./target &&
echo -n 'current dictionary : '&& pwd echo ' as following:' &&
ls -l
a=$(netstat -tunlp|grep 8000|awk '{print $7}')
string=$a
#对IFS变量 进行替换处理
OLD_IFS="$IFS"
IFS="/"
array=($string)
IFS="$OLD_IFS"
kill 9  ${array[0]}&&
sleep 2
echo -n 'current running process as following : '&&
ps -ef
echo 'process is restarting ... '&&
nohup java -jar publicNo-1.0.jar
