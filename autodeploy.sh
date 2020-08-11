#!/usr/bin/env bash

cd /nurxat/publicNo &&
echo -en '\e[33mcurrent dictionary : \e[36m'&& pwd echo -e '\e[33m as following:\e[36m' &&
ls -l &&
mvn clean &&
echo -en '\e[33mcurrent dictionary : \e[36m'&& pwd echo -e '\e[33m as following:\e[36m' &&
ls -l &&
git pull &&
echo -en '\e[33mcurrent dictionary : \e[36m'&& pwd echo -e '\e[33m as following:\e[36m' &&
ls -l &&
mvn install &&
echo -en '\e[33mcurrent dictionary : \e[36m'&& pwd echo -e '\e[33m as following:\e[36m' &&
ls -l &&
cd ./target &&
echo -en '\e[33mcurrent dictionary : \e[36m'&& pwd echo -e '\e[33m as following:\e[36m' &&
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
echo -e '\e[33mcurrent running process as following : \e[36m'&&
ps -ef
echo -e '\e[33mprocess is restarting ... \e[36m'&&
nohup java -jar publicNo-1.0.jar
