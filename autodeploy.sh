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